package me.Zcamt.zgangs.objects.gang;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.gangmembers.GangMembers;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.gangrivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.gangstats.GangStats;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GangManager {

    private final Database database;
    private final Cache<UUID, Gang> gangCache;

    public GangManager(Database database) {
        this.database = database;
        this.gangCache = Caffeine.newBuilder()
                .maximumSize(1000L)
                .expireAfterAccess(3L, TimeUnit.MINUTES)
                .removalListener((RemovalListener<UUID, Gang>) (uuid, gang, cause) -> {
                    if (gang == null) return;
                    gang.serialize();
                }).build();
    }

    public Gang createNewGang(String name, GangPlayer gangOwner) {
        UUID uuid = UUID.randomUUID();

        while (idExistsInDatabase(uuid)) {
            uuid = UUID.randomUUID();
        }

        List<UUID> memberList = new ArrayList<>();
        memberList.add(gangOwner.getUUID());

        int memberLimitForLvl1 = ZGangs.getGangLevelManager().getGangLevelFromInt(1).getMaxMemberLimit();
        int allyLimitForLvl1 = ZGangs.getGangLevelManager().getGangLevelFromInt(1).getMaxAllyLimit();

        Gang gang = new Gang(uuid, gangOwner.getUUID(), System.currentTimeMillis(), name, 1, 0,
                new GangStats(new HashMap<>()),
                new GangMembers(memberLimitForLvl1, memberList, new ArrayList<>()),
                new GangAllies(allyLimitForLvl1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new GangRivals(new ArrayList<>(), new ArrayList<>()),
                new GangPermissions(new HashMap<>()),
                new GangItemDelivery(new HashMap<>()));
        addGangToCache(uuid, gang);
        gang.serialize();
        gangOwner.setGangID(gang.getUUID());
        gangOwner.setGangRank(GangRank.OWNER);
        gangOwner.serialize();
        return gang;
    }

    public boolean deleteGang(Gang gang) {
        if (gang.getGangMembers().getMemberCount() != 1) {
            return false;
        }
        UUID gangUUID = gang.getUUID();

        clearMembersForGang(gang);
        clearAlliesForGang(gang);
        clearRivalsForGang(gang);

        if (isIdInCache(gangUUID)) {
            gangCache.invalidate(gangUUID);
        }
        database.getGangCollection().deleteOne(new Document("_id", gangUUID.toString()));
        return true;
    }

    public boolean forceDeleteGang(Gang gang) {
        UUID gangUUID = gang.getUUID();

        clearMembersForGang(gang);
        clearAlliesForGang(gang);
        clearRivalsForGang(gang);

        if (isIdInCache(gangUUID)) {
            gangCache.invalidate(gangUUID);
        }
        database.getGangCollection().deleteOne(new Document("_id", gangUUID.toString()));
        return true;
    }

    //Todo: Gør alt database stuffs ASYNC
    public Gang findById(UUID uuid) {
        if(uuid == null) {
            return null;
        }
        if (gangCache.asMap().containsKey(uuid)) {
            return gangCache.getIfPresent(uuid);
        }
        Document gangDocument = database.getGangCollection().find(new Document("_id", uuid.toString())).first();
        if (gangDocument == null) {
            return null;
            //throw new NoSuchElementException("Couldn't find gang with UUID '" + uuid + "'");
        }
        Gang gang = ZGangs.GSON.fromJson(gangDocument.toJson(), Gang.class);
        addGangToCache(gang.getUUID(), gang);
        return gang;
    }

    public Gang findByName(String name) {
        if(name == null) {
            return null;
        }
        Document gangDocument = database.getGangCollection().find(new Document("name", name)).first();
        if (gangDocument == null) {
            return null;
            //throw new NoSuchElementException("Couldn't find gang with name '" + name + "'");
        }
        Gang gang = ZGangs.GSON.fromJson(gangDocument.toJson(), Gang.class);
        addGangToCache(gang.getUUID(), gang);
        return gang;
    }

    private void addGangToCache(UUID uuid, Gang gang) {
        gangCache.put(uuid, gang);
    }

    private boolean idExistsInDatabase(UUID uuid) {
        long count = database.getGangCollection().countDocuments(new Document("_id", uuid.toString()));
        return count > 0;
    }

    public boolean nameExistsInDatabase(String name) {
        long count = database.getGangCollection().countDocuments(new Document("name", name));
        return count > 0;
    }

    private boolean isIdInCache(UUID uuid) {
        return gangCache.asMap().containsKey(uuid);
    }

    private void clearMembersForGang(Gang gang) {
        GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
        for (UUID memberUUID : gang.getGangMembers().getMemberList()) {
            GangPlayer gangPlayer = gangPlayerManager.findById(memberUUID);
            gang.getGangMembers().removeGangPlayerFromGang(gangPlayer);
        }

        for (UUID invitedUUID : gang.getGangMembers().getPlayerInvites()) {
            GangPlayer gangPlayer = gangPlayerManager.findById(invitedUUID);
            gang.getGangMembers().removePlayerFromInvites(gangPlayer);
        }
    }

    private void clearAlliesForGang(Gang gang) {
        for (UUID allyUUID : gang.getGangAllies().getAlliedGangs()) {
            Gang alliedGang = findById(allyUUID);
            gang.getGangAllies().removeAlly(alliedGang);
        }

        for (UUID outgoingInviteUUID : gang.getGangAllies().getAlliedGangInvitesOutgoing()) {
            Gang outgoingInviteGang = findById(outgoingInviteUUID);
            gang.getGangAllies().removeAllyInviteOutgoing(outgoingInviteGang);
        }

        for (UUID incomingInviteUUID : gang.getGangAllies().getAlliedGangInvitesIncoming()) {
            Gang incomingInviteGang = findById(incomingInviteUUID);
            gang.getGangAllies().removeAllyInviteOutgoing(incomingInviteGang);
        }
    }

    private void clearRivalsForGang(Gang gang) {
        for (UUID rivalUUID : gang.getGangRivals().getRivalGangs()) {
            Gang rivalGang = findById(rivalUUID);
            gang.getGangRivals().removeRival(rivalGang);
        }

        for (UUID rivalAgainstUUID : gang.getGangRivals().getRivalGangsAgainst()) {
            Gang rivalAgainstGang = findById(rivalAgainstUUID);
            gang.getGangRivals().removeRivalAgainst(rivalAgainstGang);
        }
    }

    public void invalidateCache() {
        gangCache.invalidateAll();
    }

}

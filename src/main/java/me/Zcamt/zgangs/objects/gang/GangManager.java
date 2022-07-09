package me.Zcamt.zgangs.objects.gang;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.database.Database;
import me.Zcamt.zgangs.objects.gang.access.GangAccess;
import me.Zcamt.zgangs.objects.gang.allies.GangAllies;
import me.Zcamt.zgangs.objects.gang.itemdelivery.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.members.GangMembers;
import me.Zcamt.zgangs.objects.gang.motd.GangMotd;
import me.Zcamt.zgangs.objects.gang.permissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.rivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.stats.GangStats;
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
                new GangMotd(null, null, null, null, null, null, null),
                new GangStats(0, 0, 0, 0, 0, 0, 0),
                new GangMembers(memberLimitForLvl1, 100, memberList, new ArrayList<>()),
                new GangAllies(allyLimitForLvl1, 100, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                new GangRivals(new ArrayList<>(), new ArrayList<>()),
                new GangPermissions(
                        GangRank.MEMBER,
                        GangRank.CAPTAIN,
                        GangRank.CAPTAIN,
                        GangRank.CAPTAIN,
                        GangRank.CAPTAIN,
                        GangRank.CO_OWNER,
                        GangRank.CAPTAIN,
                        GangRank.CAPTAIN,
                        GangRank.CO_OWNER,
                        GangRank.CAPTAIN),
                new GangItemDelivery(0, 0),
                new GangAccess(false, false, false));
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
        Document gangDocument = database.getGangCollection().find(new Document("name", name))
                .collation(Collation.builder().locale("en").collationStrength(CollationStrength.PRIMARY).build())
                .first();
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
        return findByName(name) != null;
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

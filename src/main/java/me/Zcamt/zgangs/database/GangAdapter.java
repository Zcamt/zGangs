package me.Zcamt.zgangs.database;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.Gang;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GangAdapter extends TypeAdapter<Gang> {
    private final Gson gson = ZGangs.GSON;

    @Override
    public void write(JsonWriter writer, Gang gang) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gang.getUUID().toString());
        writer.name("name").value(gang.getName());
        writer.name("level").value(gang.getLevel());
        writer.name("kills").value(gang.getKills());
        writer.name("deaths").value(gang.getDeaths());
        writer.name("bank").value(gang.getBank());
        writer.name("ownerUUID").value(gang.getOwnerUUID().toString());
        writer.name("members").value(ConversionUtil.gangMemberMapToString(gang.getMemberMap()));
        writer.name("playerInvites").value(ConversionUtil.uuidListToString(gang.getPlayerInvites()));
        writer.name("alliedGangs").value(ConversionUtil.uuidListToString(gang.getAlliedGangs()));
        writer.name("alliedGangInvitesIncoming").value(ConversionUtil.uuidListToString(gang.getAlliedGangInvitesIncoming()));
        writer.name("alliedGangInvitesOutgoing").value(ConversionUtil.uuidListToString(gang.getAlliedGangInvitesOutgoing()));
        writer.name("rivalGangs").value(ConversionUtil.uuidListToString(gang.getRivalGangs()));
        writer.name("rivalGangsAgainst").value(ConversionUtil.uuidListToString(gang.getRivalGangsAgainst()));
        writer.name("rankPermissions").value(ConversionUtil.gangRankPermissionMapToString(gang.getRankPermissionMap()));
        writer.endObject();
    }

    @Override
    public Gang read(JsonReader reader) throws IOException {
        UUID uuid = null;
        String name = null;
        int level = 0;
        int kills = 0;
        int deaths = 0;
        int bank = 0;
        UUID ownerUUID = null;
        HashMap<UUID, Integer> memberMap = null;
        List<UUID> playerInvites = null;
        List<UUID> alliedGangs = null;
        List<UUID> alliedGangInvitesIncoming = null;
        List<UUID> alliedGangInvitesOutgoing = null;
        List<UUID> rivalGangs = null;
        List<UUID> rivalGangsAgainst = null;
        HashMap<String, Integer> rankPermissions = null;
        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "name" -> name = reader.nextString();
                case "level" -> level = reader.nextInt();
                case "kills" -> kills = reader.nextInt();
                case "deaths" -> deaths = reader.nextInt();
                case "bank" -> bank = reader.nextInt();
                case "ownerUUID" -> ownerUUID = UUID.fromString(reader.nextString());
                case "members" -> memberMap = ConversionUtil.stringToGangMemberMap(reader.nextString());
                case "playerInvites" -> playerInvites = ConversionUtil.uuidListFromString(reader.nextString());
                case "alliedGangs" -> alliedGangs = ConversionUtil.uuidListFromString(reader.nextString());
                case "alliedGangInvitesIncoming" -> alliedGangInvitesIncoming = ConversionUtil.uuidListFromString(reader.nextString());
                case "alliedGangInvitesOutgoing" -> alliedGangInvitesOutgoing = ConversionUtil.uuidListFromString(reader.nextString());
                case "rivalGangs" -> rivalGangs = ConversionUtil.uuidListFromString(reader.nextString());
                case "rivalGangsAgainst" -> rivalGangsAgainst = ConversionUtil.uuidListFromString(reader.nextString());
                case "rankPermissions" -> rankPermissions =ConversionUtil.stringToGangRankPermissionMap(reader.nextString());
            }
        }

        //Todo: Maybe check for any variables being null

        Gang gang = new Gang(uuid,
                name,
                level,
                kills,
                deaths,
                bank,
                ownerUUID,
                memberMap,
                playerInvites,
                alliedGangs,
                alliedGangInvitesIncoming,
                alliedGangInvitesOutgoing,
                rivalGangs,
                rivalGangsAgainst,
                rankPermissions);
        reader.endObject();
        return gang;
    }
}

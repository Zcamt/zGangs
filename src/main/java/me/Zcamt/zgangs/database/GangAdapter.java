package me.Zcamt.zgangs.database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.gangallies.GangAllies;
import me.Zcamt.zgangs.objects.gang.gangitem.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.gangpermissions.GangPermissions;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangAdapter extends TypeAdapter<Gang> {

    //Todo: Check if empty lists/maps causes issues.
    @Override
    public void write(JsonWriter writer, Gang gang) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gang.getUUID().toString());
        writer.name("ownerUUID").value(gang.getOwnerUUID().toString());
        writer.name("creationDate").value(gang.getCreationDateMillis());
        writer.name("name").value(gang.getName());
        writer.name("level").value(gang.getLevel());
        writer.name("kills").value(gang.getKills());
        writer.name("guardKills").value(gang.getGuardKills());
        writer.name("officerPlusKills").value(gang.getOfficerPlusKills());
        writer.name("deaths").value(gang.getDeaths());
        writer.name("bank").value(gang.getBank());
        writer.name("maxMembers").value(gang.getMaxMembers());
        writer.name("members").value(ConversionUtil.uuidListToString(gang.getMemberList()));
        writer.name("playerInvites").value(ConversionUtil.uuidListToString(gang.getPlayerInvites()));
        writer.name("gangAllies").value(ZGangs.GSON.toJson(gang.getGangAllies()));
        writer.name("rivalGangs").value(ConversionUtil.uuidListToString(gang.getRivalGangs()));
        writer.name("rivalGangsAgainst").value(ConversionUtil.uuidListToString(gang.getRivalGangsAgainst()));
        writer.name("gangPermissions").value(ZGangs.GSON.toJson(gang.getGangPermissions()));
        writer.name("gangItemDelivery").value(ConversionUtil.gangItemDeliveryToString(gang.getGangItemDelivery().getDeliveredItems()));
        writer.endObject();
    }

    //Todo: Check if empty lists/maps causes issues.
    @Override
    public Gang read(JsonReader reader) throws IOException {
        UUID uuid = null;
        UUID ownerUUID = null;
        long creationDateUnix = 0;
        String name = null;
        int level = 0;
        int kills = 0;
        int guardKills = 0;
        int officerPlusKills = 0;
        int deaths = 0;
        int bank = 0;
        int maxMembers = 0;
        int maxAllies = 0;
        List<UUID> memberList = null;
        List<UUID> playerInvites = null;
        GangAllies gangAllies = null;
        List<UUID> rivalGangs = null;
        List<UUID> rivalGangsAgainst = null;
        GangPermissions gangPermissions = null;
        GangItemDelivery gangItemDelivery = null;
        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "ownerUUID" -> ownerUUID = UUID.fromString(reader.nextString());
                case "creationDate" -> creationDateUnix = reader.nextLong();
                case "name" -> name = reader.nextString();
                case "level" -> level = reader.nextInt();
                case "kills" -> kills = reader.nextInt();
                case "guardKills" -> guardKills = reader.nextInt();
                case "officerPlusKills" -> officerPlusKills = reader.nextInt();
                case "deaths" -> deaths = reader.nextInt();
                case "bank" -> bank = reader.nextInt();
                case "maxMembers" -> maxMembers = reader.nextInt();
                case "maxAllies" -> maxAllies = reader.nextInt();
                case "members" -> memberList = ConversionUtil.uuidListFromString(reader.nextString());
                case "playerInvites" -> playerInvites = ConversionUtil.uuidListFromString(reader.nextString());
                case "gangAllies" -> gangAllies = ZGangs.GSON.fromJson(reader.nextString(), GangAllies.class);
                case "rivalGangs" -> rivalGangs = ConversionUtil.uuidListFromString(reader.nextString());
                case "rivalGangsAgainst" -> rivalGangsAgainst = ConversionUtil.uuidListFromString(reader.nextString());
                case "gangPermissions" -> gangPermissions = ZGangs.GSON.fromJson(reader.nextString(), GangPermissions.class);
                case "gangItemDelivery" -> gangItemDelivery = ConversionUtil.gangItemDeliveryFromString(reader.nextString());
            }
        }

        //Todo: Maybe check for any variables being null

        Gang gang = new Gang(uuid,
                ownerUUID,
                creationDateUnix,
                name,
                level,
                kills,
                guardKills,
                officerPlusKills,
                deaths,
                bank,
                maxMembers,
                memberList,
                playerInvites,
                gangAllies,
                rivalGangs,
                rivalGangsAgainst,
                gangPermissions,
                gangItemDelivery);
        reader.endObject();
        return gang;
    }
}

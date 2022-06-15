package me.Zcamt.zgangs.objects.gang;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.allies.GangAllies;
import me.Zcamt.zgangs.objects.gang.itemdelivery.GangItemDelivery;
import me.Zcamt.zgangs.objects.gang.members.GangMembers;
import me.Zcamt.zgangs.objects.gang.motd.GangMotd;
import me.Zcamt.zgangs.objects.gang.permissions.GangPermissions;
import me.Zcamt.zgangs.objects.gang.rivals.GangRivals;
import me.Zcamt.zgangs.objects.gang.stats.GangStats;

import java.io.IOException;
import java.util.UUID;

public class GangAdapter extends TypeAdapter<Gang> {

    @Override
    public void write(JsonWriter writer, Gang gang) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gang.getUUID().toString());
        writer.name("ownerUUID").value(gang.getOwnerUUID().toString());
        writer.name("creationDate").value(String.valueOf(gang.getCreationDateMillis()));
        writer.name("name").value(gang.getName());
        writer.name("level").value(gang.getLevel());
        writer.name("bank").value(gang.getBank());
        writer.name("gangMotd").value(ZGangs.GSON.toJson(gang.getGangMotd()));
        writer.name("gangStats").value(ZGangs.GSON.toJson(gang.getGangStats()));
        writer.name("gangMembers").value(ZGangs.GSON.toJson(gang.getGangMembers()));
        writer.name("gangAllies").value(ZGangs.GSON.toJson(gang.getGangAllies()));
        writer.name("gangRivals").value(ZGangs.GSON.toJson(gang.getGangRivals()));
        writer.name("gangPermissions").value(ZGangs.GSON.toJson(gang.getGangPermissions()));
        writer.name("gangItemDelivery").value(ZGangs.GSON.toJson(gang.getGangItemDelivery()));
        writer.endObject();
    }

    @Override
    public Gang read(JsonReader reader) throws IOException {
        UUID uuid = null;
        UUID ownerUUID = null;
        long creationDateUnix = 0;
        String name = null;
        int level = 1;
        int bank = 0;
        GangMotd gangMotd = null;
        GangStats gangStats = null;
        GangMembers gangMembers = null;
        GangAllies gangAllies = null;
        GangRivals gangRivals = null;
        GangPermissions gangPermissions = null;
        GangItemDelivery gangItemDelivery = null;
        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "ownerUUID" -> ownerUUID = UUID.fromString(reader.nextString());
                case "creationDate" -> creationDateUnix = Long.parseLong(reader.nextString());
                case "name" -> name = reader.nextString();
                case "level" -> level = reader.nextInt();
                case "bank" -> bank = reader.nextInt();
                case "gangMotd" -> gangMotd = ZGangs.GSON.fromJson(reader.nextString(), GangMotd.class);
                case "gangStats" -> gangStats = ZGangs.GSON.fromJson(reader.nextString(), GangStats.class);
                case "gangMembers" -> gangMembers = ZGangs.GSON.fromJson(reader.nextString(), GangMembers.class);
                case "gangAllies" -> gangAllies = ZGangs.GSON.fromJson(reader.nextString(), GangAllies.class);
                case "gangRivals" -> gangRivals = ZGangs.GSON.fromJson(reader.nextString(), GangRivals.class);
                case "gangPermissions" -> gangPermissions = ZGangs.GSON.fromJson(reader.nextString(), GangPermissions.class);
                case "gangItemDelivery" -> gangItemDelivery = ZGangs.GSON.fromJson(reader.nextString(), GangItemDelivery.class);
            }
        }

        Gang gang = new Gang(uuid,
                ownerUUID,
                creationDateUnix,
                name,
                level,
                bank,
                gangMotd, gangStats,
                gangMembers,
                gangAllies,
                gangRivals,
                gangPermissions,
                gangItemDelivery);
        reader.endObject();
        return gang;
    }
}

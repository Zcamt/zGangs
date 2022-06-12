package me.Zcamt.zgangs.objects.gang.gangallies;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangAlliesAdapter extends TypeAdapter<GangAllies> {

    @Override
    public void write(JsonWriter writer, GangAllies gangAllies) throws IOException {
        writer.beginObject();
        writer.name("maxAllies").value(gangAllies.getMaxAllies());
        writer.name("allyDamagePercent").value(gangAllies.getAllyDamagePercent());
        writer.name("alliedGangs").value(ConversionUtil.uuidListToString(gangAllies.getAlliedGangs()));
        writer.name("alliedGangInvitesIncoming").value(ConversionUtil.uuidListToString(gangAllies.getAlliedGangInvitesIncoming()));
        writer.name("alliedGangInvitesOutgoing").value(ConversionUtil.uuidListToString(gangAllies.getAlliedGangInvitesIncoming()));
        writer.endObject();
    }

    @Override
    public GangAllies read(JsonReader reader) throws IOException {
        int maxAllies = 0;
        int allyDamagePercent = 100;
        List<UUID> alliedGangs = null;
        List<UUID> alliedGangInvitesIncoming = null;
        List<UUID> alliedGangInvitesOutgoing = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "maxAllies" -> maxAllies = reader.nextInt();
                case "allyDamagePercent" -> allyDamagePercent = reader.nextInt();
                case "alliedGangs" -> alliedGangs = ConversionUtil.uuidListFromString(reader.nextString());
                case "alliedGangInvitesIncoming" -> alliedGangInvitesIncoming = ConversionUtil.uuidListFromString(reader.nextString());
                case "alliedGangInvitesOutgoing" -> alliedGangInvitesOutgoing = ConversionUtil.uuidListFromString(reader.nextString());
            }
        }

        //Todo: Maybe check for any variables being null
        GangAllies gangAllies = new GangAllies(maxAllies, allyDamagePercent, alliedGangs, alliedGangInvitesIncoming, alliedGangInvitesOutgoing);
        reader.endObject();
        return gangAllies;
    }
}

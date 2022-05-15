package me.Zcamt.zgangs.objects.gang.gangrivals;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangRivalsAdapter extends TypeAdapter<GangRivals> {
    @Override
    public void write(JsonWriter writer, GangRivals gangRivals) throws IOException {
        writer.beginObject();
        writer.name("rivalGangs").value(ConversionUtil.uuidListToString(gangRivals.getRivalGangs()));
        writer.name("rivalGangsAgainst").value(ConversionUtil.uuidListToString(gangRivals.getRivalGangsAgainst()));
        writer.endObject();
    }

    @Override
    public GangRivals read(JsonReader reader) throws IOException {
        List<UUID> rivalGangs = null;
        List<UUID> rivalGangsAgainst = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "rivalGangs" -> rivalGangs = ConversionUtil.uuidListFromString(reader.nextString());
                case "rivalGangsAgainst" -> rivalGangsAgainst = ConversionUtil.uuidListFromString(reader.nextString());
            }
        }

        //Todo: Maybe check for any variables being null
        GangRivals gangRivals = new GangRivals(rivalGangs, rivalGangsAgainst);
        reader.endObject();
        return gangRivals;
    }
}

package me.Zcamt.zgangs.objects.gang.motd;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GangMotdAdapter extends TypeAdapter<GangMotd> {

    @Override
    public void write(JsonWriter writer, GangMotd gangMotd) throws IOException {
        writer.beginObject();
        writer.name("line1").value(gangMotd.getLine1());
        writer.name("line2").value(gangMotd.getLine2());
        writer.name("line3").value(gangMotd.getLine3());
        writer.name("line4").value(gangMotd.getLine4());
        writer.name("line5").value(gangMotd.getLine5());
        writer.name("line6").value(gangMotd.getLine6());
        writer.name("line7").value(gangMotd.getLine7());
        writer.endObject();
    }

    @Override
    public GangMotd read(JsonReader reader) throws IOException {
        String line1 = null;
        String line2 = null;
        String line3 = null;
        String line4 = null;
        String line5 = null;
        String line6 = null;
        String line7 = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "line1" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line1 = line;
                    }
                }
                case "line2" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line2 = line;
                    }
                }
                case "line3" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line3 = line;
                    }
                }
                case "line4" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line4 = line;
                    }
                }
                case "line5" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line5 = line;
                    }
                }
                case "line6" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line6 = line;
                    }
                }
                case "line7" -> {
                    String line = reader.nextString();
                    if(!line.equalsIgnoreCase("")) {
                        line7 = line;
                    }
                }
            }
        }

        GangMotd gangMotd = new GangMotd(line1, line2, line3, line4, line5, line6, line7);
        reader.endObject();
        return gangMotd;
    }
}

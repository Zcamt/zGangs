package me.Zcamt.zgangs.objects.gang.access;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GangAccessAdapter extends TypeAdapter<GangAccess> {
    @Override
    public void write(JsonWriter writer, GangAccess gangAccess) throws IOException {
        writer.beginObject();
        writer.name("gangAreaCUnlocked").value(gangAccess.isGangAreaCUnlocked());
        writer.name("gangAreaBUnlocked").value(gangAccess.isGangAreaBUnlocked());
        writer.name("gangAreaAUnlocked").value(gangAccess.isGangAreaAUnlocked());
        writer.endObject();
    }

    @Override
    public GangAccess read(JsonReader reader) throws IOException {
        boolean gangAreaCUnlocked = false;
        boolean gangAreaBUnlocked = false;
        boolean gangAreaAUnlocked = false;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "gangAreaCUnlocked" -> gangAreaCUnlocked = reader.nextBoolean();
                case "gangAreaBUnlocked" -> gangAreaBUnlocked = reader.nextBoolean();
                case "gangAreaAUnlocked" -> gangAreaAUnlocked = reader.nextBoolean();
            }
        }

        GangAccess gangAccess = new GangAccess(gangAreaCUnlocked, gangAreaBUnlocked, gangAreaAUnlocked);
        reader.endObject();
        return gangAccess;
    }
}

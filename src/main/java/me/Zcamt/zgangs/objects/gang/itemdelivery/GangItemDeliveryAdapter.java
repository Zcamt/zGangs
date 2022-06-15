package me.Zcamt.zgangs.objects.gang.itemdelivery;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GangItemDeliveryAdapter extends TypeAdapter<GangItemDelivery> {
    @Override
    public void write(JsonWriter writer, GangItemDelivery gangItemDelivery) throws IOException {
        writer.beginObject();
        writer.name("breadDelivered").value(gangItemDelivery.getBreadDelivered());
        writer.name("cigsDelivered").value(gangItemDelivery.getCigsDelivered());
        writer.endObject();
    }

    @Override
    public GangItemDelivery read(JsonReader reader) throws IOException {
        int breadDelivered = 0;
        int cigsDelivered = 0;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "breadDelivered" -> breadDelivered = reader.nextInt();
                case "cigsDelivered" -> cigsDelivered = reader.nextInt();
            }
        }

        GangItemDelivery gangItemDelivery = new GangItemDelivery(breadDelivered, cigsDelivered);
        reader.endObject();
        return gangItemDelivery;
    }
}

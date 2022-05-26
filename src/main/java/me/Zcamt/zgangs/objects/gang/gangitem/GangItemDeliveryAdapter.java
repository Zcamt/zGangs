package me.Zcamt.zgangs.objects.gang.gangitem;

import com.google.common.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class GangItemDeliveryAdapter extends TypeAdapter<GangItemDelivery> {
    @Override
    public void write(JsonWriter writer, GangItemDelivery gangItemDelivery) throws IOException {
        writer.beginObject();
        writer.name("deliveredItems").value(ZGangs.GSON.toJson(gangItemDelivery.getDeliveredItemsMap()));
        writer.endObject();
    }

    @Override
    public GangItemDelivery read(JsonReader reader) throws IOException {
        Map<GangDeliveryItem, Integer> itemDeliveryMap = null;
        Type gangItemDeliveryMapType = new TypeToken<Map<GangDeliveryItem, Integer>>(){}.getType();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "deliveredItems" -> itemDeliveryMap = ZGangs.GSON.fromJson(reader.nextString(), gangItemDeliveryMapType);
            }
        }

        //Todo: Maybe check for any variables being null
        GangItemDelivery gangItemDelivery = new GangItemDelivery(itemDeliveryMap);
        reader.endObject();
        return gangItemDelivery;
    }
}

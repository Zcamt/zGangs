package me.Zcamt.zgangs.objects.gang.gangstats;

import com.google.common.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class GangStatsAdapter extends TypeAdapter<GangStats> {
    @Override
    public void write(JsonWriter writer, GangStats gangStats) throws IOException {
        writer.beginObject();
        writer.name("stats").value(ZGangs.GSON.toJson(gangStats.getStatsMap()));
        writer.endObject();
    }

    @Override
    public GangStats read(JsonReader reader) throws IOException {
        Map<GangStat, Integer> stats = null;
        Type gangStatsMapType = new TypeToken<Map<GangStat, Integer>>(){}.getType();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "stats" -> stats = ZGangs.GSON.fromJson(reader.nextString(), gangStatsMapType);
            }
        }
        //Todo: Maybe check for any variables being null
        GangStats gangStats = new GangStats(stats);
        reader.endObject();
        return gangStats;
    }
}

package me.Zcamt.zgangs.objects.gang.stats;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GangStatsAdapter extends TypeAdapter<GangStats> {
    @Override
    public void write(JsonWriter writer, GangStats gangStats) throws IOException {
        writer.beginObject();
        writer.name("kills").value(gangStats.getKills());
        writer.name("deaths").value(gangStats.getDeaths());
        writer.name("guard_kills_in_c").value(gangStats.getGuard_kills_in_c());
        writer.name("guard_kills_in_b").value(gangStats.getGuard_kills_in_b());
        writer.name("guard_kills_in_a").value(gangStats.getGuard_kills_in_a());
        writer.name("total_guard_kills").value(gangStats.getTotal_guard_kills());
        writer.name("officer_plus_kills").value(gangStats.getOfficer_plus_kills());
        writer.endObject();
    }

    @Override
    public GangStats read(JsonReader reader) throws IOException {
        int kills = 0;
        int deaths = 0;
        int guard_kills_in_c = 0;
        int guard_kills_in_b = 0;
        int guard_kills_in_a = 0;
        int total_guard_kills = 0;
        int officer_plus_kills = 0;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "kills" -> kills = reader.nextInt();
                case "deaths" -> deaths = reader.nextInt();
                case "guard_kills_in_c" -> guard_kills_in_c = reader.nextInt();
                case "guard_kills_in_b" -> guard_kills_in_b = reader.nextInt();
                case "guard_kills_in_a" -> guard_kills_in_a = reader.nextInt();
                case "total_guard_kills" -> total_guard_kills = reader.nextInt();
                case "officer_plus_kills" -> officer_plus_kills = reader.nextInt();
            }
        }

        GangStats gangStats = new GangStats(kills, deaths,
                guard_kills_in_c, guard_kills_in_b, guard_kills_in_a, total_guard_kills,
                officer_plus_kills);
        reader.endObject();
        return gangStats;
    }
}

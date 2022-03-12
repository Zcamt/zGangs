package me.Zcamt.zgangs.objects;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangPlayerAdapter extends TypeAdapter<GangPlayer> {
    private final Gson gson = ZGangs.GSON;

    @Override
    public void write(JsonWriter writer, GangPlayer gangPlayer) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gangPlayer.getUUID().toString());
        writer.name("gangUUID").value(gangPlayer.getUUID().toString());
        writer.name("gangRank").value(gangPlayer.getGangRank());
        writer.name("gangInvites").value(Utils.uuidListToString(gangPlayer.getGangInvites()));
        writer.endObject();
    }

    @Override
    public GangPlayer read(JsonReader reader) throws IOException {
        UUID uuid = null;
        UUID gangUUID = null;
        int gangRank = 0;
        List<UUID> gangInvites = null;

        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "gangUUID" -> gangUUID = UUID.fromString(reader.nextString());
                case "gangRank" -> gangRank = reader.nextInt();
                case "gangInvites" -> gangInvites = Utils.uuidListFromString(reader.nextString());
            }
        }

        GangPlayer gangPlayer = new GangPlayer(uuid, gangUUID, gangRank, gangInvites);
        reader.endObject();
        return gangPlayer;
    }
}

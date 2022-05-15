package me.Zcamt.zgangs.objects.gangplayer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangPlayerAdapter extends TypeAdapter<GangPlayer> {
    private final Gson gson = ZGangs.GSON;

    //Todo: Check if empty lists causes issues.
    @Override
    public void write(JsonWriter writer, GangPlayer gangPlayer) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gangPlayer.getUUID().toString());
        writer.name("gangUUID").value(gangPlayer.getUUID().toString());
        writer.name("gangRank").value(gangPlayer.getGangRank().getID());
        writer.name("gangInvites").value(ConversionUtil.uuidListToString(gangPlayer.getGangInvites()));
        writer.endObject();
    }

    //Todo: Check if empty lists causes issues.
    @Override
    public GangPlayer read(JsonReader reader) throws IOException {
        UUID uuid = null;
        UUID gangUUID = null;
        GangRank gangRank = null;
        List<UUID> gangInvites = null;

        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "gangUUID" -> gangUUID = UUID.fromString(reader.nextString());
                case "gangRank" -> gangRank = GangRank.getRank(reader.nextInt());
                case "gangInvites" -> gangInvites = ConversionUtil.uuidListFromString(reader.nextString());
            }
        }

        //Todo: Maybe check for any variables being null

        GangPlayer gangPlayer = new GangPlayer(uuid, gangUUID, gangRank, gangInvites);
        reader.endObject();
        return gangPlayer;
    }
}

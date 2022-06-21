package me.Zcamt.zgangs.objects.gangplayer;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.settings.GangPlayerSettings;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangPlayerAdapter extends TypeAdapter<GangPlayer> {

    @Override
    public void write(JsonWriter writer, GangPlayer gangPlayer) throws IOException {
        writer.beginObject();
        writer.name("_id").value(gangPlayer.getUUID().toString());
        String gangUUID;
        if(gangPlayer.getGangUUID() == null) {
            gangUUID = "";
        } else {
            gangUUID = gangPlayer.getGangUUID().toString();
        }
        writer.name("gangUUID").value(gangUUID);
        writer.name("gangRank").value(gangPlayer.getGangRank().getID());
        writer.name("gangInvites").value(ConversionUtil.uuidListToString(gangPlayer.getGangInvites()));
        writer.name("gangPlayerSettings").value(ZGangs.GSON.toJson(gangPlayer.getGangPlayerSettings()));
        writer.endObject();
    }

    @Override
    public GangPlayer read(JsonReader reader) throws IOException {
        UUID uuid = null;
        UUID gangUUID = null;
        GangRank gangRank = null;
        List<UUID> gangInvites = null;
        GangPlayerSettings gangPlayerSettings = null;

        reader.beginObject();

        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "_id" -> uuid = UUID.fromString(reader.nextString());
                case "gangUUID" -> {
                    String gangUUIDString = reader.nextString();
                    if(gangUUIDString.equalsIgnoreCase("")) {
                        gangUUID = null;
                    } else {
                        gangUUID = UUID.fromString(gangUUIDString);
                    }
                }
                case "gangRank" -> gangRank = GangRank.getRank(reader.nextInt());
                case "gangInvites" -> gangInvites = ConversionUtil.uuidListFromString(reader.nextString());
                case "gangPlayerSettings" -> gangPlayerSettings = ZGangs.GSON.fromJson(reader.nextString(), GangPlayerSettings.class);
            }
        }

        GangPlayer gangPlayer = new GangPlayer(uuid, gangUUID, gangRank, gangInvites, gangPlayerSettings);
        reader.endObject();
        return gangPlayer;
    }
}

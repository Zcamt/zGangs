package me.Zcamt.zgangs.objects.gangplayer.settings;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GangPlayerSettingsAdapter extends TypeAdapter<GangPlayerSettings> {
    @Override
    public void write(JsonWriter writer, GangPlayerSettings gangPlayerSettings) throws IOException {
        writer.beginObject();
        writer.name("receiveGangChat").value(gangPlayerSettings.isReceiveGangChat());
        writer.name("receiveAllyChat").value(gangPlayerSettings.isReceiveAllyChat());
        writer.name("receiveMemberConnectNotification").value(gangPlayerSettings.isReceiveMemberConnectNotification());
        writer.name("receiveMemberDisconnectNotification").value(gangPlayerSettings.isReceiveMemberDisconnectNotification());
        writer.endObject();
    }

    @Override
    public GangPlayerSettings read(JsonReader reader) throws IOException {
        boolean receiveGangChat = true;
        boolean receiveAllyChat = true;
        boolean receiveMemberConnectNotification = true;
        boolean receiveMemberDisconnectNotification = true;
        reader.beginObject();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "receiveGangChat" -> receiveGangChat = reader.nextBoolean();
                case "receiveAllyChat" -> receiveAllyChat = reader.nextBoolean();
                case "receiveMemberConnectNotification" -> receiveMemberConnectNotification = reader.nextBoolean();
                case "receiveMemberDisconnectNotification" -> receiveMemberDisconnectNotification = reader.nextBoolean();
            }
        }

        GangPlayerSettings gangPlayerSettings =
                new GangPlayerSettings(receiveGangChat,
                        receiveAllyChat,
                        receiveMemberConnectNotification,
                        receiveMemberDisconnectNotification);
        reader.endObject();
        return gangPlayerSettings;
    }
}

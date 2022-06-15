package me.Zcamt.zgangs.objects.gang.members;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.utils.ConversionUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GangMembersAdapter extends TypeAdapter<GangMembers> {
    @Override
    public void write(JsonWriter writer, GangMembers gangMembers) throws IOException {
        writer.beginObject();
        writer.name("maxMembers").value(gangMembers.getMaxMembers());
        writer.name("memberDamagePercent").value(gangMembers.getMemberDamagePercent());
        writer.name("memberList").value(ConversionUtil.uuidListToString(gangMembers.getMemberList()));
        writer.name("playerInvites").value(ConversionUtil.uuidListToString(gangMembers.getPlayerInvites()));
        writer.endObject();
    }

    @Override
    public GangMembers read(JsonReader reader) throws IOException {
        int maxMembers = 0;
        int memberDamagePercent = 100;
        List<UUID> memberList = null;
        List<UUID> playerInvites = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "maxMembers" -> maxMembers = reader.nextInt();
                case "memberDamagePercent" -> memberDamagePercent = reader.nextInt();
                case "memberList" -> memberList = ConversionUtil.uuidListFromString(reader.nextString());
                case "playerInvites" -> playerInvites = ConversionUtil.uuidListFromString(reader.nextString());
            }
        }

        GangMembers gangMembers = new GangMembers(maxMembers, memberDamagePercent, memberList, playerInvites);
        reader.endObject();
        return gangMembers;
    }
}

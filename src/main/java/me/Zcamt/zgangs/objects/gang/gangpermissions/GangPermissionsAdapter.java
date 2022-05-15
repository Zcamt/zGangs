package me.Zcamt.zgangs.objects.gang.gangpermissions;

import com.google.common.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class GangPermissionsAdapter extends TypeAdapter<GangPermissions> {
    @Override
    public void write(JsonWriter writer, GangPermissions gangPermissions) throws IOException {
        writer.beginObject();
        writer.name("permissions").value(ZGangs.GSON.toJson(gangPermissions.getPermissionsMap()));
        writer.endObject();
    }

    @Override
    public GangPermissions read(JsonReader reader) throws IOException {
        Map<GangPermission, GangRank> permissions = null;
        Type gangPermissionsMapType = new TypeToken<Map<GangPermission, GangRank>>(){}.getType();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "permissions" -> permissions = ZGangs.GSON.fromJson(reader.nextString(), gangPermissionsMapType);
            }
        }

        //Todo: Maybe check for any variables being null
        GangPermissions gangPermissions = new GangPermissions(permissions);
        return gangPermissions;
    }
}

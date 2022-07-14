package me.Zcamt.zgangs.objects.gang.permissions;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.Zcamt.zgangs.objects.gang.GangRank;

import java.io.IOException;

public class GangPermissionsAdapter extends TypeAdapter<GangPermissions> {
    @Override
    public void write(JsonWriter writer, GangPermissions gangPermissions) throws IOException {
        writer.beginObject();
        writer.name("minRankToInvitePlayers").value(gangPermissions.getMinRankToInvitePlayers().getID());
        writer.name("minRankToManageInvites").value(gangPermissions.getMinRankToInvitePlayers().getID());
        writer.name("minRankToKickMembers").value(gangPermissions.getMinRankToKickMembers().getID());
        writer.name("minRankToUseShop").value(gangPermissions.getMinRankToUseShop().getID());
        writer.name("minRankToLevelUpGang").value(gangPermissions.getMinRankToLevelUpGang().getID());
        writer.name("minRankToManageMOTD").value(gangPermissions.getMinRankToManageMOTD().getID());
        writer.name("minRankToManageAllies").value(gangPermissions.getMinRankToManageAllies().getID());
        writer.name("minRankToManageRivals").value(gangPermissions.getMinRankToManageRivals().getID());
        writer.name("minRankToManageMemberRanks").value(gangPermissions.getMinRankToManageMemberRanks().getID());
        writer.name("minRankToRenameGang").value(gangPermissions.getMinRankToRenameGang().getID());
        writer.endObject();
    }

    @Override
    public GangPermissions read(JsonReader reader) throws IOException {
        GangRank minRankToInvitePlayers = GangRank.MEMBER;
        GangRank minRankToManageInvites = GangRank.CAPTAIN;
        GangRank minRankToKickMembers = GangRank.CAPTAIN;
        GangRank minRankToUseShop = GangRank.CAPTAIN;
        GangRank minRankToLevelUpGang = GangRank.CAPTAIN;
        GangRank minRankToManageMOTD = GangRank.CO_OWNER;
        GangRank minRankToManageAllies = GangRank.CAPTAIN;
        GangRank minRankToManageRivals = GangRank.CAPTAIN;
        GangRank minRankToManageMemberRanks = GangRank.CO_OWNER;
        GangRank minRankToRenameGang = GangRank.CAPTAIN;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "minRankToInvitePlayers" -> minRankToInvitePlayers = GangRank.getRank(reader.nextInt());
                case "minRankToManageInvites" -> minRankToManageInvites = GangRank.getRank(reader.nextInt());
                case "minRankToKickMembers" -> minRankToKickMembers = GangRank.getRank(reader.nextInt());
                case "minRankToUseShop" -> minRankToUseShop = GangRank.getRank(reader.nextInt());
                case "minRankToLevelUpGang" -> minRankToLevelUpGang = GangRank.getRank(reader.nextInt());
                case "minRankToManageMOTD" -> minRankToManageMOTD = GangRank.getRank(reader.nextInt());
                case "minRankToManageAllies" -> minRankToManageAllies = GangRank.getRank(reader.nextInt());
                case "minRankToManageRivals" -> minRankToManageRivals = GangRank.getRank(reader.nextInt());
                case "minRankToManageMemberRanks" -> minRankToManageMemberRanks = GangRank.getRank(reader.nextInt());
                case "minRankToRenameGang" -> minRankToRenameGang = GangRank.getRank(reader.nextInt());
            }
        }


        GangPermissions gangPermissions = new GangPermissions(
                minRankToInvitePlayers,
                minRankToManageInvites,
                minRankToKickMembers,
                minRankToUseShop,
                minRankToLevelUpGang,
                minRankToManageMOTD,
                minRankToManageAllies,
                minRankToManageRivals,
                minRankToManageMemberRanks,
                minRankToRenameGang);
        reader.endObject();
        return gangPermissions;
    }
}

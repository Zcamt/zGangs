package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.managers.ConfigManager;
import me.Zcamt.zgangs.utils.Utilities;
import me.Zcamt.zgangs.managers.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GangRepository {

    private final Database database;

    private final String TABLE_NAME = "gangs";

    public GangRepository(Database database) {
        this.database = database;
    }

    //Todo: Update table info
    public CompletableFuture<Gang> getGangFromDB(int gangID){
        CompletableFuture<Gang> future = new CompletableFuture<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + " ID = ?";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, gangID);
                ResultSet rs = statement.executeQuery();
                String gangName = rs.getString("NAME");
                int level = rs.getInt("LEVEL");
                int kills = rs.getInt("KILLS");
                int deaths = rs.getInt("DEATHS");
                int bank = rs.getInt("BANK");
                UUID ownerUUID = UUID.fromString(rs.getString("OWNER_UUID"));
                HashMap<UUID, Integer> memberList = Utilities.deserializeGangMemberList(rs.getString("MEMBERS"));
                List<String> playerInvites = Utilities.deSerializeStringToStringList(rs.getString("PLAYER_INVITES"));
                List<Integer> alliedGangs = Utilities.deSerializeStringToIntList(rs.getString("ALLIED_GANGS"));
                List<Integer> alliedGangInvitesIncoming = Utilities.deSerializeStringToIntList(rs.getString("ALLIED_GANGS_INCOMING"));
                List<Integer> alliedGangInvitesOutgoing = Utilities.deSerializeStringToIntList(rs.getString("ALLIED_GANGS_OUTGOING"));
                List<Integer> rivalGangs = Utilities.deSerializeStringToIntList(rs.getString("RIVAL_GANGS"));
                List<Integer> rivalGangsAgainst = Utilities.deSerializeStringToIntList(rs.getString("RIVAL_GANGS_AGAINST"));
                Gang gang = new Gang(gangID, gangName,
                        level, kills, deaths, bank,
                        ownerUUID,
                        memberList,
                        playerInvites, alliedGangs, alliedGangInvitesIncoming, alliedGangInvitesOutgoing, rivalGangs, rivalGangsAgainst,
                        this);
                rs.close();
                future.complete(gang);
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't get gang with ID '" + gangID + "' " + e);
            }
        });
        return future;
    }

    public int getNextGangID() throws SQLException {
        String query = "SELECT AUTOINC " +
                "FROM information_schema.INNODB_SYS_TABLESTATS " +
                "WHERE NAME = \"" + ConfigManager.database + "/gangs\"";
        try (Connection connection = database.getHikari().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt("AUTOINC");
        }
    }

    //Todo: Update table

    public void insertNewGangIntoDB(Gang gang) {
        String query = "INSERT INTO " + TABLE_NAME +
                " (" +
                "ID," +
                "NAME, " +
                "LEVEL, " +
                "KILLS, " +
                "DEATHS, " +
                "BANK, " +
                "OWNER_UUID, " +
                "MEMBERS, " +
                "PLAYER_INVITES, " +
                "ALLIED_GANGS, " +
                "ALLIED_GANGS_INCOMING, " +
                "ALLIED_GANGS_OUTGOING, " +
                "RIVAL_GANGS, " +
                "RIVAL_GANGS_AGAINST, " +
                "CREATED_DATE" +
                ") " +
                "VALUES(" +
                "DEFAULT," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "DEFAULT" +
                ")";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, gang.getName());
                statement.setInt(2, gang.getLevel());
                statement.setInt(3, gang.getKills());
                statement.setInt(4, gang.getDeaths());
                statement.setInt(5, gang.getBank());
                statement.setString(6, gang.getOwnerUUID().toString());
                statement.setString(7, gang.getSerializedMemberList());
                statement.setString(8, gang.getSerializedPlayerInvites());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't insert new gang with ID '" + gang.getId() + "' into the database" + e);
            }
        });

    }

    public CompletableFuture<Boolean> gangIdExists(int gangID){
        CompletableFuture<Boolean> booleanCompletableFuture = new CompletableFuture<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID = ?";
        try (Connection connection = database.getHikari().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gangID);
            ResultSet rs = statement.executeQuery();
            booleanCompletableFuture.complete(rs.next());
            return booleanCompletableFuture;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if gang-id '" + gangID + "' exists " + e);
        }
    }

    public CompletableFuture<Boolean> gangNameExists(String gangName){
        CompletableFuture<Boolean> booleanCompletableFuture = new CompletableFuture<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE NAME = ?";
        try (Connection connection = database.getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, gangName);
            ResultSet rs = statement.executeQuery();
            booleanCompletableFuture.complete(rs.next());
            return booleanCompletableFuture;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if gang-name '" + gangName + "' exists " + e);
        }
    }


    public void updateGangInDB(Gang gang){
        String query = "UPDATE " + TABLE_NAME + " SET " +
                "NAME = ?, " +
                "LEVEL = ?, " +
                "KILLS = ?, " +
                "DEATHS = ?, " +
                "BANK = ?, " +
                "OWNER_UUID = ?, " +
                "MEMBERS = ?, " +
                "PLAYER_INVITES = ?, " +
                "ALLIED_GANGS = ?, " +
                "ALLIED_GANGS_INCOMING = ?, " +
                "ALLIED_GANGS_OUTGOING = ?, " +
                "RIVAL_GANGS = ?, " +
                "RIVAL_GANGS_AGAINST = ?" +
                "" +
                "WHERE ID = ?";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, gang.getName());
                statement.setInt(2, gang.getLevel());
                statement.setInt(3, gang.getKills());
                statement.setInt(4, gang.getDeaths());
                statement.setInt(5, gang.getBank());
                statement.setString(6, gang.getOwnerUUID().toString());
                statement.setString(7, gang.getSerializedMemberList());
                statement.setString(8, gang.getSerializedPlayerInvites());
                statement.setString(9, gang.getSerializedAllyList());
                statement.setString(10, gang.getSerializedAllyInvitesIncomingList());
                statement.setString(11, gang.getSerializedAllyInvitesOutgoingList());
                statement.setString(12, gang.getSerializedRivalList());
                statement.setString(13, gang.getSerializedRivalsAgainstList());
                statement.setInt(14, gang.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update information for gang with ID '" + gang.getId() + "' " + e);
            }
        });
    }

}

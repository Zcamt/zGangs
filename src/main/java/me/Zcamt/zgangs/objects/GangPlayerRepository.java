package me.Zcamt.zgangs.objects;

import me.Zcamt.zgangs.utils.Utilities;
import me.Zcamt.zgangs.managers.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GangPlayerRepository {

    private final Database database;

    private final String TABLE_NAME = "gang_players";

    public GangPlayerRepository(Database database) {
        this.database = database;
    }

    public CompletableFuture<GangPlayer> getGangPlayerFromDB(UUID uuid){
        CompletableFuture<GangPlayer> future = new CompletableFuture<>();
        String query = "SELECT * FROM " + database + ".gang_players WHERE " + " UUID = ?";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid.toString());
                ResultSet rs = statement.executeQuery();
                int gangID = rs.getInt("GANG_ID");
                int gangRank = rs.getInt("GANG_RANK");
                List<Integer> gangInvites = Utilities.deSerializeStringToIntList(rs.getString("GANG_INVITES"));
                GangPlayer gangPlayer = new GangPlayer(uuid, gangID, gangRank, gangInvites, this);
                rs.close();
                future.complete(gangPlayer);
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't get gang-player with UUID '" + uuid + "' " + e);
            }
        });
        return future;
    }

    public void insertNewGangPlayerIntoDB(GangPlayer gangPlayer){
        String query = "INSERT INTO " + TABLE_NAME + ".gang_players " +
                "(" +
                "UUID," +
                "GANG_ID, " +
                "GANG_RANK, " +
                "GANG_INVITES" +
                ")" +
                " " +
                "VALUES(" +
                "?," +
                "?," +
                "?," +
                "?" +
                ")";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, gangPlayer.getUUID().toString());
                statement.setInt(2, gangPlayer.getGangID());
                statement.setInt(3, gangPlayer.getGangRank());
                statement.setString(4, gangPlayer.getSerializedGangInvitesList());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't insert new gang player with UUID '" + gangPlayer.getUUID() + "' into the database" + e);
            }
        });
    }

    public CompletableFuture<Boolean> uuidExistsInDB(UUID uuid){
        CompletableFuture<Boolean> booleanCompletableFuture = new CompletableFuture<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE UUID = ?";
        try (Connection connection = database.getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            booleanCompletableFuture.complete(rs.next());
            return booleanCompletableFuture;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if UUID '" + uuid + "' exists in database " + e);
        }
    }

    public void updateGangPlayerInDB(GangPlayer gangPlayer){
        String query = "UPDATE " + TABLE_NAME + ".gang_players SET " +
                "GANG_ID = ?, " +
                "GANG_RANK = ?, " +
                "GANG_INVITES = ?" +
                "" +
                "WHERE UUID = ?";
        database.executeInPool(() -> {
            try (Connection connection = database.getHikari().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, gangPlayer.getGangID());
                statement.setInt(2, gangPlayer.getGangRank());
                statement.setString(3, gangPlayer.getSerializedGangInvitesList());
                statement.setString(4, gangPlayer.getUUID().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update information for gang player with UUID '" + gangPlayer.getUUID() + "' " + e);
            }
        });
    }
}

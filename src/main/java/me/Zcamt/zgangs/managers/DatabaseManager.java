package me.Zcamt.zgangs.managers;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.helpers.Utilities;
import me.Zcamt.zgangs.objects.CallbackGang;
import me.Zcamt.zgangs.objects.CallbackGangPlayer;
import me.Zcamt.zgangs.objects.Gang;
import me.Zcamt.zgangs.objects.GangPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private ZGangs plugin;
    private static Connection connection;
    private String host, database, username, password;
    private int port;

    public String getDatabase() {
        return database;
    }

    public DatabaseManager(ZGangs plugin) {
        this.plugin = plugin;
    }

    public void createDatabases() {
        String query = "CREATE TABLE IF NOT EXISTS " + ConfigManager.database + ".gangs " +
                "(" +
                "ID int(11) AUTO_INCREMENT, " +
                "NAME varchar(32) NOT NULL, " +
                "LEVEL int(11) DEFAULT 1 NOT NULL, " +
                "KILLS int(11) DEFAULT 0 NOT NULL, " +
                "DEATHS int(11) DEFAULT 0 NOT NULL, " +
                "BANK int(11) DEFAULT 0 NOT NULL, " +
                "OWNER_UUID varchar(36) NOT NULL, " +
                "MEMBERS varchar(255) NOT NULL, " +
                "CREATED_DATE TIMESTAMP, " +
                "PRIMARY KEY (ID)" +
                ")";

        String query2 = "CREATE TABLE IF NOT EXISTS " + ConfigManager.database + ".gang_players " +
                "(" +
                "UUID varchar(36) NOT NULL, " +
                "GANG_ID int(11) DEFAULT 0 NOT NULL, " +
                "GANG_RANK int(11) DEFAULT 0 NOT NULL, " +
                "PRIMARY KEY (UUID)" +
                ")";
        PreparedStatement ps = prepareStatement(query);
        PreparedStatement ps2 = prepareStatement(query2);
        asyncThread(() -> {
                    try {
                        ps.executeUpdate();
                        ps2.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void getGangFromDB(int gangID, CallbackGang callbackGang){
        asyncThread(new BukkitRunnable() {
            @Override
            public void run() {
                String query = "SELECT * FROM " + database + ".gangs WHERE " + " ID = " + gangID;
                PreparedStatement ps = prepareStatement(query);
                try {
                    ResultSet rs = ps.executeQuery();
                    String gangName = rs.getString("NAME");
                    int level = rs.getInt("LEVEL");
                    int kills = rs.getInt("KILLS");
                    int deaths = rs.getInt("DEATHS");
                    int bank = rs.getInt("BANK");
                    UUID ownerUUID = UUID.fromString(rs.getString("OWNER_UUID"));
                    final HashMap<UUID, Integer> memberList = Utilities.deserializeGangMemberList(rs.getString("MEMBERS"));
                    Gang gang = new Gang(gangID, gangName, level, kills, deaths, bank, ownerUUID, memberList);
                    ps.close();
                    rs.close();
                    callbackGang.setGang(gang);
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void getGangPlayerFromDB(Player player, CallbackGangPlayer callbackGangPlayer){
        asyncThread(new BukkitRunnable() {
            @Override
            public void run() {
                String query = "SELECT * FROM " + database + ".gang_players WHERE " + " UUID = ?";
                PreparedStatement ps = prepareStatement(query);
                try {
                    ps.setString(1, player.getUniqueId().toString());
                    ResultSet rs = ps.executeQuery();
                    String UUID = rs.getString("UUID");
                    int gangID = rs.getInt("GANG_ID");
                    int gangRank = rs.getInt("GANG_RANK");
                    List<Integer> gangInvites = Utilities.deSerializeStringToIntList(rs.getString("GANG_INVITES"));
                    GangPlayer gangPlayer = new GangPlayer(player, gangID, gangRank, gangInvites);
                    ps.close();
                    rs.close();
                    callbackGangPlayer.setGangPlayer(gangPlayer);
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public int getNextGangID() throws SQLException {
        String query = "SELECT AUTOINC " +
                "FROM information_schema.INNODB_SYS_TABLESTATS " +
                "WHERE NAME = \"" + database + "/gangs\"";
        PreparedStatement ps = prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("AUTOINC");
    }

    public PreparedStatement prepareStatement(String query){
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return ps;
    }

    public void asyncThread(Runnable runnable){
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void setup() throws SQLException {
        host = "" + ConfigManager.host;
        port = ConfigManager.port;
        database = "" + ConfigManager.database;
        username = "" + ConfigManager.username;
        password = "" + ConfigManager.password;
        openConnection();
        createDatabases();
    }

    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()){
            return;
        }

        connection = DriverManager.getConnection("jdbc:mysql://" +
                        this.host + ":"
                        + this.port + "/"
                        + this.database +"?autoReconnect=true",
                this.username,
                this.password);
    }

}

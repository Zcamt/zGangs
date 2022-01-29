package me.Zcamt.zgangs.managers;


import com.zaxxer.hikari.HikariDataSource;
import me.Zcamt.zgangs.objects.GangPlayerRepository;
import me.Zcamt.zgangs.objects.GangRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database {

    private final String HOST = ConfigManager.host,
            DATABASE_NAME = ConfigManager.database,
            USERNAME = ConfigManager.username,
            PASSWORD = ConfigManager.password;
    private final int PORT = ConfigManager.port;
    private HikariDataSource hikari;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private GangRepository gangRepository;
    private GangPlayerRepository gangPlayerRepository;

    public void setup(){
        connect();
        gangRepository = new GangRepository(this);
        gangPlayerRepository = new GangPlayerRepository(this);
        createTables();
    }

    public void connect() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", this.HOST);
        hikari.addDataSourceProperty("port", this.PORT);
        hikari.addDataSourceProperty("databaseName", this.DATABASE_NAME);
        hikari.addDataSourceProperty("user", this.USERNAME);
        hikari.addDataSourceProperty("password", this.PASSWORD);
    }

    public boolean isConnected(){return hikari != null;}

    public void disconnect(){
        if(isConnected()){
            hikari.close();
        }
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void createTables() {
        String query = "CREATE TABLE IF NOT EXISTS gangs " +
                "(" +
                "ID int(11) AUTO_INCREMENT, " +
                "NAME varchar(32) NOT NULL, " +
                "LEVEL int(11) DEFAULT 1 NOT NULL, " +
                "KILLS int(11) DEFAULT 0 NOT NULL, " +
                "DEATHS int(11) DEFAULT 0 NOT NULL, " +
                "BANK int(11) DEFAULT 0 NOT NULL, " +
                "OWNER_UUID varchar(36) NOT NULL, " +
                "MEMBERS varchar(255) NOT NULL, " +
                "PLAYER_INVITES varchar(255) NOT NULL, " +
                "CREATED_DATE TIMESTAMP, " +
                "PRIMARY KEY (ID)" +
                ")";
        executeInPool(() -> {
            try (Connection connection = hikari.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't create gangs table" + e);
            }
        });


        String query1 = "CREATE TABLE IF NOT EXISTS gang_players " +
                "(" +
                "UUID varchar(36) NOT NULL, " +
                "GANG_ID int(11) DEFAULT 0 NOT NULL, " +
                "GANG_RANK int(11) DEFAULT 0 NOT NULL," +
                "GANG_INVITES varchar(255) NOT NULL, " +
                "PRIMARY KEY (UUID)" +
                ")";
        executeInPool(() -> {
            try (Connection connection = hikari.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query1)) {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Couldn't create gang_players table" + e);
            }
        });

    }

    public void executeInPool(Runnable runnable){
        threadPool.execute(runnable);
    }

    public GangRepository getGangRepository() {
        return gangRepository;
    }
    public GangPlayerRepository getGangPlayerRepository() {
        return gangPlayerRepository;
    }
}

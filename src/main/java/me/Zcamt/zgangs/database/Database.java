package me.Zcamt.zgangs.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> gangCollection;
    private final MongoCollection<Document> gangPlayerCollection;

    //Todo: Gør alt database stuffs ASYNC
    public Database() {
        this.client = new MongoClient(new MongoClientURI("mongodb://localhost:30124/")); //Todo: URI skal nok opdateres
        this.database = client.getDatabase("nprison");
        this.gangCollection = database.getCollection("gangs");
        this.gangPlayerCollection = database.getCollection("gang_players");
    }


    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getGangCollection() {
        return gangCollection;
    }

    public MongoCollection<Document> getGangPlayerCollection() {
        return gangPlayerCollection;
    }
}

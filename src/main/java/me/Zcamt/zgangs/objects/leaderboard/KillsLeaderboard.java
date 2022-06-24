package me.Zcamt.zgangs.objects.leaderboard;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillsLeaderboard extends Leaderboard {
    private final List<GangLeaderboardEntry> leaderboard = new ArrayList<>();

    @Override
    public void update() {
        ZGangs.getThreadPool().execute(() -> {
            List<GangLeaderboardEntry> leaderboard = new ArrayList<>();
            FindIterable<Document> gangs = ZGangs.getDatabase().getGangCollection().find().sort(new BasicDBObject("kills", -1)).limit(10);
            try (MongoCursor<Document> gangIterator = gangs.iterator()) {
                while (gangIterator.hasNext()) {
                    Document gangDocument = gangIterator.next();
                    Gang gang = ZGangs.GSON.fromJson(gangDocument.toJson(), Gang.class);
                    GangLeaderboardEntry gangEntry = new GangLeaderboardEntry(
                            gang.getUUID(),
                            gang.getOwnerUUID(),
                            LeaderboardType.KILLS,
                            gang.getGangStats().getKills()
                    );
                    leaderboard.add(gangEntry);
                }
            }

            leaderboard.removeIf(entry -> entry.getLeaderboardType() != LeaderboardType.KILLS);
            setLastUpdatedEpoch(System.currentTimeMillis());
            leaderboard.sort(Comparator.comparing(GangLeaderboardEntry::getAmount));
            this.leaderboard.clear();
            this.leaderboard.addAll(leaderboard);
        });
    }

    @Override
    public List<GangLeaderboardEntry> getLeaderBoard() {
        return leaderboard;
    }



}

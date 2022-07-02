package me.Zcamt.zgangs.objects.leaderboard.leaderboards;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.leaderboard.GangLeaderboardEntry;
import me.Zcamt.zgangs.objects.leaderboard.Leaderboard;
import me.Zcamt.zgangs.objects.leaderboard.LeaderboardType;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeathLeaderboard extends Leaderboard {

    private final List<GangLeaderboardEntry> leaderboard = new ArrayList<>();

    @Override
    public void update() {
        ZGangs.getThreadPool().execute(() -> {
            List<GangLeaderboardEntry> leaderboard = new ArrayList<>();
            FindIterable<Document> gangs = ZGangs.getDatabase().getGangCollection().find()
                    .sort(new BasicDBObject("gangStats.deaths", -1)).limit(21);
            try (MongoCursor<Document> gangIterator = gangs.iterator()) {
                while (gangIterator.hasNext()) {
                    Document gangDocument = gangIterator.next();
                    Gang gang = ZGangs.GSON.fromJson(gangDocument.toJson(), Gang.class);
                    GangLeaderboardEntry gangEntry = new GangLeaderboardEntry(
                            gang.getUUID(),
                            gang.getOwnerUUID(),
                            LeaderboardType.DEATHS,
                            gang.getGangStats().getDeaths()
                    );
                    leaderboard.add(gangEntry);
                }
            }

            leaderboard.removeIf(entry -> entry.getLeaderboardType() != LeaderboardType.DEATHS);
            setLastUpdatedEpoch(System.currentTimeMillis());
            leaderboard.sort(Comparator.comparing(GangLeaderboardEntry::getAmount));
            this.leaderboard.clear();
            this.leaderboard.addAll(leaderboard);
        });
    }

    @Override
    public List<GangLeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }
}

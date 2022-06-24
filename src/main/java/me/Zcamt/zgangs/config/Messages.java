package me.Zcamt.zgangs.config;

import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    private static final File messagesFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "messages.yml");
    public static YamlConfiguration config;

    private static String invalidUsage;
    private static String neededGangRank = "&cFejl: Du skal være &4{rank} &ci din bande for at gøre dette";
    public static String noPerm;
    public static String notInGang;
    public static String cantWhileInGang = "&cFejl: Det kan du ikke gøre når du er medlem af en bande";
    public static String invalidPlayer;
    public static String invalidGang;
    public static String illegalGangName;
    public static String unknownCommand;
    public static String invalidInput;
    public static String noEnoughMoney = "&cFejl: Det har du ikke penge nok til";
    public static String unexpectedError = "&cFejl: Noget gik galt, kontakt venligst en Admin";
    private static String inviteSentToPlayer = "&aInvitation sendt til {player}";
    private static String inviteReceivedFrom = "&aInvitation modtaget fra {gang}, accepter den via /bk accept {gang} eller via menuen";
    private static String bankDeposit = "&a&lDu har sat &c&l{amount} &a&lind i bandebanken";
    private static String playerJoinedGang = "&a&l{player} er nu en del af banden";
    public static String cantLeaveAsOwner = "&cFejl: Du kan ikke forlade en bande du ejer";
    public static String cantDeleteGangWhileHasMembers = "&cFejl: Du skal være alene i din bande for at kunne slette den";
    private static String playerLeftGang = "&a&l{player} har forladt banden!";
    private static String allyInviteIncoming = "&a&lDin bande har modtaget en invitation om at blive allierede fra {gang}";
    private static String allyInviteOutgoing = "&a&lDin bande har send en invitation om at blive allierede til {gang}";
    private static String newRival = "&a&lDin bande har lige gjort {gang} til en rival";
    private static String newRivalAgainst = "&a&l{gang} har lige gjort din bande til en rival";

    public static String invalidUsage(String usage) {
        return invalidUsage.replace("{usage}", usage);
    }

    public static String neededGangRank(String neededRank) {
        return neededGangRank.replace("{rank}", neededRank);
    }

    public static String inviteSentToPlayer(String playerName) {
        return inviteSentToPlayer.replace("{player}", playerName);
    }

    public static String inviteReceivedFrom(String gangName) {
        return inviteReceivedFrom.replace("{gang}", gangName);
    }

    public static String bankDeposit(int amount) {
        return bankDeposit.replace("{amount}", String.valueOf(amount));
    }

    public static String playerJoinedGang(String playerName) {
        return playerJoinedGang.replace("{player}", playerName);
    }

    public static String playerLeftGang(String playerName) {
        return playerLeftGang.replace("{player}", playerName);
    }

    public static String allyInviteIncoming(String gangName) {
        return allyInviteIncoming.replace("{gang}", gangName);
    }

    public static String allyInviteOutgoing(String gangName) {
        return allyInviteOutgoing.replace("{gang}", gangName);
    }

    public static String newRival(String gangName) {
        return newRival.replace("{gang}", gangName);
    }

    public static String newRivalAgainst(String gangName) {
        return newRivalAgainst.replace("{gang}", gangName);
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(messagesFile);

        invalidUsage = ChatUtil.CC(config.getString("invalid-usage"));
        noPerm = ChatUtil.CC(config.getString("no-perm"));
        notInGang = ChatUtil.CC(config.getString("not-in-gang"));
        invalidPlayer = ChatUtil.CC(config.getString("invalid-player"));
        invalidGang = ChatUtil.CC(config.getString("invalid-gang"));
        illegalGangName = ChatUtil.CC(config.getString("illegal-gangname"));
        unknownCommand = ChatUtil.CC(config.getString("unknown-command"));
        invalidInput = ChatUtil.CC(config.getString("invalid-input"));
    }

}

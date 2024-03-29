package me.Zcamt.zgangs.config;

import me.Zcamt.zgangs.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    private static final File messagesFile = new File(Bukkit.getPluginManager().getPlugin("zGangs").getDataFolder(), "messages.yml");
    public static YamlConfiguration config;

    private static String invalidUsage;
    private static String neededGangRank = "&cFejl: Du skal være mindst &4{rank} &ci din bande for at gøre dette";
    public static String noPerm;
    public static String notInGang;
    public static String cantWhileInGang = "&cFejl: Det kan du ikke gøre når du er medlem af en bande";
    public static String invalidPlayer;
    public static String invalidGang;
    public static String illegalGangName;
    public static String unknownCommand;
    public static String invalidInput;
    public static String notEnoughMoney = "&cFejl: Det har du ikke penge nok til";
    public static String notEnoughGangMoney = "&cFejl: Det har din bande ikke penge nok til";
    public static String unexpectedError = "&cFejl: Noget gik galt, kontakt venligst en Admin";
    public static String levelRequirementsNotMet = "&cFejl: Din bande har ikke opnået alle krav endnu";
    private static String gangReachedNewLevel = "&aDin bande er nu i level {level}";
    private static String inviteSentToPlayer = "&aDin bande har sendt en invitation til {player}";
    private static String inviteReceivedFrom = "&aInvitation modtaget fra {gang}, accepter den via /bk accept {gang} eller via /b";
    private static String bankDeposit = "&a&lDu har sat &c&l{amount} &a&lind i bandebanken";
    private static String playerJoinedGang = "&a&l{player} er nu en del af banden";
    public static String cantLeaveAsOwner = "&cFejl: Du kan ikke forlade en bande du ejer";
    public static String cantDeleteGangWhileHasMembers = "&cFejl: Du skal være alene i din bande for at kunne slette den";
    private static String playerLeftGang = "&a&l{player} har forladt banden!";
    private static String playerKickedFromGang = "&a&l{player} er blevet smidt ud af banden!";
    private static String allyInviteIncoming = "&a&lDin bande har modtaget en invitation om at blive allierede fra {gang}";
    private static String allyInviteOutgoing = "&a&lDin bande har sendt en invitation om at blive allierede til {gang}";
    private static String newAlly = "&a&lDin bande er nu allierede med {gang}";
    private static String noLongerAlly = "&al&lDin bande er ikke længere allierede med {gang}";
    private static String newRival = "&a&lDin bande har lige gjort {gang} til en rival";
    private static String newRivalAgainst = "&a&l{gang} har lige gjort din bande til en rival";
    private static String noLongerRival = "&al&lDin bande er ikke længere rivaler med {gang}";
    private static String noLongerRivalAgainst = "&al&lDin bande er ikke længere markeret som rival af {gang}";
    private static String memberConnected = "&a&l{player} &aer nu online";
    private static String memberDisconnected = "&c&l{player} &cer nu offline";
    private static String allyConnected = "&a&l{player} &aer nu online";
    private static String allyDisconnected = "&c&l{player} &cer nu offline";

    public static String invalidUsage(String usage) {
        return invalidUsage.replace("{usage}", usage);
    }

    public static String neededGangRank(String neededRank) {
        return neededGangRank.replace("{rank}", neededRank);
    }

    public static String gangReachedNewLevel(int level) {
        return gangReachedNewLevel.replace("{level}", String.valueOf(level));
    }

    public static String inviteSentToPlayer(String playerName) {
        return inviteSentToPlayer.replace("{player}", playerName);
    }

    public static String inviteReceivedFrom(String gangName) {
        return inviteReceivedFrom.replace("{gang}", gangName);
    }

    public static String bankDeposit(String amount) {
        return bankDeposit.replace("{amount}", String.valueOf(amount));
    }

    public static String playerJoinedGang(String playerName) {
        return playerJoinedGang.replace("{player}", playerName);
    }

    public static String playerLeftGang(String playerName) {
        return playerLeftGang.replace("{player}", playerName);
    }

    public static String playerKickedFromGang(String playerName) {
        return playerKickedFromGang.replace("{player}", playerName);
    }

    public static String allyInviteIncoming(String gangName) {
        return allyInviteIncoming.replace("{gang}", gangName);
    }

    public static String allyInviteOutgoing(String gangName) {
        return allyInviteOutgoing.replace("{gang}", gangName);
    }

    public static String noLongerAlly(String gangName) {
        return noLongerAlly.replace("{gang}", gangName);
    }

    public static String newAlly(String gangName) {
        return newAlly.replace("{gang}", gangName);
    }

    public static String newRival(String gangName) {
        return newRival.replace("{gang}", gangName);
    }

    public static String newRivalAgainst(String gangName) {
        return newRivalAgainst.replace("{gang}", gangName);
    }

    public static String noLongerRival(String gangName) {
        return noLongerRival.replace("{gang}", gangName);
    }

    public static String noLongerRivalAgainst(String gangName) {
        return noLongerRivalAgainst.replace("{gang}", gangName);
    }

    public static String memberConnected(String memberName) {
        return memberConnected.replace("{player}", memberName);
    }

    public static String memberDisconnected(String memberName) {
        return memberDisconnected.replace("{player}", memberName);
    }

    public static String allyConnected(String allyName) {
        return allyConnected.replace("{player}", allyName);
    }

    public static String allyDisconnected(String allyName) {
        return allyDisconnected.replace("{player}", allyName);
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

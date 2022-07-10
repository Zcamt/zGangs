package me.Zcamt.zgangs.guis;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.chatinput.ChatInputManager;
import me.Zcamt.zgangs.config.Config;
import me.Zcamt.zgangs.config.Messages;
import me.Zcamt.zgangs.objects.gang.Gang;
import me.Zcamt.zgangs.objects.gang.GangManager;
import me.Zcamt.zgangs.objects.gang.GangRank;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayer;
import me.Zcamt.zgangs.objects.gangplayer.GangPlayerManager;
import me.Zcamt.zgangs.utils.ChatUtil;
import me.Zcamt.zgangs.utils.ItemCreator;
import me.Zcamt.zgangs.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GangSettingsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();
    private final Economy economy = ZGangs.getEconomy();

    public GangSettingsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBande indstillinger"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        //Rename
        setItem(10, new ItemCreator(Material.NAME_TAG).setName("&aSkift navn").make());

        //Manage members
        setItem(12, new ItemCreator(Material.PLAYER_HEAD).setName("&aHåndter medlemmer").make());

        //Manage MOTD
        setItem(14, new ItemCreator(Material.BOOK).setName("&aHåndter MOTD").make());

        //Manage allies
        setItem(16, new ItemCreator(Material.GREEN_BANNER).setName("&aHåndter allierede").make());

        //Bank-deposit
        setItem(28, new ItemCreator(Material.GOLD_NUGGET).setName("&aIndsæt penge").make());

        //Manage gang-rank permissions
        setItem(30, new ItemCreator(Material.GOLDEN_SWORD).setName("&aHåndter adgang for bande-roller").make());

        //Delete gang
        setItem(32, new ItemCreator(Material.RED_DYE).setName("&aSlet bande").make());

        //Manage rivals
        setItem(34, new ItemCreator(Material.RED_BANNER).setName("&aHåndter rivaler").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        switch (clickedItem.getType()) {
            case BARRIER -> {
                MainGui mainGui = new MainGui(player, gang);
                mainGui.openTo(player);
            }
            //Rename
            case NAME_TAG -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToRenameGang();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                chatInputManager.newStringInput(player, name -> {
                    if(!Utils.isNameValid(name)) {
                        ChatUtil.sendMessage(player, Messages.illegalGangName);
                        return;
                    }
                    if(!gang.hasMoney(Config.gangRenameCost)) {
                        ChatUtil.sendMessage(player, Messages.notEnoughGangMoney);
                        return;
                    }
                    gang.setName(name);
                    gang.sendMessageToOnlineMembers(Config.prefix + " &c&l" + player.getName() + " &ahar lige ændret bandens navn til &c" + name);
                });
                ChatUtil.sendMessage(player, Config.prefix + " &a&lSkriv det nye navn du ønsker for din bande i chatten! " +
                        "- Hvis du ønsker at afbryde processen tast '&c&l-afbryd&a&l'");
                player.closeInventory();
            }
            //Deposit
            case GOLD_NUGGET -> {
                chatInputManager.newIntInput(player, amount -> {
                    if(!economy.has(player, amount)) {
                        ChatUtil.sendMessage(player, Messages.notEnoughMoney);
                        return;
                    }

                    EconomyResponse response = economy.withdrawPlayer(player, amount);
                    if(!response.transactionSuccess()) {
                        ChatUtil.sendMessage(player, response.errorMessage);
                        return;
                    }
                    gang.setBank(gang.getBank() + amount);
                    ChatUtil.sendMessage(player, Config.prefix + " " + Messages.bankDeposit(Utils.formattedCurrency(amount)));
                });
                ChatUtil.sendMessage(player, Config.prefix + " &a&lSkriv den mængde du ønsker at sætte ind i bandebanken! " +
                        "- Hvis du ønsker at afbryde processen tast '&c&l-afbryd&a&l'");
                player.closeInventory();
            }
            //Delete
            case RED_DYE -> {
                GangRank requiredRank = GangRank.OWNER;
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                if(gangManager.deleteGang(gang)) {
                    ChatUtil.sendMessage(player, Config.prefix + " &a&lDin bande er nu slettet");
                    player.closeInventory();
                } else {
                    ChatUtil.sendMessage(player, Config.prefix + " &cFejl: Din bande kunne ikke slettes, vær' sikker på at du er alene i den!");
                }
            }
            //Members
            case PLAYER_HEAD -> {
                GangRank minimumRankRequired;
                GangRank requiredRankToMangeMemberRanks = gang.getGangPermissions().getMinRankToManageMemberRanks();
                GangRank requiredRankToKickMembers = gang.getGangPermissions().getMinRankToKickMembers();
                if(requiredRankToMangeMemberRanks.isHigherOrEqualThan(requiredRankToKickMembers)) {
                    minimumRankRequired = requiredRankToKickMembers;
                } else {
                    minimumRankRequired = requiredRankToMangeMemberRanks;
                }
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(minimumRankRequired)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(minimumRankRequired.getName()));
                    return;
                }

                //Open Menu
            }
            //MOTD
            case BOOK -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageMOTD();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                //Open Menu
            }
            //Allies
            case GREEN_BANNER -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageAllies();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                //Open Menu
            }
            //Rivals
            case RED_BANNER -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageRivals();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                //Open Menu
            }
            //Rank permissions
            case GOLDEN_SWORD -> {
                GangRank requiredRank = GangRank.OWNER;
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                //Open Menu
            }
        }
    }

}

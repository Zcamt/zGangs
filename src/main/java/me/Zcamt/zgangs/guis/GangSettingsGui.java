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

import java.nio.file.attribute.UserPrincipalNotFoundException;

public class GangSettingsGui extends GUI {

    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangManager gangManager = ZGangs.getGangManager();
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();
    private final Economy economy = ZGangs.getEconomy();

    protected GangSettingsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBande indstillinger"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());

        //Rename
        setItem(10, new ItemCreator(Material.NAME_TAG).setName("&aSkift navn")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Manage members
        setItem(12, new ItemCreator(Material.PLAYER_HEAD).setName("&aHåndter medlemmer")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Manage MOTD
        setItem(14, new ItemCreator(Material.BOOK).setName("&aHåndter MOTD")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Manage allies
        setItem(16, new ItemCreator(Material.GREEN_BANNER).setName("&aHåndter allierede")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Bank-deposit
        setItem(28, new ItemCreator(Material.GOLD_NUGGET).setName("&aIndsæt penge")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Manage gang-rank permissions
        setItem(30, new ItemCreator(Material.GOLDEN_SWORD).setName("&aHåndter adgang for bande-roller")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Delete gang
        setItem(32, new ItemCreator(Material.RED_DYE).setName("&aSlet bande")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());

        //Manage rivals
        setItem(34, new ItemCreator(Material.RED_BANNER).setName("&aHåndter rivaler")
                .addLore("&7Klik her for at:",
                        "&6- &fNOGET")
                .make());
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
            //Rank permissions
            case GOLDEN_SWORD -> {
                GangRank requiredRank = GangRank.OWNER;
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            //MOTD
            case BOOK -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageMOTD();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                ManageMOTDGui manageMOTDGui = new ManageMOTDGui(player, gang);
                manageMOTDGui.openTo(player);
            }

            //Allies
            case GREEN_BANNER -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageAllies();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                ManageAlliesGui manageAlliesGui = new ManageAlliesGui(player, gang);
                manageAlliesGui.openTo(player);
            }
            //Rivals MANGLER
            case RED_BANNER -> {
                GangRank requiredRank = gang.getGangPermissions().getMinRankToManageRivals();
                GangRank playerRank = gangPlayer.getGangRank();

                if(!playerRank.isHigherOrEqualThan(requiredRank)) {
                    ChatUtil.sendMessage(player, Messages.neededGangRank(requiredRank.getName()));
                    return;
                }

                //Open Menu
            }
            //Members MANGLER
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
        }
    }
}

class ManagePermissionsGui extends GUI {
    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();

    protected ManagePermissionsGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBanderollers adgang"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        //minRankToInvitePlayers 10
        GangRank requiredRankToInvitePlayer = gang.getGangPermissions().getMinRankToInvitePlayers();
        setItem(10, new ItemCreator(Material.NETHER_STAR)
                .setName("&aInviter spillere til banden")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToInvitePlayer.getFormatedNamed()),
                        "&6- &fInvitere andre til banden")
                .make());
        //minRankToManageInvites 19
        GangRank requiredRankToManageInvites = gang.getGangPermissions().getMinRankToManageInvites();
        setItem(19, new ItemCreator(Material.PAPER)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToManageInvites.getFormatedNamed()),
                        "&6- &fInvitere andre til banden",
                        "&6- &fHåndtere udgående invitationer")
                .make());

        //minRankToKickMembers 12
        GangRank requiredRankToKick = gang.getGangPermissions().getMinRankToKickMembers();
        setItem(12, new ItemCreator(Material.SKELETON_SKULL)
                .setName("&aKick medlemmer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToKick.getFormatedNamed()),
                        "&6- &fFjerne medlemmer fra banden")
                .make());
        //minRankToManageMemberRanks 21
        GangRank requiredRankToManageMemberRanks = gang.getGangPermissions().getMinRankToManageMemberRanks();
        setItem(21, new ItemCreator(Material.PLAYER_HEAD)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToManageMemberRanks.getFormatedNamed()),
                        "&6- &fHåndtere nuværende medlemmer i banden")
                .make());
        //minRankToUseShop 30
        GangRank requiredRankToUseShop = gang.getGangPermissions().getMinRankToManageMemberRanks();
        setItem(30, new ItemCreator(Material.GOLD_INGOT)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToUseShop.getFormatedNamed()),
                        "&6- &fBruge bandeshoppen")
                .make());

        //minRankToRankUp 14
        GangRank requiredRankToRankUp = gang.getGangPermissions().getMinRankToLevelUpGang();
        setItem(14, new ItemCreator(Material.EXPERIENCE_BOTTLE)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToRankUp.getFormatedNamed()),
                        "&6- &fLevel banden op")
                .make());
        //minRankToManageMOTD 23
        GangRank requiredRankToManageMOTD = gang.getGangPermissions().getMinRankToManageMOTD();
        setItem(23, new ItemCreator(Material.BOOK)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToManageMOTD.getFormatedNamed()),
                        "&6- &fHåndtere bandens MOTD")
                .make());
        //minRankToRenameGang 32
        GangRank requiredRankToRenameGang = gang.getGangPermissions().getMinRankToRenameGang();
        setItem(32, new ItemCreator(Material.NAME_TAG)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToRenameGang.getFormatedNamed()),
                        "&6- &fÆndre bandens navn")
                .make());

        //minRankToManageAllies 16
        GangRank requiredRankToManageAllies = gang.getGangPermissions().getMinRankToManageAllies();
        setItem(32, new ItemCreator(Material.GREEN_BANNER)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToManageAllies.getFormatedNamed()),
                        "&6- &fHåndtere bandens allierede")
                .make());
        //minRankToManageRivals 25
        GangRank requiredRankToManageRivals = gang.getGangPermissions().getMinRankToManageRivals();
        setItem(32, new ItemCreator(Material.RED_BANNER)
                .setName("&aHåndter invitationer")
                .addLore("&7Med denne adgang vil medlemmer der er",
                        "{rank} eller derover kunne:"
                                .replace("{rank}", requiredRankToManageRivals.getFormatedNamed()),
                        "&6- &fHåndtere bandens rivaler")
                .make());

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        boolean isLeftClick = event.isLeftClick();
        boolean isRightClick = event.isRightClick();
        switch (clickedItem.getType()) {
            case BARRIER -> {
                GangSettingsGui gangSettingsGui = new GangSettingsGui(player, gang);
                gangSettingsGui.openTo(player);
            }
            case NETHER_STAR -> {
                GangRank currentMinRankToInvitePlayers = gang.getGangPermissions().getMinRankToInvitePlayers();
                GangRank newMinRankToInvitePlayers = currentMinRankToInvitePlayers;
                if(isLeftClick) {
                    newMinRankToInvitePlayers = GangRank.getRank(currentMinRankToInvitePlayers.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToInvitePlayers = GangRank.getRank(currentMinRankToInvitePlayers.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToInvitePlayers(newMinRankToInvitePlayers);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case PAPER -> {
                GangRank currentMinRankToManageInvites = gang.getGangPermissions().getMinRankToManageInvites();
                GangRank newMinRankToManageInvites = currentMinRankToManageInvites;
                if(isLeftClick) {
                    newMinRankToManageInvites = GangRank.getRank(currentMinRankToManageInvites.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToManageInvites = GangRank.getRank(currentMinRankToManageInvites.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToManageInvites(newMinRankToManageInvites);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case SKELETON_SKULL -> {
                GangRank currentMinRankToKickMembers = gang.getGangPermissions().getMinRankToKickMembers();
                GangRank newMinRankToKickMembers = currentMinRankToKickMembers;
                if(isLeftClick) {
                    newMinRankToKickMembers = GangRank.getRank(currentMinRankToKickMembers.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToKickMembers = GangRank.getRank(currentMinRankToKickMembers.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToKickMembers(newMinRankToKickMembers);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case PLAYER_HEAD -> {
                GangRank currentMinRankToManageMembers = gang.getGangPermissions().getMinRankToManageMemberRanks();
                GangRank newMinRankToManageMembers = currentMinRankToManageMembers;
                if(isLeftClick) {
                    newMinRankToManageMembers = GangRank.getRank(currentMinRankToManageMembers.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToManageMembers = GangRank.getRank(currentMinRankToManageMembers.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToManageMemberRanks(newMinRankToManageMembers);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case GOLD_INGOT -> {
                GangRank currentMinRankToUseShop = gang.getGangPermissions().getMinRankToUseShop();
                GangRank newMinRankToUseShop = currentMinRankToUseShop;
                if(isLeftClick) {
                    newMinRankToUseShop = GangRank.getRank(currentMinRankToUseShop.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToUseShop = GangRank.getRank(currentMinRankToUseShop.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToUseShop(newMinRankToUseShop);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case EXPERIENCE_BOTTLE -> {
                GangRank currentMinRankToLevelUpGang = gang.getGangPermissions().getMinRankToLevelUpGang();
                GangRank newMinRankToLevelUpGang = currentMinRankToLevelUpGang;
                if(isLeftClick) {
                    newMinRankToLevelUpGang = GangRank.getRank(currentMinRankToLevelUpGang.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToLevelUpGang = GangRank.getRank(currentMinRankToLevelUpGang.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToLevelUpGang(newMinRankToLevelUpGang);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case BOOK -> {
                GangRank currentMinRankToManageMOTD = gang.getGangPermissions().getMinRankToManageMOTD();
                GangRank newMinRankToManageMOTD = currentMinRankToManageMOTD;
                if(isLeftClick) {
                    newMinRankToManageMOTD = GangRank.getRank(currentMinRankToManageMOTD.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToManageMOTD = GangRank.getRank(currentMinRankToManageMOTD.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToManageMOTD(newMinRankToManageMOTD);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case NAME_TAG -> {
                GangRank currentMinRankToRename = gang.getGangPermissions().getMinRankToRenameGang();
                GangRank newMinRankToRename = currentMinRankToRename;
                if(isLeftClick) {
                    newMinRankToRename = GangRank.getRank(currentMinRankToRename.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToRename = GangRank.getRank(currentMinRankToRename.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToRenameGang(newMinRankToRename);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case GREEN_BANNER -> {
                GangRank currentMinRankToManageAllies = gang.getGangPermissions().getMinRankToManageAllies();
                GangRank newMinRankToManageAllies = currentMinRankToManageAllies;
                if(isLeftClick) {
                    newMinRankToManageAllies = GangRank.getRank(currentMinRankToManageAllies.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToManageAllies = GangRank.getRank(currentMinRankToManageAllies.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToManageAllies(newMinRankToManageAllies);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
            case RED_BANNER -> {
                GangRank currentMinRankToManageRivals = gang.getGangPermissions().getMinRankToManageRivals();
                GangRank newMinRankToManageRivals = currentMinRankToManageRivals;
                if(isLeftClick) {
                    newMinRankToManageRivals = GangRank.getRank(currentMinRankToManageRivals.getID() + 1);
                } else if (isRightClick) {
                    newMinRankToManageRivals = GangRank.getRank(currentMinRankToManageRivals.getID() - 1);
                }
                gang.getGangPermissions().setMinRankToManageRivals(newMinRankToManageRivals);
                ManagePermissionsGui managePermissionsGui = new ManagePermissionsGui(player, gang);
                managePermissionsGui.openTo(player);
            }
        }
    }
}


class ManageMOTDGui extends GUI {
    private final Player player;
    private final Gang gang;
    private final GangPlayer gangPlayer;
    private final GangPlayerManager gangPlayerManager = ZGangs.getGangPlayerManager();
    private final ChatInputManager chatInputManager = ZGangs.getChatInputManager();

    protected ManageMOTDGui(Player player, Gang playerGang) {
        super(54, ChatUtil.CC("&c&lBandens MOTD"));
        generateGuiBorder();
        this.player = player;
        this.gang = playerGang;
        this.gangPlayer = gangPlayerManager.findById(player.getUniqueId());

        setItem(13, new ItemCreator(Material.BOOK).addLore(gang.getGangMotd().getFullMotd()).make());

        setItem(19, new ItemCreator(Material.PAPER).setName("MOTD Linje 1")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine1() == null ? "" : gang.getGangMotd().getLine1())
                .setAmount(1)
                .make());
        setItem(21, new ItemCreator(Material.PAPER).setAmount(2).setName("MOTD Linje 2")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine2() == null ? "" : gang.getGangMotd().getLine2())
                .setAmount(1)
                .make());
        setItem(23, new ItemCreator(Material.PAPER).setAmount(3).setName("MOTD Linje 3")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine3() == null ? "" : gang.getGangMotd().getLine3())
                .setAmount(1)
                .make());
        setItem(25, new ItemCreator(Material.PAPER).setAmount(4).setName("MOTD Linje 4")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine4() == null ? "" : gang.getGangMotd().getLine4())
                .setAmount(1)
                .make());
        setItem(29, new ItemCreator(Material.PAPER).setAmount(5).setName("MOTD Linje 5")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine5() == null ? "" : gang.getGangMotd().getLine5())
                .setAmount(1)
                .make());
        setItem(31, new ItemCreator(Material.PAPER).setAmount(6).setName("MOTD Linje 6")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine6() == null ? "" : gang.getGangMotd().getLine6())
                .setAmount(1)
                .make());
        setItem(33, new ItemCreator(Material.PAPER).setAmount(7).setName("MOTD Linje 7")
                .addLore("&7Nuværende linje: ",
                        gang.getGangMotd().getLine7() == null ? "" : gang.getGangMotd().getLine7())
                .setAmount(1)
                .make());

        setItem(49, new ItemCreator(Material.BARRIER).setName("&cTilbage").make());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null) return;
        if(!gangPlayer.isInGang()) return;
        boolean isLeftClick = event.isLeftClick();
        switch (clickedItem.getType()) {
            case BARRIER -> {
                GangSettingsGui gangSettingsGui = new GangSettingsGui(player, gang);
                gangSettingsGui.openTo(player);
            }
            case PAPER -> {
                int amount = clickedItem.getAmount();
                chatInputManager.newStringInput(player, input -> {
                    if(input.length() > 32) {
                        ChatUtil.sendMessage(player, "&cFejl: Hver linje i bandens MOTD må være max 32 tegn");
                        return;
                    }
                    switch (amount) {
                        default -> {
                            ChatUtil.sendMessage(player, Messages.unexpectedError);
                            return;
                        }
                        case 1 -> {
                            gang.getGangMotd().setLine1(input);
                        }
                        case 2 -> {
                            gang.getGangMotd().setLine2(input);
                        }
                        case 3 -> {
                            gang.getGangMotd().setLine3(input);
                        }
                        case 4 -> {
                            gang.getGangMotd().setLine4(input);
                        }
                        case 5 -> {
                            gang.getGangMotd().setLine5(input);
                        }
                        case 6 -> {
                            gang.getGangMotd().setLine6(input);
                        }
                        case 7 -> {
                            gang.getGangMotd().setLine7(input);
                        }
                    }
                    ChatUtil.sendMessage(player, Config.prefix + " Du har nu ændret linje " + amount + " af bandens MOTD");
                });
            }
        }
    }
}
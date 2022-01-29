package me.Zcamt.zgangs.zgangs.managers;

import co.aikar.commands.PaperCommandManager;
import me.Zcamt.zgangs.zgangs.commands.MainGangCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    private final GangManager gangManager;
    private final GangPlayerManager gangPlayerManager;

    public CommandManager(GangManager gangManager, GangPlayerManager gangPlayerManager) {
        this.gangManager = gangManager;
        this.gangPlayerManager = gangPlayerManager;
    }

    public void registerCommands(JavaPlugin plugin){
        PaperCommandManager commandManager = new PaperCommandManager(plugin);
        commandManager.registerCommand(new MainGangCommand(gangManager, gangPlayerManager));
    }


}

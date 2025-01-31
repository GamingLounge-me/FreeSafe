package me.gaminglounge.freesafe; 
  
import java.util.logging.Level;

import org.bukkit.plugin.PluginManager; 
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.WorldEdit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.gaminglounge.freesafe.commands.AdminClaimCommands;
import me.gaminglounge.freesafe.commands.PlayerClaimComands;
import me.gaminglounge.freesafe.commands.TeamClaimCommands; 

public final class FreeSafe extends JavaPlugin { 
 
    public static ClaimManager claimManager;
    public static VariableManager variableManager;
    public static Visualization visualization;
    public static FreeSafe INSTANCE;
    public static TeamClaimManager teamClaimManager;
 
    @Override
    public void onLoad() {
        INSTANCE = this; 
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

    }

    @Override
    public void onEnable() {
        // this.listener();
        getLogger().log(Level.INFO, WorldEdit.getVersion());
        CommandAPI.onEnable();
        new PlayerClaimComands();
        new AdminClaimCommands();
        new TeamClaimCommands();        
        claimManager = new ClaimManager();
        variableManager = new VariableManager();
        visualization = new Visualization();
        teamClaimManager = new TeamClaimManager();

    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

    }

    public void listener() {
        PluginManager pm = getServer().getPluginManager();

        // pm.registerEvents(new InvClickEvent(), this);
    } 
} 

package me.gaminglounge.freesafe.commands;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.freesafe.FreeSafe;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class AdminClaimCommands {
    
    FreeSafe freeSafe = FreeSafe.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";
    
    public AdminClaimCommands(){
        new CommandAPICommand("adminclaim")
            .withSubcommand(new CommandAPICommand("addclaimblocks")
            .withPermission("freesafe.adminclaim.addclaimblocks")
                .withArguments(new PlayerArgument("target"))
                .withArguments(new IntegerArgument("value"))
                .executesPlayer((useless,args)->{
                    freeSafe.variableManager.setClaimBlock((Player) args.get("target"), freeSafe.variableManager.getClaimBlock((Player)args.get("target")) + (int)args.get("value"));
                    freeSafe.variableManager.setMaxClaimBlock((Player) args.get("target"), freeSafe.variableManager.getMaxClaimBlock((Player)args.get("target")) + (int)args.get("value"));
                    ((Player) args.get("target")).sendMessage(mm.deserialize(prefix+""));
            }))
            .withSubcommand(new CommandAPICommand("getclaimblocks"))
            .register();
    }
}

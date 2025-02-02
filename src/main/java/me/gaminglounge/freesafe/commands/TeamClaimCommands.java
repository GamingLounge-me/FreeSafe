package me.gaminglounge.freesafe.commands;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.gaminglounge.freesafe.FreeSafe;
import me.gaminglounge.freesafe.VariableManager;
import me.gaminglounge.gamingteams.DataBasePool;
import me.gaminglounge.gamingteams.Gamingteams;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TeamClaimCommands {
    
    FreeSafe freeSafe = FreeSafe.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";

    public TeamClaimCommands() {

        new CommandAPICommand("teamclaim")
            .withSubcommand(new CommandAPICommand("create")
            .withPermission("freesafe.teamclaim.create")
                .withArguments(new StringArgument("name"))
                .withOptionalArguments(new Location2DArgument("pos1"))
                .withOptionalArguments(new Location2DArgument("pos2"))
                .executesPlayer((executor,args)->{
                    int teamID = DataBasePool.getTeam(Gamingteams.INSTANCE.basePool, executor.getUniqueId());
                    if((int)teamID == 0){
                        executor.sendMessage(mm.deserialize(prefix+"<red>You are not in a team.</red>"));
                        return;
                    }
                    if(args.get("name") == null) {
                        executor.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim create \\<name></gray>"));
                        return;
                    }
                    if((args.get("pos1") == null) || (args.get("pos2") == null)) {
                    Location pos3 = freeSafe.variableManager.getPos3(executor);
                    Location pos4 = freeSafe.variableManager.getPos4(executor);
                    if(pos3 == null || pos4 == null) {
                        executor.sendMessage(mm.deserialize(prefix +"<red>Please select an area first.</red>"));
                        return;
                        }
                    FreeSafe.INSTANCE.teamClaimManager.createTeamRegion(teamID, pos3, pos4,(String) args.get("name"), executor);
                    }
                }))
            .withSubcommand(new CommandAPICommand("remove")
            .withPermission("freesafe.teamclaim.remove")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        int teamID = DataBasePool.getTeam(Gamingteams.INSTANCE.basePool, p.getUniqueId());
                        return VariableManager.listTeamRegion(teamID,p);
                    }
                    return null; 
                })))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /teamclaim remove \\<name></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.teamClaimManager.removeTeamRegion(player.getPlayer(), (String)args.get("Claimname"));
                }))
                .register();
    }
}

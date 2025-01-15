package me.gaminglounge.freesafe.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.wrappers.Location2D;
import me.gaminglounge.freesafe.FreeSafe;
import me.gaminglounge.freesafe.VariableManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PlayerClaimComands {

    FreeSafe freeSafe = FreeSafe.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";

    public PlayerClaimComands() {
        new CommandAPICommand("claim")
            .withPermission("freesafe.claim.create")
            .withSubcommand(new CommandAPICommand("create")
                .withArguments(new StringArgument("ClaimName"))
                .withArguments(new Location2DArgument("pos1"))
                .withArguments(new Location2DArgument("pos2"))
                .executesPlayer((player, args)->{
                    if (args.get("ClaimName") == null || args.get("pos1") == null || args.get("pos2") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim create name x z x z</gray>"));
                        return;
                    }
                    Location pos3 = VariableManager.pos1To3D((Location2D)args.get("pos1"));
                    Location pos4 = VariableManager.pos2To3D((Location2D)args.get("pos2"));
                    FreeSafe.INSTANCE.claimManager.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
                .withSubcommand(new CommandAPICommand("radius")
                .withArguments(new StringArgument("ClaimName"))
                .withArguments(new IntegerArgument("radius"))
                .executesPlayer((player, args)->{
                    if (args.get("ClaimName") == null || args.get("radius") == null || !(args.get("radius") instanceof Integer) || ((int)args.get("radius") < 1)) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim radius name radius</gray>"));
                        return;
                    }
                    Location pos3 = VariableManager.pos3FromRadius((Location)player.getLocation(), (int)args.get("radius"));
                    Location pos4 = VariableManager.pos4FromRadius((Location)player.getLocation(), (int)args.get("radius"));
                    FreeSafe.INSTANCE.claimManager.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
                .withSubcommand(new CommandAPICommand("pos1")
                .executesPlayer((player,args)->{
                    Location location = player.getLocation();
                    freeSafe.variableManager.savePos3(player,location);
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 1 set to "+location.getBlockX()+" "+location.getBlockZ()+"</green>"));
                }))
                .withSubcommand(new CommandAPICommand("pos2")
                .executesPlayer((player,args)->{
                    Location location = player.getLocation();
                    freeSafe.variableManager.savePos4(player,location);
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 2 set to "+location.getBlockX()+" "+location.getBlockZ()+"</green>"));
                }))
                .withSubcommand(new CommandAPICommand("claim")
                .withArguments(new StringArgument("ClaimName"))
                .executesPlayer((player,args)->{
                    if(args.get("ClaimName") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim claim name</gray>"));
                        return;
                    }
                    Location pos3 = freeSafe.variableManager.getPos3(player);
                    Location pos4 = freeSafe.variableManager.getPos4(player);
                    FreeSafe.INSTANCE.claimManager.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
                .withSubcommand(new CommandAPICommand("remove")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null; 
                })))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim remove name</gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.removeRegion(player.getPlayer(), (String)args.get("Claimname"));
                }))
        .register();
    }
}

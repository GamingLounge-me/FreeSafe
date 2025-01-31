package me.gaminglounge.freesafe.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
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
            .withSubcommand(new CommandAPICommand("pos1")
            .withPermission("claim.pos1")
                .executesPlayer((player,args)->{
                    Location location = player.getLocation();
                    freeSafe.variableManager.savePos3(player,location);
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 1 set to "+location.getBlockX()+" "+location.getBlockZ()+"</green>"));
                }))
            .withSubcommand(new CommandAPICommand("radius")
            .withPermission("Claim.radius")
                .withArguments(new IntegerArgument("radius"))
                .executesPlayer((player,args)->{
                    if(args.get("radius") == null || !(args.get("radius") instanceof Integer) || ((int)args.get("radius") < 1) || ((int)args.get("radius") <= 0)) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim radius \\<radius></gray>"));
                        return;
                    }
                    Location location = player.getLocation();
                    Location pos3 = VariableManager.pos3FromRadius(location.getBlock().getLocation(), (int)args.get("radius"));
                    freeSafe.variableManager.savePos3(player,pos3);
                    Location pos4 = VariableManager.pos4FromRadius(location.getBlock().getLocation(), (int)args.get("radius"));
                    freeSafe.variableManager.savePos4(player,pos4);
                    player.sendMessage(mm.deserialize(prefix +"<green>A claim area within the radius of "+(int)args.get("radius")+" blocks has been selected</green>"));
                }))
            .withSubcommand(new CommandAPICommand("pos2")
            .withPermission("Claim.pos2")
                .executesPlayer((player,args)->{
                    Location location = player.getLocation();
                    freeSafe.variableManager.savePos4(player,location);
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 2 set to "+location.getBlockX()+" "+location.getBlockZ()+"</green>"));
                }))
            .withSubcommand(new CommandAPICommand("create")
            .withPermission("Claim.create")
                .withArguments(new StringArgument("ClaimName"))
                .withOptionalArguments(new Location2DArgument("pos1"))
                .withOptionalArguments(new Location2DArgument("pos2"))
                .executesPlayer((player,args)->{
                    if(args.get("ClaimName") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim create \\<name></gray>"));
                        return;
                    }
                    if((args.get("pos1") == null) || (args.get("pos2") == null)) {
                        Location pos3 = freeSafe.variableManager.getPos3(player);
                        Location pos4 = freeSafe.variableManager.getPos4(player);
                        if(pos3 == null || pos4 == null) {
                            player.sendMessage(mm.deserialize(prefix +"<red>Please select an area first.</red>"));
                            return;
                        }
                        FreeSafe.INSTANCE.claimManager.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                        return;
                    }
                    Location pos3 = VariableManager.pos1To3D((Location2D)args.get("pos1"));
                    Location pos4 = VariableManager.pos2To3D((Location2D)args.get("pos2"));
                    FreeSafe.INSTANCE.claimManager.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
            .withSubcommand(new CommandAPICommand("remove")
            .withPermission("Claim.remove")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null; 
                })))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim remove \\<name></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.removeRegion(player.getPlayer(), (String)args.get("Claimname"));
                }))
            .withSubcommand(new CommandAPICommand("info")
            .withPermission("Claim.info")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null;
                    })))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim info \\<name></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.infoRegion(player.getPlayer(), (String)args.get("Claimname"));
                }))
            .withSubcommand(new CommandAPICommand("trust")
            .withPermission("Claim.trust")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null;
                    })))
                .withArguments(new PlayerArgument("player"))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null || args.get("player") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim trust \\<name> \\<player></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.trustRegion(player.getPlayer(), (String)args.get("Claimname"), (Player)args.get("player"));
                }))
            .withSubcommand(new CommandAPICommand("untrust")
            .withPermission("Claim.untrust")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null;
                    })))
                .withArguments(new PlayerArgument("player"))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null || args.get("player") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim untrust \\<name> \\<player></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.untrustRegion(player.getPlayer(), (String)args.get("Claimname"), (Player)args.get("player"));
                }))
                .withSubcommand(new CommandAPICommand("rebase")
                .withPermission("Claim.rebase")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null;
                    })))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim rebase \\<name></gray>"));
                        return;
                    }
                    Location pos3 = freeSafe.variableManager.getPos3(player);
                    Location pos4 = freeSafe.variableManager.getPos4(player);
                    if(pos3 == null || pos4 == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please select an area first.</red>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.redefineRegion(player, (String)args.get("Claimname"), pos3, pos4);
                    
                }))
                .withSubcommand(new CommandAPICommand("transfer")
                .withPermission("Claim.transfer")
                .withArguments(new StringArgument("Claimname").replaceSuggestions(ArgumentSuggestions.stringCollection(info ->{
                    if(info.sender() instanceof Player p){
                        return VariableManager.listRegion(p);
                    }
                    return null;
                    })))
                .withArguments(new PlayerArgument("player"))
                .executesPlayer((player,args)->{
                    if(args.get("Claimname") == null || args.get("player") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim transfer \\<name> \\<player></gray>"));
                        return;
                    }
                    FreeSafe.INSTANCE.claimManager.transferRegion(player.getPlayer(), (String)args.get("Claimname"), (Player)args.get("player"));
                }))

                .withSubcommand(new CommandAPICommand("help")
                .withPermission("Claim.help")
                .executesPlayer((player,args)->{
                    player.sendMessage(mm.deserialize("<red>Commands:</red>"));
                    player.sendMessage(mm.deserialize("<gray> /claim pos1</gray>"));
                    player.sendMessage(mm.deserialize("<gray> /claim pos2</gray>"));
                    player.sendMessage(mm.deserialize("<gray> /claim radius \\<radius></gray> <white>Sets the radius of the claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim create \\<name></gray> <white>Creates a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim remove \\<name></gray> <white>Removes a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim info \\<name></gray> <white>Shows information about a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim trust \\<name> \\<player></gray> <white>Trusts a player in a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim untrust \\<name> \\<player></gray> <white>Untrusts a player in a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim rebase \\<name></gray> <white>Redefines the area of a claim.</white>"));
                    player.sendMessage(mm.deserialize("<gray> /claim transfer \\<name> \\<player></gray> <white>Transfers a claim to another player.</white>"));
                }))
                .withSubcommand(new CommandAPICommand("visualize")
                .withPermission("Claim.visualize")
                .withArguments(new BooleanArgument("status"))
                .executesPlayer((player,args)->{
                    if(args.get("status") == null || !(args.get("status") instanceof Boolean)) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim visualize \\<true/false></gray>"));
                        return;
                    }
                    if((boolean)args.get("status")==true) {
                        freeSafe.visualization.addhotbar(player);
                        player.sendMessage(mm.deserialize(prefix +"<green>Visualisation enabled</green>"));
                        return;
                    }
                    else {
                        freeSafe.visualization.removehotbar(player);
                        player.sendMessage(mm.deserialize(prefix +"<green>Visualisation disabled</green>"));
                        return;
                    }
                }))
                .withSubcommand(new CommandAPICommand("claimblocks")
                .withPermission("Claim.claimblocks")
                .executesPlayer((player, args)->{
                    int min = freeSafe.variableManager.getClaimBlock(player);
                    int max = freeSafe.variableManager.getMaxClaimBlock(player);
                    player.sendMessage(mm.deserialize(prefix +"<gradient:dark_red:red>"+min+"<white>/</white>"+max+" Claimblocks</gradient>"));
                }))
                .withSubcommand(new CommandAPICommand("send")
                .withPermission("Claim.send")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new IntegerArgument("amount"))
                .executesPlayer((player, args)->{
                    if (args.get("amount") == null || (int)args.get("amount")< 0){
                        player.sendMessage(mm.deserialize(prefix+"The amount has to be more than 0"));
                        return;
                    }
                    if((int)args.get("amount") > freeSafe.variableManager.getClaimBlock(player)){
                        player.sendMessage(mm.deserialize(prefix+"<red>You don't have enougth claimblocks.</red>"));
                        return;
                    }
                    freeSafe.variableManager.setClaimBlock(player, freeSafe.variableManager.getClaimBlock(player) - (int) args.get("amount"));
                    freeSafe.variableManager.setMaxClaimBlock(player, freeSafe.variableManager.getMaxClaimBlock(player) - (int) args.get("amount"));
                    freeSafe.variableManager.setClaimBlock((Player)args.get("player"), freeSafe.variableManager.getClaimBlock((Player)args.get("player")) + (int) args.get("amount"));
                    freeSafe.variableManager.setMaxClaimBlock((Player)args.get("player"), freeSafe.variableManager.getMaxClaimBlock((Player)args.get("player")) + (int) args.get("amount"));
                    player.sendMessage(mm.deserialize("<gradient:#0c7819:#22e639>"+args.get("amount")+" Claimblocks</gradient> have been transfered to"+args.get("player")));
                }))
        .register();
    }
}

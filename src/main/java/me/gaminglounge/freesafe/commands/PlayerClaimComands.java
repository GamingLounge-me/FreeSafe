package me.gaminglounge.freesafe.commands;

import org.bukkit.Location;


import dev.jorel.commandapi.CommandAPICommand;
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
    String prefix = "<white>[</white><gradient:aqua,green>GamingLounge</gradient><white>]</white> ";

    public PlayerClaimComands() {
        new CommandAPICommand("claim")
            .withPermission("freesafe.claim.create")
            .withSubcommand(new CommandAPICommand("create")
                .withArguments(new StringArgument("ClaimName"))
                .withArguments(new Location2DArgument("pos1"))
                .withArguments(new Location2DArgument("pos2"))
                .executesPlayer((player, args)->{
                    if (args.get("ClaimName") == null || args.get("pos1") == null || args.get("pos2") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim create name x z x z</gray><red>.</red>"));
                        return;
                    }
                    Location pos3 = VariableManager.pos1To3D((Location2D)args.get("pos1"));
                    Location pos4 = VariableManager.pos2To3D((Location2D)args.get("pos2"));
                    FreeSafe.INSTANCE.createClaim.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
                .withSubcommand(new CommandAPICommand("radius")
                .withArguments(new StringArgument("ClaimName"))
                .withArguments(new IntegerArgument("radius"))
                .executesPlayer((player, args)->{
                    if (args.get("ClaimName") == null || args.get("radius") == null || !(args.get("radius") instanceof Integer) || ((int)args.get("radius") < 1)) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim radius name radius</gray><red>.</red>"));
                        return;
                    }
                    Location pos3 = VariableManager.pos3FromRadius((Location)player.getLocation(), (int)args.get("radius"));
                    Location pos4 = VariableManager.pos4FromRadius((Location)player.getLocation(), (int)args.get("radius"));
                    FreeSafe.INSTANCE.createClaim.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
                .withSubcommand(new CommandAPICommand("pos1")
                .executesPlayer((player,args)->{
                    freeSafe.variableManager.savePos3(player,(Location)player.getLocation());
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 1 set to "+player.getLocation()+".</green>"));
                }))
                .withSubcommand(new CommandAPICommand("pos2")
                .executesPlayer((player,args)->{
                    freeSafe.variableManager.savePos4(player,(Location)player.getLocation());
                    player.sendMessage(mm.deserialize(prefix +"<green>Position 2 set to "+player.getLocation()+".</green>"));
                }))
                .withSubcommand(new CommandAPICommand("claim")
                .withArguments(new StringArgument("ClaimName"))
                .executesPlayer((player,args)->{
                    if(args.get("ClaimName") == null) {
                        player.sendMessage(mm.deserialize(prefix +"<red>Please provide the correct input</red><gray> /claim claim name</gray><red>.</red>"));
                        return;
                    }
                    Location pos3 = freeSafe.variableManager.getPos3(player);
                    Location pos4 = freeSafe.variableManager.getPos4(player);
                    FreeSafe.INSTANCE.createClaim.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                }))
        .register();
    }
}

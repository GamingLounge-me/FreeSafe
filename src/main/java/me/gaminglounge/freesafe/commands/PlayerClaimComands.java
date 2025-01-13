package me.gaminglounge.freesafe.commands;

import org.bukkit.Location;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.wrappers.Location2D;
import me.gaminglounge.freesafe.FreeSafe;
import me.gaminglounge.freesafe.VariableManager;

public class PlayerClaimComands {

    FreeSafe freeSafe = FreeSafe.INSTANCE;

    public PlayerClaimComands() {
        new CommandAPICommand("claim")
            .withPermission("freesafe.claim.create")
            .withSubcommand(new CommandAPICommand("create"))
                .withArguments(new StringArgument("ClaimName"))
                .withArguments(new Location2DArgument("pos1"))
                .withArguments(new Location2DArgument("pos2"))
                .executesPlayer((player, args)->{
                    Location pos3 = VariableManager.pos1To3D((Location2D)args.get("pos1"));
                    Location pos4 = VariableManager.pos2To3D((Location2D)args.get("pos2"));
                    FreeSafe.INSTANCE.createClaim.createRegion(player.getPlayer(), (String)args.get("ClaimName"), pos3, pos4);
                })    
/*                .withSubcommand(new CommandAPICommand("radius"))
                .withArguments(new IntegerArgument("radius"))
                .executesPlayer((player, args)->{
                    // FreeSafe.INSTANCE.createClaim.createRegion(player.getPlayer(), (String)args.get("ClaimName"));
                })*/
        .register();
    }
}

package me.gaminglounge.freesafe;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.gaminglounge.gamingteams.DataBasePool;
import me.gaminglounge.gamingteams.Gamingteams;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TeamClaimManager {

    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";    FreeSafe freeSafe = FreeSafe.INSTANCE;
    
    public void createTeamRegion(int teamname, Location pos3, Location pos4, String claimName, Player sender){

        String nameAdapted = teamname + "_" + claimName.toLowerCase();
        var wesender = BukkitAdapter.adapt(sender);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(wesender.getWorld());

        if (regions.hasRegion(nameAdapted)) {
            sender.sendMessage(mm.deserialize(prefix + "<red>A Claim with the name </red><blue>" + claimName + "</blue><red> already exists.</red>"));
            return;
        }

        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        ProtectedRegion newTeamRegion = new ProtectedCuboidRegion(nameAdapted, min, max);
        //This could be the place to check for claimblocks, if teams get claimblocks?
        List<ProtectedRegion> overlapping = newTeamRegion.getIntersectingRegions(regions.getRegions().values());
        if (!overlapping.isEmpty()) {
            sender.sendMessage(mm.deserialize(prefix+"<red>The claim you tried to create, is overlaping with existing claims.</red><br><green>If you like to rebase a claim use<green> <gray>/claim rebase \\<name></gray>"));
            return;
        }
        //now would be a great line to add claimblock removal and so on.
        DefaultDomain owners = newTeamRegion.getOwners();
        DefaultDomain members = newTeamRegion.getMembers();

        //todo: fix this, so that it uses a listener, that returns admins and not owners, so that we can have multiple people manage the claim
        owners.addPlayer(wesender.getUniqueId());
        newTeamRegion.setOwners(owners);

        DataBasePool.getMembersUUIDs(Gamingteams.INSTANCE.basePool, teamname).forEach(teamMembers -> {
            members.addPlayer(teamMembers);    
        });


        newTeamRegion.setMembers(members);
        newTeamRegion.setFlag(Flags.TNT, StateFlag.State.ALLOW);
        regions.addRegion(newTeamRegion);
        sender.sendMessage(mm.deserialize(prefix+"<green>Your Claim named </green><blue>"+claimName+"</blue><green> has been created.</green>"));
        if (!(freeSafe.variableManager.getPos3(sender) == null || freeSafe.variableManager.getPos4(sender) == null)) {
            freeSafe.variableManager.removePos3(sender);
            freeSafe.variableManager.removePos4(sender);
            return;
        }
    }

    public void removeTeamRegion(Player owner, String claimName) {
        String nameAdapted = DataBasePool.getTeam(Gamingteams.INSTANCE.basePool, owner.getUniqueId())+"_"+claimName.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A claim with the name </red><blue>"+claimName+"</blue><red> does not exist.</red>"));
            return;
        }
    
        regions.removeRegion(nameAdapted);
        owner.sendMessage(mm.deserialize(prefix+"<green>Your claim named </green><blue>"+claimName+"</blue><green> has been removed.</green>"));
    }

    public void trustTeamRegion(){
        
    }
}

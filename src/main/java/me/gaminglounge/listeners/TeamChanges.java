package me.gaminglounge.listeners;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.gaminglounge.freesafe.FreeSafe;
import me.gaminglounge.teamslistener.TeamsJoinPlayer;
import me.gaminglounge.teamslistener.TeamsLeftPlayer;


public class TeamChanges implements Listener{
    
    @EventHandler
    public void addTeamMember(TeamsJoinPlayer event){
        
        int TeamID = event.getTeamID();
        UUID UUID = event.joinedMember();//this has been named in an incorrect way, but will be fixed
        FreeSafe.INSTANCE.getLogger().log(Level.WARNING, "The UUID "+UUID.toString()+" joined the Team with the ID "+TeamID);

        var wPlayer = WorldGuardPlugin.inst().wrapOfflinePlayer(Bukkit.getOfflinePlayer(UUID));
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        Bukkit.getWorlds().stream()
            .map(BukkitAdapter::adapt)
            .map(container::get)
            .map(RegionManager::getRegions)
            .map(map -> map.values())
            .flatMap(Collection::stream)
            .filter(region -> region.getId().startsWith(TeamID +  "_"))
            .forEach(region -> {
                DefaultDomain members = region.getMembers();
                members.addPlayer(wPlayer);
                region.setMembers(members);
            });
    }

    @EventHandler
    public void removeTeamMember(TeamsLeftPlayer event){

        int TeamID = event.getTeamID();
        UUID UUID = event.removedMember();
        FreeSafe.INSTANCE.getLogger().log(Level.WARNING, "The UUID "+UUID.toString()+" left the Team with the ID "+TeamID);

        var wPlayer = WorldGuardPlugin.inst().wrapOfflinePlayer(Bukkit.getOfflinePlayer(UUID));
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        Bukkit.getWorlds().stream()
        .map(BukkitAdapter::adapt)
        .map(container::get)
        .map(RegionManager::getRegions)
        .map(map -> map.values())
        .flatMap(Collection::stream)
        .filter(region -> region.getId().startsWith(TeamID +  "_"))
        .forEach(region -> {
            DefaultDomain members = region.getMembers();
            members.removePlayer(wPlayer);
            region.setMembers(members);
            DefaultDomain owners = region.getOwners();
            owners.removePlayer(wPlayer);
            region.setOwners(owners);
        });
    }

}

package me.gaminglounge.freesafe;


import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;

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

import net.kyori.adventure.text.minimessage.MiniMessage;


public class ClaimManager {
    
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";    FreeSafe freeSafe = FreeSafe.INSTANCE;

    public void createRegion(Player owner, String name, Location pos3, Location pos4) {
        
        if (!(ProtectedRegion.isValidId(name))){
            owner.sendMessage(mm.deserialize(prefix+"<red>Invalid region name</red>"));
            return;
        }
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionManager regions = container.get(weowner.getWorld());
        if (regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> already exists.</red>"));
            return;
        }
        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        ProtectedRegion region = new ProtectedCuboidRegion(nameAdapted, min, max);
        
        List<ProtectedRegion> overlapping = region.getIntersectingRegions(regions.getRegions().values());
        if (!overlapping.isEmpty()) {
            owner.sendMessage(mm.deserialize(prefix+"<red>The claim you tried to create, is overlaping with existing claims.</red><br><green>If you like to rebase a claim use<green> <gray>/claim rebase \\<name></gray>"));
            return;
        }

        DefaultDomain owners = region.getMembers();
        owners.addPlayer(weowner.getUniqueId());
        region.setOwners(owners);
        region.setFlag(Flags.TNT, StateFlag.State.ALLOW);
        regions.addRegion(region);
        owner.sendMessage(mm.deserialize(prefix+"<green>Your Claim named </green><blue>"+name+"</blue><green> has been created.</green>"));
        if (!(freeSafe.variableManager.getPos3(owner) == null || freeSafe.variableManager.getPos4(owner) == null)) {
            freeSafe.variableManager.removePos3(owner);
            freeSafe.variableManager.removePos4(owner);
            return;
        }
    }
    public void removeRegion(Player owner, String name) {
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        regions.removeRegion(nameAdapted);
        owner.sendMessage(mm.deserialize(prefix+"<green>Your Claim named </green><blue>"+name+"</blue><green> has been removed.</green>"));
    }
    public void infoRegion(Player owner, String name) {
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted) || regions.getRegion(nameAdapted) == null) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        ProtectedRegion region = regions.getRegion(nameAdapted);
        String finalList = "";
        for(String list:region.getMembers().getPlayers())finalList = finalList + String.valueOf(list);
        owner.sendMessage(mm.deserialize(
            "<red><bold>Claim Informations</bold></red><br>"+
            "<green>Name: </green><blue>"+name+"</blue><br>"+
            "<green>Owner: </green><blue>"+owner.getName()+"</blue><br>"+
            "<green>Members: </green><blue>"+finalList+"</blue><br>"+
            "<green>Area: </green><blue>"+FreeSafe.INSTANCE.variableManager.squareArear(region)+"</blue>"
        ));
    }
    public void trustRegion(Player owner, String name, Player target) {
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted) || regions.getRegion(nameAdapted) == null) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        ProtectedRegion region = regions.getRegion(nameAdapted);
        DefaultDomain members = region.getMembers();
        members.addPlayer(BukkitAdapter.adapt(target).getUniqueId());
        region.setMembers(members);
        owner.sendMessage(mm.deserialize(prefix+"<green>"+target.getName()+"</green><green> has been added as a member of </green><blue>"+name+"</blue>"));
    }
    public void untrustRegion(Player owner, String name, Player target) {
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted) || regions.getRegion(nameAdapted) == null) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        ProtectedRegion region = regions.getRegion(nameAdapted);
        DefaultDomain members = region.getMembers();
        members.removePlayer(BukkitAdapter.adapt(target).getUniqueId());
        region.setMembers(members);
        owner.sendMessage(mm.deserialize(prefix+"<green>"+target.getName()+"</green><green> has been removed as a member of </green><blue>"+name+"</blue>"));
    }
    public void redefineRegion(Player owner, String name, Location pos3, Location pos4){
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        ProtectedRegion region = regions.getRegion(nameAdapted);
        ProtectedRegion newRegion = new ProtectedCuboidRegion(region.getId(), min, max);

        newRegion.setFlags(region.getFlags());
        newRegion.setMembers(region.getMembers());
        newRegion.setOwners(region.getOwners());
    
        List<ProtectedRegion> overlapping = region.getIntersectingRegions(regions.getRegions().values())
            .stream()
            .filter(r -> !r.getId().equals(region.getId()))
            .collect(Collectors.toList());
        if (!overlapping.isEmpty()) {
            owner.sendMessage(mm.deserialize(prefix+"<red>The new area of the claim you tried to redefine, is overlaping with existing claims.</red>"));
            return;
        }
        regions.removeRegion(nameAdapted);
        regions.addRegion(newRegion);
    
        owner.sendMessage(mm.deserialize(prefix + "<green>The claim </green><blue>" + name + "</blue><green> has been redefined.</green>"));        
    }
    public void transferRegion(Player owner, String name, Player target) {
        String nameAdapted = owner.getUniqueId().toString()+"_"+name.toLowerCase();
        var weowner = BukkitAdapter.adapt(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(weowner.getWorld());
        if (!regions.hasRegion(nameAdapted) || regions.getRegion(nameAdapted) == null) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> does not exist.</red>"));
            return;
        }
        ProtectedRegion region = regions.getRegion(nameAdapted);
        DefaultDomain owners = region.getOwners();
        owners.removeAll();
        owners.addPlayer(BukkitAdapter.adapt(target).getUniqueId());
        region.setOwners(owners);
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();
        ProtectedRegion newRegion = new ProtectedCuboidRegion(target.getUniqueId().toString()+"_"+name.toLowerCase(), min, max);

        newRegion.setFlags(region.getFlags());
        newRegion.setMembers(region.getMembers());
        newRegion.setOwners(region.getOwners());
        owner.sendMessage(mm.deserialize(prefix+"<green>"+target.getName()+"</green><green> has been set as the new owner of </green><blue>"+name+"</blue>"));
    }
}
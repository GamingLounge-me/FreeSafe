package me.gaminglounge.freesafe;


import org.bukkit.entity.Player;
import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
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

        //this is the place that basically saves regions and holds them
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        //this defines the world of the region
        RegionManager regions = container.get(weowner.getWorld());
        if (regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name </red><blue>"+name+"</blue><red> already exists.</red>"));
            return;
        }
        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        ProtectedRegion region = new ProtectedCuboidRegion(nameAdapted, min, max);

        DefaultDomain owners = region.getMembers();
        owners.addPlayer(weowner.getUniqueId());
        region.setOwners(owners);
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
}
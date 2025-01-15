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


public class CreateClaim {
    
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><gradient:aqua,green>GamingLounge</gradient><white>]</white> ";

    public void createRegion(Player owner, String name, Location pos3, Location pos4) {
        
        if (!(ProtectedRegion.isValidId(name))){
            owner.sendMessage(mm.deserialize(prefix+"<red>Invalid region name</red>"));
            return;
        }
        String nameAdapted = name.toLowerCase()+"_"+owner.getUniqueId().toString();
        var weowner = BukkitAdapter.adapt(owner);

        //this is the place that basically saves regions and holds them
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        //this defines the world of the region
        RegionManager regions = container.get(weowner.getWorld());
        if (regions.hasRegion(nameAdapted)) {
            owner.sendMessage(mm.deserialize(prefix+"<red>A Claim with the name "+name+" already exists.</red>"));
            return;
        }
        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        ProtectedRegion region = new ProtectedCuboidRegion(nameAdapted, min, max);

        DefaultDomain owners = region.getMembers();
        owners.addPlayer(weowner.getUniqueId());
        region.setOwners(owners);
        regions.addRegion(region);
        owner.sendMessage(mm.deserialize(prefix+"<green>Your Claim named"+name+", has been created.</green>"));
    }
}
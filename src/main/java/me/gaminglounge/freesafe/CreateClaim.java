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

    public void createRegion(Player owner, String name, Location pos3, Location pos4) {
        
        if (!(ProtectedRegion.isValidId(name))){
            owner.sendMessage(mm.deserialize("<red>Invalid region name</red>"));
            return;
        }

        com.sk89q.worldedit.entity.Player weowner = BukkitAdapter.adapt(owner);

        //this is the place that basically saves regions and holds them
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        //this defines the world of the region
        RegionManager regions = container.get(weowner.getWorld());

        //this gives the region a name/id
        ProtectedRegion region = regions.getRegion(name);

        region.setPriority(0);

        BlockVector3 min = BlockVector3.at(pos3.x(),pos3.y(),pos3.z());
        BlockVector3 max = BlockVector3.at(pos4.x(),pos4.y(),pos4.z());
        region = new ProtectedCuboidRegion(name, min, max);

        DefaultDomain owners = region.getMembers();
        owners.addPlayer(weowner.getUniqueId());
        owners.addGroup("owners");
        regions.addRegion(region);
    }
}
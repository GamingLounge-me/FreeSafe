package me.gaminglounge.freesafe;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import dev.jorel.commandapi.wrappers.Location2D;

public class VariableManager {

    private int startClaimblocks = 45000;

    public static final NamespacedKey claimblock = new NamespacedKey("freesafe", "claimblock");
    public static final NamespacedKey maxclaimblock = new NamespacedKey("freesafe", "maxclaimblock");
    private HashMap<Entity, Location> savedpos3;
    private HashMap<Entity, Location> savedpos4;

    public VariableManager() {
        savedpos3 = new HashMap<Entity, Location>();
        savedpos4 = new HashMap<Entity, Location>();
    }

    public void setClaimBlock(Player player, int amount) {
        player.getPersistentDataContainer().set(claimblock, org.bukkit.persistence.PersistentDataType.INTEGER, amount);
    }

    public int getClaimBlock(Player player) {
        if(!(player.getPersistentDataContainer().has(claimblock, org.bukkit.persistence.PersistentDataType.INTEGER))){
            player.getPersistentDataContainer().set(claimblock, org.bukkit.persistence.PersistentDataType.INTEGER, startClaimblocks);	
            return startClaimblocks;
        }
        return player.getPersistentDataContainer().get(claimblock, org.bukkit.persistence.PersistentDataType.INTEGER);
    }

    public void setMaxClaimBlock(Player player, int amount) {
        player.getPersistentDataContainer().set(maxclaimblock, org.bukkit.persistence.PersistentDataType.INTEGER, amount);
        if (amount < 0){
            amount = 0;
            Bukkit.getLogger().warning("Something went wrong, "+player.getName()+"owns les then 0 max claimblocks.");
        }
    }

    public int getMaxClaimBlock(Player player) {
        if(!(player.getPersistentDataContainer().has(maxclaimblock, org.bukkit.persistence.PersistentDataType.INTEGER))){
            player.getPersistentDataContainer().set(maxclaimblock, org.bukkit.persistence.PersistentDataType.INTEGER, startClaimblocks);	
            return startClaimblocks;
        }
        return player.getPersistentDataContainer().get(maxclaimblock, org.bukkit.persistence.PersistentDataType.INTEGER);
    }

    public void savePos3(Entity player, Location pos1) {
        savedpos3.put(player, pos1);
    }

    public Location getPos3(Entity player) {
        return savedpos3.get(player);
    }

    public Location removePos3(Entity player) {
        return savedpos3.remove(player);
    }

    public void savePos4(Entity player, Location pos2) {
        savedpos4.put(player, pos2);
    }

    public Location getPos4(Entity player) {
        return savedpos4.get(player);
    }

    public Location removePos4(Entity player) {
        return savedpos4.remove(player);
    }

    public static Location pos1To3D(Location2D pos1) {
        
        Location pos3 = new Location(pos1.getWorld(), pos1.getX(), pos1.getWorld().getMinHeight(), pos1.getZ());
        return pos3;
    }

    public static Location pos2To3D(Location2D pos2) {
        
        Location pos4 = new Location(pos2.getWorld(), pos2.getX(), pos2.getWorld().getMaxHeight(), pos2.getZ());
        return pos4;
    }

    public static Location pos3FromRadius(Location pos1, int radius) {
        Location pos3 = new Location(pos1.getWorld(), pos1.getX() - radius, pos1.getWorld().getMinHeight(), pos1.getZ() - radius);
        return pos3;
    }

    public static Location pos4FromRadius(Location pos1, int radius) {
        Location pos4 = new Location(pos1.getWorld(), pos1.getX() + radius, pos1.getWorld().getMaxHeight(), pos1.getZ() + radius);
        return pos4;
    }

    public static List<String> listRegion(Player owner) {
        
        var wgowner = WorldGuardPlugin.inst().wrapPlayer(owner);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        List<String> regionNames = Bukkit.getWorlds().stream()
            .map(BukkitAdapter::adapt)
            .map(container::get)
            .map(RegionManager::getRegions)
            .map(map -> map.values())
            .flatMap(Collection::stream)
            .filter(region -> region.isOwner(wgowner))
            .map(ProtectedRegion::getId)
            .filter(region -> region.startsWith(owner.getUniqueId().toString() +  "_"))
            .map(VariableManager::realRegionName)
            .collect(Collectors.toList());
        return regionNames;
    }

    public static List<String> listTeamRegion(int TeamID, Player sender) {

        var wsender = WorldGuardPlugin.inst().wrapPlayer(sender);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        List<String> regionNames = Bukkit.getWorlds().stream()
            .map(BukkitAdapter::adapt)
            .map(container::get)
            .map(RegionManager::getRegions)
            .map(map -> map.values())
            .flatMap(Collection::stream)
            .filter(region -> region.isOwner(wsender))
            .map(ProtectedRegion::getId)
            .filter(region -> region.startsWith(TeamID + "_"))
            .map(VariableManager::realRegionName)
            .collect(Collectors.toList());

        return regionNames;
    }

    public static String realRegionName(String region){
        if(Pattern.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}_.*", region)){
            return region.split("_",2)[1];
        }
        else if(Pattern.matches("[0-9]+_.*",region)){
            return region.split("_",2)[1];
        }
        else return region;
    }
    public static int squareArear(ProtectedRegion region){
        int x = region.getMaximumPoint().x() - region.getMinimumPoint().x();
        int z = region.getMaximumPoint().z() - region.getMinimumPoint().z();
        if (x == 0) x = 1;
        if (z == 0) z = 1;
        return Math.abs(x)*Math.abs(z);
    }
}
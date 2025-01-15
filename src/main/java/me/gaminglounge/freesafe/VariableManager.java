package me.gaminglounge.freesafe;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import dev.jorel.commandapi.wrappers.Location2D;

public class VariableManager {

    private HashMap<Entity, Location> savedpos3;
    private HashMap<Entity, Location> savedpos4;

    public VariableManager() {
        savedpos3 = new HashMap<Entity, Location>();
        savedpos4 = new HashMap<Entity, Location>();
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
}
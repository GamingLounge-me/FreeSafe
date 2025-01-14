package me.gaminglounge.freesafe;

import org.bukkit.Location;

import dev.jorel.commandapi.wrappers.Location2D;

public class VariableManager {

    public static Location pos1To3D(Location2D pos1) {
        
        Location pos3 = new Location(pos1.getWorld(), pos1.getX(), pos1.getWorld().getMinHeight(), pos1.getZ());
        return pos3;
    }

    public static Location pos2To3D(Location2D pos2) {
        
        Location pos4 = new Location(pos2.getWorld(), pos2.getX(), pos2.getWorld().getMaxHeight(), pos2.getZ());
        return pos4;
    }

    public static Location pos3FromRadius(Location pos1, Integer radius) {
        Location pos3 = new Location(pos1.getWorld(), pos1.getX() - radius, pos1.getWorld().getMinHeight(), pos1.getZ() - radius);
        return pos3;
    }

    public static Location pos4FromRadius(Location pos1, Integer radius) {
        Location pos4 = new Location(pos1.getWorld(), pos1.getX() + radius, pos1.getWorld().getMaxHeight(), pos1.getZ() + radius);
        return pos4;
    }
}
package me.gaminglounge.freesafe;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.minimessage.MiniMessage;
//does not work at all, cause the scheduler would run for ever
public class Visualization {
    
    MiniMessage mm = MiniMessage.miniMessage();
    FreeSafe freeSafe = FreeSafe.INSTANCE;
    Map<Player, BukkitTask> map;

    public Visualization(){
        map = new HashMap<Player,BukkitTask>();
    }


    public void addhotbar (Player player){

        if(map.isEmpty()||!map.containsKey(player)) {
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(freeSafe, () -> {
                Location pos1 = freeSafe.variableManager.getPos3(player);
                if (pos1 == null) return;
                Location pos2 = freeSafe.variableManager.getPos4(player);
                if (pos2 == null) return;
                int x = pos1.getBlockX() - pos2.getBlockX();
                int z = pos1.getBlockZ() - pos2.getBlockZ();
                int arear = Math.abs(x)*Math.abs(z);
                int freeblocks = freeSafe.variableManager.getClaimBlock(player);
                int x1 = pos1.getBlockX();
                int z1 = pos1.getBlockZ();
                int x2 = pos2.getBlockX();
                int z2 = pos2.getBlockZ();
                String blockInfo = "<gold>Pos1</gold> <aqua>"+x1+" "+z1+"</aqua>      <gradient:#0c7819:#22e639>"+arear+"<white>/</white>"+freeblocks+"</gradient>      "+"<gold>Pos2</gold> <aqua>"+x2+" "+z2+"</aqua>";
                if (freeblocks < arear) blockInfo = "<gold>Pos1</gold> <aqua>"+x1+" "+z1+"</aqua>      <gradient:dark_red:red>"+arear+"<white>/</white>"+freeblocks+"</gradient>      "+"<gold>Pos2</gold> <aqua>"+x2+" "+z2+"</aqua>";
                player.sendActionBar(mm.deserialize(blockInfo));
            }, 0, 15);
            map.put(player, task);
        }
        else return;
    }
    public void removehotbar(Player player){
        if(map.containsKey(player)) map.remove(player).cancel();
        else return;
    }
}

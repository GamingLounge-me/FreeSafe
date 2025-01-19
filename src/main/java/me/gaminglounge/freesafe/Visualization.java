package me.gaminglounge.freesafe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.minimessage.MiniMessage;
//does not work at all, cause the scheduler would run for ever
public class Visualization {
    
        MiniMessage mm = MiniMessage.miniMessage();
        FreeSafe freeSafe = FreeSafe.INSTANCE;

    public void hotbar (Player player, Boolean status){

        Location pos1 = freeSafe.variableManager.getPos3(player);
        Location pos2 = freeSafe.variableManager.getPos4(player);
        int x = pos1.getBlockX() - pos2.getBlockX();
        int z = pos1.getBlockZ() - pos2.getBlockZ();
        int arear = Math.abs(x)*Math.abs(z);

        if(status == true || pos1 != null || pos2 != null){
            Bukkit.getScheduler().runTaskTimer(FreeSafe.INSTANCE, () ->{

            player.sendActionBar(mm.deserialize(""+String.format("%.1f",pos2.getBlock())+"      "+arear+"      "+String.format("%.1f",pos1.getBlock())));
            }, 0, 15);
        }
    }
}

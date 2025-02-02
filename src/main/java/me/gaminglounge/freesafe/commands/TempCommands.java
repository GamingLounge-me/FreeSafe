package me.gaminglounge.freesafe.commands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.jonas.stuff.utility.ItemBuilder;
import dev.jorel.commandapi.CommandAPICommand;
import me.gaminglounge.freesafe.FreeSafe;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TempCommands {

    FreeSafe freeSafe = FreeSafe.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();
    String prefix = "<white>[</white><#ff0000>G<#ff1100>a<#ff2200>m<#ff3300>i<#ff4400>n<#ff5500>g<#ff6600>L<#ff7700>o<#ff8800>u<#ff9900>n<#ffaa00>g<#ffbb00>e<white>]</white> ";
    
    public TempCommands(){
        new CommandAPICommand("Telepad")
            .withSubcommand(new CommandAPICommand("get")
            .withPermission("freesafe.tempcommands.telepad.get")
            .executesPlayer((executor,args)->{
                ItemStack diamondBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);
                if(executor.getInventory().containsAtLeast(diamondBlock,1)){
                    if(executor.getPlayer().getInventory().addItem(new ItemBuilder()
                        .setMaterial(Material.BEACON)
                        .setName("Telepad")
                        .whenPlaced("telepads:buildTelepad")
                        .build()).isEmpty()){
                            executor.getPlayer().getInventory().removeItemAnySlot(diamondBlock);
                        }
                        else {executor.sendMessage(mm.deserialize(prefix+"<aqua>You don't have enought space in your inventorry<aqua>"));}
                }
                else {
                    executor.sendMessage(mm.deserialize(prefix+"<aqua>You need a Diamondblock for that<aqua>"));
                }
            }))
            .register();
    }
}

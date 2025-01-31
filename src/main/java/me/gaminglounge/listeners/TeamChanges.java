package me.gaminglounge.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.gaminglounge.teamslistener.TeamsJoinPlayer;


public class TeamChanges implements Listener{
    
@EventHandler
public void test(TeamsJoinPlayer event){
    
    int Teamid = event.getTeamID();
    UUID UUID = event.removedMember();

    }
}

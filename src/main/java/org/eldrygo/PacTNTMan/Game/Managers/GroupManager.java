package org.eldrygo.PacTNTMan.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.eldrygo.PacTNTMan.Plugin.Utils.OtherUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupManager {
    List<Player> players = new ArrayList<>();
    Player pacMan;

    public void syncPlayers() {
        clearPlayers();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) continue;
            if (pacMan.equals(p)) continue;

            players.add(p);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }
    public int getPlayersInt() {
        int count = 0;
        for (Player p : players) {
            count = count + 1;
        }
        return count;
    }

    public void clearPlayers() {
        players.clear();
    }

    public void clearPacMan() {
        if (pacMan != null) pacMan = null;
        for (String entry : new HashSet<>(OtherUtils.getPacManTeam().getEntries())) {
            OtherUtils.getPacManTeam().removeEntry(entry);
        }
    }
    public void setPacMan(Player p) {
        clearPacMan();
        pacMan = p;
        if (!OtherUtils.getPacManTeam().getEntries().contains(p.getName())) {
            OtherUtils.getPacManTeam().addEntity(p);
        }
    }
    public Player getPacMan() {
        return pacMan;
    }
}

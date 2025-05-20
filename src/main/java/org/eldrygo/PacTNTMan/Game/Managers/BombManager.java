package org.eldrygo.PacTNTMan.Game.Managers;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.eldrygo.PacTNTMan.Lib.Time.Managers.TimerManager;
import org.eldrygo.PacTNTMan.PacTNTMan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BombManager {
    private final List<Player> playersWithBomb = new ArrayList<>();

    private Team bombTeam;
    private final ItemManager itemManager;
    private final PacTNTMan plugin;
    private final TimerManager timerManager;

    public BombManager(ItemManager itemManager, PacTNTMan plugin, TimerManager timerManager) {
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.timerManager = timerManager;
        this.bombTeam = null;
    }

    public void addBomb(Player player) {
        if (!playersWithBomb.contains(player)) {
            playersWithBomb.add(player);
            bombTeam.addEntry(player.getName());
            player.setGlowing(true);
            itemManager.setBombHead(player);
            if (!timerManager.isRunning(player.getName())) {
                timerManager.createTimer(player.getName(), 30);
            }
        }
    }

    public void takeBomb(Player player) {
        if (playersWithBomb.contains(player)) {
            playersWithBomb.remove(player);
            bombTeam.removeEntry(player.getName());
            player.setGlowing(false);
            itemManager.removeBombHead(player);
            if (timerManager.isRunning(player.getName())) {
                timerManager.stopTimer(player.getName());
                timerManager.removeTimer(player.getName());
            }
        }
    }

    public void resetBombs() {
        for (Player p : playersWithBomb) {
            itemManager.removeBombHead(p);
            p.setGlowing(false);
        }
        playersWithBomb.clear();
        for (String entry : new HashSet<>(bombTeam.getEntries())) {
            bombTeam.removeEntry(entry);
        }
    }

    public Team getBombTeam() {
        return bombTeam;
    }
    public List<Player> getPlayersWithBomb() {
        return playersWithBomb;
    }

    public void setBombTeam(Team bombTeam) {
        this.bombTeam = bombTeam;
    }

    public void bombExplode(Player p) {
        plugin.getLogger().warning("Player " + p.getName() + " exploded.");
        p.damage(100000.0);
        timerManager.stopTimer(p.getName());
        timerManager.removeTimer(p.getName());
    }
}

package org.eldrygo.RTNTTag.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.eldrygo.RTNTTag.RTNTTag;

import java.util.*;
import java.util.stream.Collectors;

public class BombManager {
    private final List<Player> playersWithBomb = new ArrayList<>();

    private Team bombTeam;
    private final RTNTTag plugin;
    private final ItemManager itemManager;

    public BombManager(RTNTTag plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.bombTeam = null;
    }

    public void addBomb(Player player) {
        if (!playersWithBomb.contains(player)) {
            playersWithBomb.add(player);
            bombTeam.addEntry(player.getName());
            player.setGlowing(true);
            itemManager.setBombHead(player);
        }
    }

    public void takeBomb(Player player) {
        if (playersWithBomb.contains(player)) {
            playersWithBomb.remove(player);
            bombTeam.removeEntry(player.getName());
            player.setGlowing(false);
            itemManager.removeBombHead(player);
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

    public void giveRandomBombs(int playerInt) {
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
        Collections.shuffle(onlinePlayers);
        List<Player> selectedPlayers = onlinePlayers.stream().limit(playerInt).collect(Collectors.toList());

        for (Player player : selectedPlayers) {
            addBomb(player);
            plugin.getLogger().warning("Gived the GoldenBomb to " + player.getName() + "with giveRandomBombs method.");
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
}
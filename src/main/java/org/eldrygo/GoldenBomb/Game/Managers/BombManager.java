package org.eldrygo.GoldenBomb.Game.Managers;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Utils.OtherUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BombManager {
    private final List<Player> playersWithBomb = new ArrayList<>();

    private Team bombTeam;
    private final GoldenBomb plugin;

    public BombManager(GoldenBomb plugin) {
        this.plugin = plugin;
        this.bombTeam = null;
    }

    public void addBomb(Player player) {
        if (!playersWithBomb.contains(player)) {
            playersWithBomb.add(player);
            bombTeam.addEntity(player);
            player.setGlowing(true);
        }
    }

    public void takeBomb(Player player) {
        if (playersWithBomb.contains(player)) {
            playersWithBomb.remove(player);
            bombTeam.removeEntity(player);
            player.setGlowing(false);
        }
    }

    public void resetBombs() {
        playersWithBomb.clear();
        for (String entry : new HashSet<>(bombTeam.getEntries())) {
            bombTeam.removeEntry(entry);
            Objects.requireNonNull(
                    Bukkit.getEntity(UUID.fromString(entry))).setGlowing(false);
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

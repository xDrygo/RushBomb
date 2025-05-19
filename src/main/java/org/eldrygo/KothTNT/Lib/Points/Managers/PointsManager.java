package org.eldrygo.KothTNT.Lib.Points.Managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointsManager {

    private final Map<Player, Integer> points;

    public PointsManager() {
        points = new HashMap<>();
    }

    public void addPoints(Player p, int amount) {
        points.put(p, points.getOrDefault(p, 0) + amount);
    }

    public void removePoints(Player p, int amount) {
        points.put(p, Math.max(0, points.getOrDefault(p, 0) - amount));
    }

    public void setPoints(Player p, int amount) {
        points.put(p, Math.max(0, amount));
    }

    public int getPoints(Player p) {
        return points.getOrDefault(p, 0);
    }

    public void resetPoints(Player p) {
        points.put(p, 0);
    }

    public void resetAllPoints() {
        points.clear();
    }

    public List<Player> getLowestPlayers(int amount) {
        return points.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(amount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
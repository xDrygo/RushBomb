package org.eldrygo.KothTNT.Lib.Points.Managers;

import org.bukkit.entity.Player;
import org.eldrygo.KothTNT.Plugin.Utils.OtherUtils;

import java.util.Comparator;
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
        return OtherUtils.getGamePlayers().stream()
                .sorted(Comparator.comparingInt(this::getPoints))
                .limit(amount)
                .collect(Collectors.toList());
    }
    public String getLowestPlayerNameAtPosition(int position) {
        if (position <= 0) return null;

        List<Player> sorted = OtherUtils.getGamePlayers().stream()
                .sorted(Comparator.comparingInt(this::getPoints))
                .collect(Collectors.toList());

        if (position > sorted.size()) return null;

        return sorted.get(position - 1).getName();
    }
}
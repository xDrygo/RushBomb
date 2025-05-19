package org.eldrygo.KothTNT.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;

public class RunnableManager {
    private BukkitRunnable task;
    private final KothTNT plugin;
    private final BombManager bombsManager;
    private final PointsManager pointsManager;

    public RunnableManager(KothTNT plugin, BombManager bombsManager, PointsManager pointsManager) {
        this.plugin = plugin;
        this.bombsManager = bombsManager;
        this.pointsManager = pointsManager;
    }

    public void startTask() {
        plugin.getLogger().info("GoldenBomb runnable started.");
        task = new BukkitRunnable() {
            @Override
            public void run() {
                execute();
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
    }

    public void stopTask() {
        if (task != null) {
            task.cancel();
            plugin.getLogger().info("GoldenBomb runnable stopped.");
        }
    }

    public boolean taskExists() {
        return task != null;
    }

    private void execute() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (bombsManager.getPlayersWithBomb().contains(p)) {
                if (!p.isGlowing()) {
                    p.setGlowing(true);
                }

                if (!bombsManager.getBombTeam().getEntries().contains(p.getName())) {
                    bombsManager.getBombTeam().addEntity(p);
                }

                pointsManager.addPoints(p, 1);
            } else {
                if (p.isGlowing() && !p.isOp()) {
                    p.setGlowing(false);
                }

                if (bombsManager.getBombTeam().getEntries().contains(p.getName())) {
                    bombsManager.getBombTeam().removeEntity(p);
                }
            }
        }
    }
}

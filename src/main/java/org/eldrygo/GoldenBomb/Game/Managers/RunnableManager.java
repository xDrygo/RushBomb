package org.eldrygo.GoldenBomb.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eldrygo.GoldenBomb.GoldenBomb;

public class RunnableManager {
    private BukkitRunnable task;
    private final GoldenBomb plugin;
    private final BombManager bombsManager;

    public RunnableManager(GoldenBomb plugin, BombManager bombsManager) {
        this.plugin = plugin;
        this.bombsManager = bombsManager;
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

                // COMPROBACION DEL ITEM DE LA BOMBA.
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

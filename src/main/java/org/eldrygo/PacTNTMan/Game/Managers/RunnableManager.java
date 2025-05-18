package org.eldrygo.PacTNTMan.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eldrygo.PacTNTMan.PacTNTMan;
import org.eldrygo.PacTNTMan.Plugin.Utils.OtherUtils;

public class RunnableManager {

    private final BombManager bombsManager;
    private BukkitRunnable task;
    private final PacTNTMan plugin;
    private final GroupManager groupManager;

    public RunnableManager(BombManager bombsManager, PacTNTMan plugin, GroupManager groupManager) {
        this.bombsManager = bombsManager;
        this.plugin = plugin;
        this.groupManager = groupManager;
    }

    public void startTask() {
        plugin.getLogger().info("PacTNTMan runnable started.");
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
            plugin.getLogger().info("PacTNTMan runnable stopped.");
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
            } else if (groupManager.getPacMan() != null && groupManager.getPacMan().equals(p)) {
                if (!p.isGlowing()) {
                    p.setGlowing(true);
                }

                if (!OtherUtils.getPacManTeam().getEntries().contains(p.getName())) {
                    OtherUtils.getPacManTeam().addEntity(p);
                }
            } else {
                if (p.isGlowing()) {
                    p.setGlowing(false);
                }

                if (bombsManager.getBombTeam().getEntries().contains(p.getName())) {
                    bombsManager.getBombTeam().removeEntity(p);
                } else if (!OtherUtils.getPacManTeam().getEntries().contains(p.getName())) {
                    OtherUtils.getPacManTeam().removeEntity(p);
                }
            }
        }
    }
}

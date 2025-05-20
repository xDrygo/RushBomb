package org.eldrygo.PacTNTMan.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.eldrygo.PacTNTMan.Lib.Time.Managers.TimerManager;
import org.eldrygo.PacTNTMan.PacTNTMan;
import org.eldrygo.PacTNTMan.Plugin.Managers.ConfigManager;
import org.eldrygo.XBossBar.API.XBossBarAPI;

public class GameManager {

    private final RunnableManager runnableManager;
    private final BombManager bombManager;
    private final ConfigManager configManager;
    private final PacTNTMan plugin;
    private final TimerManager timerManager;
    private final HUDManager hudManager;

    public GameManager(RunnableManager runnableManager, BombManager bombManager, ConfigManager configManager, PacTNTMan plugin, TimerManager timerManager, HUDManager hudManager) {
        this.runnableManager = runnableManager;
        this.bombManager = bombManager;
        this.configManager = configManager;
        this.plugin = plugin;
        this.timerManager = timerManager;
        this.hudManager = hudManager;
    }

    public enum GameState {
        RUNNING,
        STOPPED
    }

    private GameState currentState = GameState.STOPPED;

    public void startGame() {
        currentState = GameState.RUNNING;
        runnableManager.startTask();

        int gameDuration = plugin.getConfig().getInt("settings.game_duration");
        timerManager.createTimer("game", gameDuration);
        hudManager.startHUDTask();

        String bossbar = configManager.getMessageConfig().getString("bossbar.game");
        XBossBarAPI.createBossBar("void", " ", BarColor.YELLOW, BarStyle.SOLID, false);
        XBossBarAPI.createBossBar("pactntman", bossbar, BarColor.YELLOW, BarStyle.SOLID, false);
        XBossBarAPI.createBossBar("pactntman_bombtimer", "%pactntman_bombtimer%", BarColor.YELLOW, BarStyle.SOLID, true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            XBossBarAPI.addPlayerToBossBar("void", p);
            XBossBarAPI.addPlayerToBossBar("pactntman", p);
            XBossBarAPI.addPlayerToBossBar("pactntman_bombtimer", p);
        }
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        timerManager.stopTimer("game");
        timerManager.removeTimer("game");
        XBossBarAPI.removeBossBar("pactntman_bombtimer");
        XBossBarAPI.removeBossBar("pactntman");
        XBossBarAPI.removeBossBar("void");
        hudManager.stopHUDTask();
        bombManager.resetBombs();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isGlowing()) {
                p.setGlowing(false);
            }
            timerManager.stopTimer(p.getName());
            timerManager.removeTimer(p.getName());
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }
}

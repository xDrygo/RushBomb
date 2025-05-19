package org.eldrygo.RTNTTag.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.eldrygo.RTNTTag.Lib.Time.Managers.TimerManager;
import org.eldrygo.RTNTTag.Plugin.Managers.ConfigManager;
import org.eldrygo.RTNTTag.RTNTTag;
import org.eldrygo.XBossBar.API.XBossBarAPI;

public class GameManager {
    private final BombManager bombManager;
    private final RunnableManager runnableManager;
    private final TimerManager timerManager;
    private final RTNTTag plugin;
    private final ConfigManager configManager;
    private final HUDManager hudManager;

    public GameManager(BombManager bombManager, RunnableManager runnableManager, TimerManager timerManager, RTNTTag plugin, ConfigManager configManager, HUDManager hudManager) {
        this.bombManager = bombManager;
        this.runnableManager = runnableManager;
        this.timerManager = timerManager;
        this.plugin = plugin;
        this.configManager = configManager;
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

        String bossbar = configManager.getMessageConfig().getString("bossbar.game");
        XBossBarAPI.createBossBar("void", " ", BarColor.YELLOW, BarStyle.SOLID, false);
        XBossBarAPI.createBossBar("rtnttag", bossbar, BarColor.YELLOW, BarStyle.SOLID, false);
        hudManager.startHUDTask();
        for (Player p : Bukkit.getOnlinePlayers()) {
            XBossBarAPI.addPlayerToBossBar("void", p);
            XBossBarAPI.addPlayerToBossBar("rtnttag", p);
        }
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        killLosers();
        timerManager.stopTimer("game");
        timerManager.removeTimer("game");
        XBossBarAPI.removeBossBar("rtnttag");
        XBossBarAPI.removeBossBar("void");
        bombManager.resetBombs();
        hudManager.stopHUDTask();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void resetAll() {
        stopGame();
        bombManager.resetBombs();
    }

    public void killLosers() {
        for (Player p : bombManager.getPlayersWithBomb()) {
            p.damage(100000.0);
        }
    }
}

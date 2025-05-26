package org.eldrygo.GoldenBomb.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Lib.Time.Managers.TimerManager;
import org.eldrygo.GoldenBomb.Plugin.Managers.ConfigManager;
import org.eldrygo.XBossBar.API.XBossBarAPI;

public class GameManager {
    private final BombManager bombManager;
    private final RunnableManager runnableManager;
    private final TimerManager timerManager;
    private final GoldenBomb plugin;
    private final ConfigManager configManager;
    private final HUDManager hudManager;

    public GameManager(BombManager bombManager, RunnableManager runnableManager, TimerManager timerManager, GoldenBomb plugin, ConfigManager configManager, HUDManager hudManager) {
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
        XBossBarAPI.createBossBar("goldenbomb", bossbar, BarColor.YELLOW, BarStyle.SOLID, false);
        hudManager.startHUDTask();
        for (Player p : Bukkit.getOnlinePlayers()) {
            XBossBarAPI.addPlayerToBossBar("void", p);
            XBossBarAPI.addPlayerToBossBar("goldenbomb", p);
        }
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        killLosers();
        timerManager.stopTimer("game");
        timerManager.removeTimer("game");
        XBossBarAPI.removeBossBar("goldenbomb");
        XBossBarAPI.removeBossBar("void");
        bombManager.resetBombs();
        hudManager.stopHUDTask();
    }

    public void cancelGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        timerManager.stopTimer("game");
        timerManager.removeTimer("game");
        XBossBarAPI.removeBossBar("goldenbomb");
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
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!bombManager.getPlayersWithBomb().contains(p) && !p.isOp()) {
                p.damage(100000.0);
            }
        }
    }
}
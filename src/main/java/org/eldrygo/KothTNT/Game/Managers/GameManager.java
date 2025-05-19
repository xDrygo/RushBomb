package org.eldrygo.KothTNT.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;
import org.eldrygo.KothTNT.Lib.Time.Managers.TimerManager;
import org.eldrygo.KothTNT.Plugin.Managers.ConfigManager;
import org.eldrygo.XBossBar.API.XBossBarAPI;

public class GameManager {
    private final BombManager bombManager;
    private final RunnableManager runnableManager;
    private final TimerManager timerManager;
    private final KothTNT plugin;
    private final ConfigManager configManager;
    private final HUDManager hudManager;
    private final PointsManager pointsManager;

    private int playersToKill;

    public GameManager(BombManager bombManager, RunnableManager runnableManager, TimerManager timerManager, KothTNT plugin, ConfigManager configManager, HUDManager hudManager, PointsManager pointsManager) {
        this.bombManager = bombManager;
        this.runnableManager = runnableManager;
        this.timerManager = timerManager;
        this.plugin = plugin;
        this.configManager = configManager;
        this.hudManager = hudManager;
        this.pointsManager = pointsManager;
    }

    public enum GameState {
        RUNNING,
        STOPPED
    }

    private GameState currentState = GameState.STOPPED;

    public void startGame(int playersToKill) {
        currentState = GameState.RUNNING;
        runnableManager.startTask();
        pointsManager.resetAllPoints();
        this.playersToKill = playersToKill;

        int gameDuration = plugin.getConfig().getInt("settings.game_duration");
        timerManager.createTimer("game", gameDuration);

        String bossbar = configManager.getMessageConfig().getString("bossbar.game");
        XBossBarAPI.createBossBar("void", " ", BarColor.YELLOW, BarStyle.SOLID, false);
        XBossBarAPI.createBossBar("kothtnt", bossbar, BarColor.YELLOW, BarStyle.SOLID, false);
        XBossBarAPI.createBossBar("kothtnt_points", "%kothtnt_points%", BarColor.YELLOW, BarStyle.SOLID, true);
        hudManager.startHUDTask();
        for (Player p : Bukkit.getOnlinePlayers()) {
            XBossBarAPI.addPlayerToBossBar("void", p);
            XBossBarAPI.addPlayerToBossBar("kothtnt", p);
            XBossBarAPI.addPlayerToBossBar("kothtnt_points", p);
        }
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        killLosers();
        timerManager.stopTimer("game");
        timerManager.removeTimer("game");
        XBossBarAPI.removeBossBar("kothtnt_points");
        XBossBarAPI.removeBossBar("kothtnt");
        XBossBarAPI.removeBossBar("void");
        bombManager.resetBombs();
        hudManager.stopHUDTask();
        playersToKill = 0;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void resetAll() {
        stopGame();
        bombManager.resetBombs();
    }

    public void killLosers() {
        for (Player p : pointsManager.getLowestPlayers(playersToKill)) {
            if (p.isOp()) continue;
            p.damage(100000.0);
        }
    }
}
package ftsos.skywars.task;

import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.scoreboard.ScoreboardSkywars;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunTask extends BukkitRunnable {
    private GameDefinition game;
    private int startInt = 10;

    public GameRunTask(GameDefinition game) {
        this.game = game;
        this.game.setState(GameDefinition.GameState.PREPARATION);
        this.game.assignSpawnPositions();
        this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Has sido teletransportado");
        this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego comienza en " + this.startInt + " segundos");
        this.game.setMovementFrozen(true);
    }

    public void run() {
        if (startInt <= 1) {
            this.cancel();
            this.game.setState(GameDefinition.GameState.ACTIVE);
            this.game.setScoreboard(new ScoreboardSkywars(game));
            this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego ha comenzado");
            this.game.setMovementFrozen(false);
        } else {
            startInt -= 1;
            this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego comenzara en " + startInt + " segundo" + (startInt == 1 ? "" : "s"));
        }
    }
}

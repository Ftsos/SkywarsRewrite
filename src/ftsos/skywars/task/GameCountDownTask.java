package ftsos.skywars.task;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountDownTask extends BukkitRunnable {

    private int time = 20;
    private GameDefinition game;
    public GameCountDownTask(GameDefinition game) {
        this.game = game;
    }
    public void run() {
        time -= 1;

        if(time == 0) {
            game.getLobbyEditSession().undo(game.getLobbyEditSession());
            for (GamePlayer gamePlayer : game.getPlayers()){
                gamePlayer.getPlayer().setFoodLevel(20);
            }
            cancel();

            new GameRunTask(game).runTaskTimer(Skywars.getInstance(), 0, 20);
        } else {
            if (time == 15 || time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Seras teletransportado a el juego en " + time + " segundos");
            }
        }
    }
}

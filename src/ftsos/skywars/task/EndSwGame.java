package ftsos.skywars.task;

import ftsos.skywars.objects.GameDefinition;
import org.bukkit.scheduler.BukkitRunnable;

public class EndSwGame extends BukkitRunnable {
    private GameDefinition game;
    public EndSwGame(GameDefinition gm) {
        this.game = gm;
    }

    @Override
    public void run() {
        try {

            game.finishGame(game.getWinner());

        } catch (Exception ig) {

        }
    }
}

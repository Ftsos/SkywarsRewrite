package ftsos.skywars.task;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidKill extends BukkitRunnable {
    private GameDefinition game;

    public VoidKill(GameDefinition game) {
        this.game = game;
    }

    @Override
    public void run() {
        if(game.isState(GameDefinition.GameState.ACTIVE)){
            for (GamePlayer gamePlayer : this.game.getPlayers()){
                Player playerDied = gamePlayer.getPlayer();
                if (playerDied.getLocation().getY() <= 0) {
                    game.sendMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");
                    game.sendSpectatorMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");

                    game.activateSpectatorSettings(playerDied);


                    if(game.getPlayers().size() <= 1 && !game.isState(GameDefinition.GameState.ENDING)){
                        game.setState(GameDefinition.GameState.ENDING);
                        try {
                            game.setWinner(game.getPlayers().get(0));
                            new EndSwGame(game).runTaskLater(Skywars.getInstance(), 100);
                        } catch (Exception ignored){

                        }

                    }
                }
            }
        } else if (game.isState(GameDefinition.GameState.ENDING)){
            game.getWinner().getPlayer().teleport(game.getLobbyPoint());

        } else {
            cancel();
        }
    }
}

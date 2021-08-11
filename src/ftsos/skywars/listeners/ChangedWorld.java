package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.task.EndSwGame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorld implements Listener {
    @EventHandler
    public void changedWorld(PlayerChangedWorldEvent event) {
        Player playerQuit = event.getPlayer();
        if(playerQuit != null && Skywars.getInstance().getGame(playerQuit) != null && Skywars.getInstance().getGame(playerQuit).getGameState() == GameDefinition.GameState.ACTIVE){
            //done tengo q hacer lo de corroborar si se encuentra en alguna partida

            GameDefinition game = Skywars.getInstance().getGame(playerQuit);

            game.sendMessage(ChatColor.BOLD + playerQuit.getName() + ChatColor.YELLOW + " Ha Muerto");
            game.sendSpectatorMessage(ChatColor.BOLD + playerQuit.getName() + ChatColor.YELLOW + " Ha Muerto");

            game.activateSpectatorSettings(playerQuit);

            if(game.isState(GameDefinition.GameState.ENDING)){
                playerQuit.teleport(game.getLobbyPoint());
            }
            if(game.getPlayers().size() <= 1 && !game.isState(GameDefinition.GameState.ENDING)){
                game.setState(GameDefinition.GameState.ENDING);
                try {
                    game.setWinner(game.getPlayers().get(0));
                    game.getSpectators().forEach((GamePlayer spectator) -> {
                        spectator.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "La partida ha finalizado", game.getWinner().getName() + " Gano esta partida");
                    });

                    game.getWinner().getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ganaste!", game.getWinner().getName() + " Gano esta partida");

                    new EndSwGame(game).runTaskLater(Skywars.getInstance(), 100);
                } catch (Exception ignored){
                    ignored.printStackTrace();
                }

            }
            /*new CheckPlayerNumber(game).runTaskTimer(Skywars.getInstance(), 0, 200);
            new PlayersRealTime(game).runTaskTimer(Skywars.getInstance(), 0, 20);*/
        }
    }
}

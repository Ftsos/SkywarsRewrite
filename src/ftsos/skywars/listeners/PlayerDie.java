package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.task.CheckPlayerNumber;
import ftsos.skywars.task.PlayersRealTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDie implements Listener {
    private Skywars plugin;

    public PlayerDie(Skywars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e){
        Player playerDied = (Player) e.getEntity();
        if(playerDied != null && Skywars.getInstance().getGame(playerDied) != null && Skywars.getInstance().getGame(playerDied).getGameState() == GameDefinition.GameState.ACTIVE){
            //todo tengo q hacer lo de corroborar si se encuentra en alguna partida

            GameDefinition game = Skywars.getInstance().getGame(playerDied);

            game.sendMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");
            game.sendSpectatorMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");

            game.activateSpectatorSettings(playerDied);
            e.setDeathMessage("");
            new CheckPlayerNumber(game).runTaskTimer(Skywars.getInstance(), 0, 200);
            new PlayersRealTime(game).runTaskTimer(Skywars.getInstance(), 0, 20);
        }
    }
}

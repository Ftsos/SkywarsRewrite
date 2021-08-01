package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorld implements Listener {
    @EventHandler
    public void changedWorld(PlayerChangedWorldEvent event) {
        if(Skywars.getInstance().getGame(event.getPlayer()) != null) {
            GameDefinition game = Skywars.getInstance().getGame(event.getPlayer());
            GamePlayer gamePlayer = game.getGamePlayer(event.getPlayer());
            game.leave(gamePlayer);
        }
    }
}

package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHit implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){

        if(event.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();

            if(Skywars.getInstance().getGame(player) != null){
                GameDefinition game = Skywars.getInstance().getGame(player);
                if(game.isState(GameDefinition.GameState.STARTING) || game.isState(GameDefinition.GameState.PREPARATION) || game.isState(GameDefinition.GameState.LOBBY)){
                    event.setCancelled(true);
                }
            }
        }

    }
}

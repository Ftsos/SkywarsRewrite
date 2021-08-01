package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHit implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){

        if(event.getDamager().getType() == EntityType.PLAYER){
            Player player = (Player) event.getDamager();
           // Bukkit.broadcastMessage(player.getPlayer().getName() + " le pego a " + event.getEntity().getName());
            if(Skywars.getInstance().getGame(player) != null){
                GameDefinition game = Skywars.getInstance().getGame(player);
                //Bukkit.broadcastMessage(player.getPlayer().getName() + " le pego a " + event.getEntity().getName() + " estando en un juego: " + game.getDisplayName() + " modo:" + game.getGameState().toString());

                if(game.isState(GameDefinition.GameState.STARTING) || game.isState(GameDefinition.GameState.PREPARATION) || game.isState(GameDefinition.GameState.LOBBY)){
                   // Bukkit.broadcastMessage(player.getPlayer().getName() + " le pego a " + event.getEntity().getName() + " estando en el lobby de un juego" );
                    event.setCancelled(true);
                }
            }
        }

    }
}

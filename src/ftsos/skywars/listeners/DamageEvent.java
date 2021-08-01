package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamageEvent(EntityDamageEvent e){
            if(!(e.getEntity() instanceof Player)) return;
                Player player = (Player) e.getEntity();

                //if (!((player.getHealth() - e.getFinalDamage()) <= 0)) return;
                if(Skywars.getInstance().getGame(player) == null) return;
                GameDefinition game = Skywars.getInstance().getGame(player);

                if(!game.isState(GameDefinition.GameState.ENDING)) return;

                e.setCancelled(true);





    }



    }



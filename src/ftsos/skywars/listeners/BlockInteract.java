package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteract implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        handle(e, player);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        Player player = e.getPlayer();
        handle(e, player);
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        handle(e, player);
    }

    public void handle(Cancellable e, Player player){
        if(Skywars.getInstance().getGame(player) != null){
            GameDefinition game = Skywars.getInstance().getGame(player);
            if(game.isMovementFrozen()){
                e.setCancelled(true);
            }
        }
    }
}

package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.objects.SwPlayer;
import ftsos.skywars.store.selectors.impl.CageSelector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = player.getItemInHand();
        Skywars.getInstance().eventHandler.handle(event);
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         //   Skywars.getInstance().getServer().getLogger().info("Skywars> Right Click");
            //xd

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "Selector De Cages") && item.getType() == Material.PAPER) {
                //display Cage Selector
              //  Skywars.getInstance().getServer().getLogger().info("Skywars> Right Click Paper");

                try {
                  // gPlayer.addOwnedCage(0);
                    for (int i = 0; i < Skywars.getInstance().players.size(); i++){
                        SwPlayer playerSw = Skywars.getInstance().players.get(i);
                        if(playerSw.getPlayer().getUniqueId().equals(player.getUniqueId())){
                            CageSelector cageSelector = new CageSelector(playerSw);
                            cageSelector.displayGui();
                            break;
                        }
                    }


               } catch (Exception ex) {
                   ex.printStackTrace();
               }
            }


        }
    }
}

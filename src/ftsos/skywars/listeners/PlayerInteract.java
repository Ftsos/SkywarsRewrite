package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GamePlayer;
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
    //TODO PLS DO A REFACTOR PLS A LIL OF THIS CODE IS REALLY DIFFICULT TO UNDERSTAND
    //TODO add a handler
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = player.getItemInHand();
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
         //   Skywars.getInstance().getServer().getLogger().info("Skywars> Right Click");
            //xd

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + "Selector De Cages") && item.getType() == Material.PAPER) {
                //display Cage Selector
              //  Skywars.getInstance().getServer().getLogger().info("Skywars> Right Click Paper");

                try {
                   GamePlayer gPlayer = Skywars.getInstance().getGame(player).getGamePlayer(player);
                  // gPlayer.addOwnedCage(0);

                   CageSelector cageSelector = new CageSelector(gPlayer);
                   cageSelector.displayGui();

               } catch (Exception ex) {
                   ex.printStackTrace();
               }
            }

            if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Select Chest") && item.getType() == Material.BLAZE_ROD) {

                //Set Chest
                String mapName = Skywars.getInstance().adminGuiManager.getMap();
                FileConfiguration config = Skywars.getInstance().getConfig();
                if(mapName != null){
               // config.set("mapas." + mapName + ".chests", p);
                    if(config.contains("mapas." + mapName + ".chests")){
                        List<String> maps = config.getStringList("mapas." + mapName + ".chests");
                        maps.add(player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
                        config.set("mapas." + mapName + ".chests", maps);
                    } else {
                        List<String> mapsNew = new ArrayList<String>();
                        mapsNew.add(player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
                        config.set("mapas." + mapName + ".chests", mapsNew);
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Epaaaaaa, primero debes hacer /swadminkit <MapName>"));
                }
            }
        }
    }
}

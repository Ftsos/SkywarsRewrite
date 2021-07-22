package ftsos.skywars.admin;

import ftsos.skywars.Skywars;
import ftsos.skywars.listeners.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdminKitListener extends EventListener {

    public void handle(Cancellable event2) {
        if (event2 instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) event2;
            Player player = event.getPlayer();
            Action action = event.getAction();
            ItemStack item = player.getItemInHand();
            setChest(event, player, action, item);
            setMap(event, player, action, item);
            setSpawn(event, player, action, item);
            setItems(event, player, action, item);

        }
    }

    public void setChest(PlayerInteractEvent event, Player player, Action action, ItemStack item){
        if(action == Action.LEFT_CLICK_BLOCK) {
            if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
            if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Select Chest") && item.getType() == Material.BLAZE_ROD) {
                //Set Chest
                String mapName = Skywars.getInstance().adminGuiManager.getMap();
                FileConfiguration config = Skywars.getInstance().getConfig();
                if (mapName != null) {

                    if (config.contains("mapas." + mapName + ".chests")) {
                        List<String> maps = config.getStringList("mapas." + mapName + ".chests");
                        maps.add(player.getLocation().getWorld().getName() + "," + event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + event.getClickedBlock().getLocation().getZ());
                        config.set("mapas." + mapName + ".chests", maps);
                    } else {
                        List<String> mapsNew = new ArrayList<String>();
                        mapsNew.add(player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
                        config.set("mapas." + mapName + ".chests", mapsNew);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Chest seteado correctamente"));

                    event.setCancelled(true);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Epaaaaaa, primero debes hacer /swadminkit <MapName>"));
                }
            }
            }
        }

    }

    public void setMap(PlayerInteractEvent event, Player player, Action action, ItemStack item){
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){

                if (item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Set Map") && item.getType() == Material.BOOK){
                    String mapName = Skywars.getInstance().adminGuiManager.getMap();
                    FileConfiguration config = Skywars.getInstance().getConfig();
                    //set Map
                    if (mapName != null) {
                            config.set("mapas." + mapName + ".worldName", player.getLocation().getWorld().getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Mapa seteado correctamente"));

                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Epaaaaaa, primero debes hacer /swadminkit <MapName>"));
                    }
                }
            }
        }
    }

    public void setSpawn(PlayerInteractEvent event, Player player, Action action, ItemStack item) {
        if(action == Action.LEFT_CLICK_BLOCK){
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Set Spawn") && item.getType() == Material.BED){
                    String mapName = Skywars.getInstance().adminGuiManager.getMap();
                    FileConfiguration config = Skywars.getInstance().getConfig();
                    //set Spawn
                    if (mapName != null) {
                        if (config.contains("mapas." + mapName + ".spawns")) {
                            List<String> spawns = config.getStringList("mapas." + mapName + ".spawns");
                            spawns.add(player.getLocation().getWorld().getName() + "," + event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + event.getClickedBlock().getLocation().getZ());
                            config.set("mapas." + mapName + ".spawns", spawns);
                        } else {
                            List<String> spawns = new ArrayList<String>();
                            spawns.add(player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
                            config.set("mapas." + mapName + ".spawns", spawns);
                        }
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Spawn seteado correctamente"));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Epaaaaaa, primero debes hacer /swadminkit <MapName>"));
                    }
                }
            }
        }
    }

    public void setItems(PlayerInteractEvent event, Player player, Action action, ItemStack item){
        //TODO
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if (item.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Set Normal Items Chest") && item.getType() == Material.BOOKSHELF){
                    Inventory inventory = Bukkit.createInventory(null, 54, "Items");
                    player.openInventory(inventory);
                }
            }
        }
    }
}

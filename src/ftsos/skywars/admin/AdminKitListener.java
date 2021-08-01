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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            saveConfig(event, player, action, item);

        }
        if(event2 instanceof InventoryClickEvent){
            InventoryClickEvent event = (InventoryClickEvent) event2;
            handleInventoryClick(event);
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
                            spawns.add(event.getClickedBlock().getLocation().getX() + "," + event.getClickedBlock().getLocation().getY() + "," + event.getClickedBlock().getLocation().getZ());
                            config.set("mapas." + mapName + ".spawns", spawns);
                        } else {
                            List<String> spawns = new ArrayList<String>();
                            spawns.add(player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ());
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

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if (item.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Set Normal Items Chest") && item.getType() == Material.BOOKSHELF) {
                    Inventory inventory = Bukkit.createInventory(null, 54, "Items");
                    //inventory.addItem()
                    FileConfiguration config = Skywars.getInstance().getConfig();
                    String mapName = Skywars.getInstance().adminGuiManager.getMap();
                    if (mapName != null) {

                        if(config.contains("mapas." + Skywars.getInstance().adminGuiManager.getMap() + ".normalItems") && !config.getStringList("mapas." + Skywars.getInstance().adminGuiManager.getMap() + ".normalItems").isEmpty()) {

                            for (String itemString : config.getStringList("mapas." + Skywars.getInstance().adminGuiManager.getMap() + ".normalItems")) {
                                try {
                                    ItemStack itemToAdd = new ItemStack(Material.valueOf(itemString));
                                    ItemMeta metaItemToAdd = itemToAdd.getItemMeta();
                                    metaItemToAdd.setDisplayName(ChatColor.YELLOW + "Item> " + itemToAdd.getType().name());
                                    inventory.addItem();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ESkywars> &8No hay ningun item, :C, pero igual puedes anadir uno :)"));
                        }
                        player.openInventory(inventory);


                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Epaaaaaa, primero debes hacer /swadminkit <MapName>"));
                    }
                }
            }
        }
    }

    public void handleInventoryClick(InventoryClickEvent event){
        //TODO
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if(!inventory.getTitle().equals("Items")) return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null) return;

        if(clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName() && clickedItem.getItemMeta().getDisplayName().contains(ChatColor.YELLOW + "Item> ")){

        }
    }

    public void saveConfig(PlayerInteractEvent event, Player player, Action action, ItemStack item){
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if (item.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Save") && item.getType() == Material.ENDER_PEARL) {
                    Skywars.getInstance().saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSkywars> &8Configuracion salvada"));
                }
            }
        }
    }
}

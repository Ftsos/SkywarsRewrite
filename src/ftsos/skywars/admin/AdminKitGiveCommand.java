package ftsos.skywars.admin;

import ftsos.skywars.Skywars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminKitGiveCommand implements CommandExecutor {
    private Skywars plugin;

    public AdminKitGiveCommand(Skywars plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Skywars> " + ChatColor.YELLOW + "No Puedes ejecutar este comando desde la consola");
            return false;
        } else {
            Player player = (Player) sender;
            if(player.hasPermission("skywars.adminkit")){
                if(args.length != 0) {
                    //give kit
                    player.setGameMode(GameMode.CREATIVE);

                    //SelectChest
                    ItemStack blazeChestRod = new ItemStack(Material.BLAZE_ROD, 1);
                    ItemMeta metaBlazeChestRod = blazeChestRod.getItemMeta();
                    metaBlazeChestRod.setDisplayName(ChatColor.RED + "Select Chest");
                    blazeChestRod.setItemMeta(metaBlazeChestRod);
                    player.getInventory().addItem(blazeChestRod);

                    //SetMap
                    ItemStack setMapBook = new ItemStack(Material.BOOK, 1);
                    ItemMeta metaSetMapBook = setMapBook.getItemMeta();
                    metaSetMapBook.setDisplayName(ChatColor.YELLOW + "Set Map");
                    setMapBook.setItemMeta(metaSetMapBook);
                    player.getInventory().addItem(setMapBook);

                    //SetSpawn
                    ItemStack setSpawnBed = new ItemStack(Material.BED, 1);
                    ItemMeta metaSetSpawnBed = setSpawnBed.getItemMeta();
                    metaSetSpawnBed.setDisplayName(ChatColor.GREEN + "Set Spawn");
                    setSpawnBed.setItemMeta(metaSetSpawnBed);
                    player.getInventory().addItem(setSpawnBed);


                    player.sendMessage(ChatColor.YELLOW + "Skywars> " + ChatColor.RESET + "Given a admin kit");

                    //set map name
                    this.plugin.adminGuiManager.setMap(args[0]);
                } else {
                    player.sendMessage(ChatColor.RED + "Skywars> " + ChatColor.DARK_GRAY + "Usage: /swadminkit <MapName>");
                }
                return true;
            } else {
                player.sendMessage("mensaje de unknown command xD");
                return false;
            }
        }
    }

}

/*
 * Copyright (c) 2021. Please give me credit, -Ftsos
 */

package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.SwPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick( InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

       // Skywars.getInstance().getLogger().info();
        //if(!Skywars.getInstance().cageManager.invList.containsKey(p.getUniqueId())) return;

        //if (e.getInventory() != Skywars.getInstance().cageManager.invList.get(p.getUniqueId())) return;

       if (e.getInventory().getTitle().equals("Selector de Cages"))

        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null) return;

        if(clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName() && clickedItem.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cClose"))){
            p.closeInventory();
        }

        Skywars.getInstance().eventHandler.handle(e);

        // Using slots click is a best option for your inventory click's
        //p.sendMessage("You clicked at slot " + e.getRawSlot());

        //Select That cage as default
        //TODO
        if(!clickedItem.hasItemMeta()) return;
        if(!clickedItem.getItemMeta().hasDisplayName()) return;
        if(clickedItem.getItemMeta().getDisplayName().contains(ChatColor.YELLOW + "Item> ")){
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(clickedItem);
            if(!nmsItem.hasTag()) return;
            NBTTagCompound itemCompound = nmsItem.getTag();
            int cageNumber = itemCompound.getInt("cage");

            if(!Skywars.getInstance().cageManager.cages.contains(cageNumber)) {
                Skywars.getInstance().getLogger().severe(ChatColor.RED + "ERROR!");
            }

            for(SwPlayer swPlayer : Skywars.getInstance().players){
                if(swPlayer.getPlayer().getUniqueId() != p.getUniqueId()) continue;
                swPlayer.setCageIndex(cageNumber);
            }
        }

    }
}

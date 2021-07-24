package ftsos.skywars.store.selectors.impl;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.Cage;
import ftsos.skywars.cage.CageManager;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.store.itemShop.ItemShop;
import ftsos.skywars.store.selectors.Selector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CageSelector extends Selector {

    public CageManager cageManager = Skywars.getInstance().cageManager;
    private GamePlayer player;
    private List<ItemStack> invIcons = new ArrayList<ItemStack>();
    private List<Cage> cages = new ArrayList<Cage>();

    public CageSelector(GamePlayer p){
        this.player = p;
    }

    public void displayGui(){
        for (int itShop : this.player.ownedCages){
            invIcons.add(Skywars.getInstance().cageManager.cages.get(itShop).icon);
            this.cages.add(Skywars.getInstance().cageManager.cages.get(itShop));
        }
        Inventory inv = Bukkit.createInventory(null, 54, "Selector de Cages");
        for (int i = 0; i < invIcons.size(); i++){
            ItemStack item = invIcons.get(i);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(cages.get(i).name);
            inv.setItem(i, item);
        }
        ItemStack closeStainedRedClay = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta metaCloseStainedRedClay = closeStainedRedClay.getItemMeta();
        metaCloseStainedRedClay.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose"));
        inv.setItem(53, new ItemStack(Material.STAINED_CLAY, 1, (short) 14));
        this.player.getPlayer().openInventory(inv);
        this.cageManager.invList.put(player.getPlayer().getUniqueId(), inv);

    }




}

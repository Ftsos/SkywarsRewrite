package ftsos.skywars.store.selectors.impl;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.CageManager;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.store.itemShop.ItemShop;
import ftsos.skywars.store.selectors.Selector;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CageSelector extends Selector {

    public CageManager cageManager = Skywars.getInstance().cageManager;
    private GamePlayer player;
    private List<ItemStack> invIcons;

    public CageSelector(GamePlayer p){
        this.player = p;
    }

    public void displayGui(){
        for (int itShop : this.player.ownedCages){
            invIcons.add(Skywars.getInstance().cageManager.cages.get(itShop).icon);
        }
        Inventory inv = Bukkit.createInventory(null, 53, "Selector de Cages");
        for (int i = 0; i < invIcons.size(); i++){
            ItemStack item = invIcons.get(i);
            inv.setItem(i, item);
        }

        this.player.getPlayer().openInventory(inv);

    }




}

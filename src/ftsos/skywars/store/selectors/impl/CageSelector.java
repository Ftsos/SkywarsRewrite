package ftsos.skywars.store.selectors.impl;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.Cage;
import ftsos.skywars.cage.CageManager;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.objects.SwPlayer;
import ftsos.skywars.store.itemShop.ItemShop;
import ftsos.skywars.store.selectors.Selector;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class CageSelector extends Selector {

    public CageManager cageManager = Skywars.getInstance().cageManager;
    private SwPlayer player;
    private List<ItemStack> invIcons = new ArrayList<ItemStack>();
    private List<Cage> cages = new ArrayList<Cage>();

    public CageSelector(SwPlayer p){
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
            meta.setDisplayName(ChatColor.YELLOW + "Cage> " + cages.get(i).name);
            item.setItemMeta(meta);
            net.minecraft.server.v1_8_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
            NBTTagCompound itemCompound = (itemNms.hasTag()) ? itemNms.getTag() : new NBTTagCompound();
            itemCompound.set("cage", new NBTTagInt(this.player.ownedCages.get(i)));
            itemNms.setTag(itemCompound);
            ItemStack itemStack = CraftItemStack.asBukkitCopy(itemNms);

            inv.setItem(i, itemStack);
        }
        ItemStack closeStainedRedClay = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta metaCloseStainedRedClay = closeStainedRedClay.getItemMeta();
        metaCloseStainedRedClay.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose"));
        closeStainedRedClay.setItemMeta(metaCloseStainedRedClay);
        inv.setItem(53, closeStainedRedClay);
        this.player.getPlayer().openInventory(inv);
        this.cageManager.invList.put(player.getPlayer().getUniqueId(), inv);

    }




}

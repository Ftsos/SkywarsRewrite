package ftsos.skywars.store.itemShop;


import org.bukkit.inventory.ItemStack;

public class ItemShop {
    public int price;
    public ItemStack icon;
    public ItemShop(int price, ItemStack i){
        this.price = price;
        this.icon = i;
    }
}

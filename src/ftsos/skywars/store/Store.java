package ftsos.skywars.store;

import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.store.categoryShop.CategoryShop;
import ftsos.skywars.store.itemShop.ItemShop;

import java.util.List;

public class Store {
    //todo Store
    public List<CategoryShop> categoryShops;

    public boolean buyThing(ItemShop itemToBuyShop, GamePlayer player){

        if(itemToBuyShop.price <= player.money){
            player.addOwnedThing(itemToBuyShop);
            return true;
        } else {
            return false;
        }
    }
}

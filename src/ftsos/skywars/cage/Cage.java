package ftsos.skywars.cage;

import ftsos.skywars.Skywars;
import ftsos.skywars.store.itemShop.ItemShop;
import ftsos.skywars.util.SchematicLoad;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Cage extends ItemShop {

    private File schemFile;
    private String nameConfig;
    public String name;
    public Cage(String nameConfig, int price, ItemStack icon){
        super(price, icon);

        //todo Cages xD, but for the game player

        if(!Skywars.getInstance().getConfig().contains("cages." + nameConfig + ".schemFile")){
            if(Skywars.getInstance().cageManager.cages.contains(this)){
                Skywars.getInstance().cageManager.cages.remove(this);
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.RED + "Skywars> la cage" + this.name + " no se puede activar, revisa la config");
            }
        }

    this.price = Skywars.getInstance().getConfig().getInt("cages." + nameConfig + ".price");
     this.schemFile = new File(Skywars.getInstance().getConfig().getString("cages."  + nameConfig + ".schemFile"));
    }

    public void pasteIt(Location spawnLocation){
        SchematicLoad.loadSchem(this.schemFile, spawnLocation);
    }

    public static boolean isValidCage(Cage cageToReview){
        //Todo
        if(!Skywars.getInstance().getConfig().contains("cages." + cageToReview.nameConfig + ".schemFile")){
            Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.RED + "Skywars> la cage " + cageToReview.name + " no se puede activar, revisa la config");

            return false;

        } else {
            return true;
        }
    }

    public int getPrice() {
        return price;
    }
}

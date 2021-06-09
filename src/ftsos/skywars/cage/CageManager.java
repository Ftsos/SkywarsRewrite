package ftsos.skywars.cage;


import ftsos.skywars.Skywars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class CageManager {
    public List<Cage> cages = new ArrayList<Cage>();
    public CageManager(){
        if(Skywars.getInstance().getConfig().getConfigurationSection("cages") != null){
            for (String cageName : Skywars.getInstance().getConfig().getConfigurationSection("cages").getKeys(false)){
                try {
                    if (Cage.isValidCage(new Cage(cageName, Skywars.getInstance().getConfig().getInt("cages." + cageName + ".price"), new ItemStack(Material.getMaterial(Skywars.getInstance().getConfig().getString("cages." + cageName + ".icon")))))) {
                        cages.add(new Cage(cageName, Skywars.getInstance().getConfig().getInt("cages." + cageName + ".price"), new ItemStack(Material.getMaterial(Skywars.getInstance().getConfig().getString("cages." + cageName + ".icon")))));

                    }
                } catch (Exception ex){
                    Skywars.getInstance().getServer().getLogger().severe(ChatColor.RED + "Skywars> You probably have a mistake in ur config, cages thing, plz Review it XD");
                }
            }
        }
    }
}

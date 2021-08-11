package ftsos.skywars.objects;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.Cage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class SwPlayer {
    public List<Integer> ownedCages;
    public Player player;
    public int cageIndex;
    public SwPlayer(Player tP) {
        this.player = tP;
    }

    public SwPlayer(List<Integer> ownedCages, Player player) {
        this.ownedCages = ownedCages;
        this.player = player;


    }

    public List<Integer> getOwnedCages() {
        return ownedCages;
    }

    public void setOwnedCages(List<Integer> ownedCages) {
        this.ownedCages = ownedCages;
    }

    public void addOwnedCage(Integer ownedCage){
        this.ownedCages.add(ownedCage);
    }
    public Player getPlayer() {
        return player;
    }

    public int getCageIndex() {
        return cageIndex;
    }

    public Cage getSelectedCageIndex() {
        return Skywars.getInstance().cageManager.cages.get(ownedCages.get(cageIndex));
    }

    public boolean setSelectedCageIndex(int toIndex){
        if(ownedCages.contains(toIndex)){
            this.cageIndex = toIndex;
            return true;
        } else {
            return false;
        }

    }
    public void setCageIndex(int cageIndex) {
        this.cageIndex = cageIndex;
    }
}

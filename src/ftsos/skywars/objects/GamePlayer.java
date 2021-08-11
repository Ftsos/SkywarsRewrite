package ftsos.skywars.objects;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.Cage;
import ftsos.skywars.store.itemShop.ItemShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GamePlayer {

    private Player player = null;
    private GameTeam team = null;
    private GamePlayerState gamePlayerState;
    private Location spawnPoint;




    public GamePlayer(Player player) {
        this.player = player;

    }

    public GamePlayer(GameTeam team) {
        this.team = team;
    }

    public boolean isTeamClass() {
        return team != null && player == null;
    }

    public Player getPlayer() {
        return player;
    }

    public GameTeam getTeam() {
        return team;
    }

    public void sendMessage(String message) {
        if (isTeamClass()) {
            getTeam().sendMessage(message);
        } else {
            //done
            //revisa el ChatUtils xD

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void teleport(Location location) {
        if (location == null) {
            return;
        }

        if (!isTeamClass()) {
            getPlayer().teleport(location);
        } else {
            getTeam().teleport(location);
        }
    }

    public String getName() {
        if (isTeamClass()) {
            return getTeam().getName();
        } else {
            return player.getDisplayName();
        }
    }

    public enum GamePlayerState {

    }




}

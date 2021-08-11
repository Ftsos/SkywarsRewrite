package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.SwPlayer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        FileConfiguration config = Skywars.getInstance().getConfig();
        if(!config.contains("players." + player.getUniqueId())) {
            if(Skywars.getInstance().cageManager.cages.isEmpty()) {
                Skywars.getInstance().getLogger().severe(ChatColor.RED + "" + ChatColor.BOLD + "Skywars> add a cage first");
                return;
            }
            Integer[] cages = {0};
            Skywars.getInstance().players.add(new SwPlayer(Arrays.asList(cages), player));
            config.set("players." + player.getUniqueId() + ".cagesOwned", cages);
            Skywars.getInstance().saveConfig();
        }
    }
}

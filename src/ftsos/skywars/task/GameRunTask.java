package ftsos.skywars.task;

import com.sk89q.worldedit.EditSession;
import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.scoreboard.ScoreboardSkywars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GameRunTask extends BukkitRunnable {
    private GameDefinition game;
    private int startInt = 10;
    private List<EditSession> editSessions;
    public GameRunTask(GameDefinition game) {
        this.game = game;
        this.game.setState(GameDefinition.GameState.PREPARATION);
        this.game.assignSpawnPositions();
        this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Has sido teletransportado");
        this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego comienza en " + this.startInt + " segundos");
        this.editSessions = new ArrayList<EditSession>();
        this.game.setMovementFrozen(true);
        if(!Skywars.getInstance().cageManager.cages.isEmpty()){
            for(GamePlayer player : this.game.getPlayers()){

                EditSession es = Skywars.getInstance().cageManager.cages.get(player.getSelectedCageIndex()).pasteIt(player.getPlayer().getLocation());
                editSessions.add(es);
            }
        }

        //adding the selector
        for(GamePlayer gPlayer : this.game.getPlayers()){
            Player player = gPlayer.getPlayer();
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Selector De Cages");
            item.setItemMeta(meta);
            player.getInventory().setItem(1, item);
        }

        new VoidKill(this.game).runTaskTimer(Skywars.getInstance(), 0, 5);
    }

    public void run() {
        if (startInt <= 1) {
            for(GamePlayer gPlayer : this.game.getPlayers()){
                Player player = gPlayer.getPlayer();
                player.getInventory().clear();
            }
            this.cancel();
            this.game.setState(GameDefinition.GameState.ACTIVE);
            this.game.setScoreboard(new ScoreboardSkywars(game));
            this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego ha comenzado");
            this.game.setMovementFrozen(false);
            for(EditSession es : this.editSessions) {
                es.undo(es);
            }
        } else {
            startInt -= 1;
            this.game.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego comenzara en " + startInt + " segundo" + (startInt == 1 ? "" : "s"));
        }
    }
}

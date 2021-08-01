package ftsos.skywars.comandos;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Skywars> " + ChatColor.GRAY + "No puedes ejecutar este comando desde la consola");
            return false;
        }

        Player player = (Player) sender;
        if(Skywars.getInstance().getGame(player) != null){
            GameDefinition game = Skywars.getInstance().getGame(player);
            GamePlayer gamePlayer = game.getGamePlayer(player);
            game.leave(gamePlayer);
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Saliste de tu partida Correctamente");
        }else{
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] No te encuetras en ninguna partida");
        }
        return true;
    }
}

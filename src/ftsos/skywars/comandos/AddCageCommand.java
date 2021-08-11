/*
 * Copyright (c) 2021. Please give me credit, -Ftsos
 */

package ftsos.skywars.comandos;

import ftsos.skywars.Skywars;
import ftsos.skywars.cage.Cage;
import ftsos.skywars.objects.SwPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AddCageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {
            if(sender instanceof Player) {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");
                return false;
            }

            if(args.length != 2) {
                sender.sendMessage("Usage. /addcagecommand <uuidplayer> <cagename>");
                return false;
            }

            String uuidString = args[0];
            UUID uuidPlayer = UUID.fromString(uuidString);
            Player player = Skywars.getInstance().getServer().getPlayer(uuidPlayer);
            if(player == null) {
                sender.sendMessage("That player is not online");
                return false;
            }

            SwPlayer swPlayer = null;
            for (SwPlayer swPlayerLoop : Skywars.getInstance().players){
                if (swPlayerLoop.getPlayer().getUniqueId() == player.getUniqueId()){
                    swPlayer = swPlayerLoop;
                    break;
                }
            }
            if(swPlayer == null) {
                sender.sendMessage("You never has connected");
                return false;
            }

            String cageName = args[1];
            if (cageName == "" || cageName == null) {
                sender.sendMessage("cage name is empty or null");
                return false;
            }

            Cage cage = null;
            for (Cage cageLoop : Skywars.getInstance().cageManager.cages){
                if (cageLoop.name == cageName){
                    cage = cageLoop;
                    break;
                }
            }

            if (cage == null) {
                sender.sendMessage("That cage doesn't exist");
                return false;
            }

            swPlayer.addOwnedCage(Skywars.getInstance().cageManager.cages.indexOf(cage));
            Skywars.getInstance().getServer().getLogger().info(ChatColor.GREEN + "Done!");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Skywars.getInstance().getServer().getLogger().severe(ChatColor.RED + "Error");
            return false;
        }

    }
}

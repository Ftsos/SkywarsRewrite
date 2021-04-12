package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;
import java.util.Set;

public class ChestOpen implements Listener {
    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GameDefinition game = Skywars.getInstance().getGame(player);

        if(game != null && game.getGamePlayer(player) != null){
            if(game.isState(GameDefinition.GameState.LOBBY) || game.isState(GameDefinition.GameState.PREPARATION) || game.isState(GameDefinition.GameState.STARTING)){
                e.setCancelled(true);
                return;
            }

            GamePlayer gamePlayer = game.getGamePlayer(player);

            if(gamePlayer.isTeamClass()) {
                if(gamePlayer.getTeam().isPlayer(player)){
                    handle(e, game);
                }
            } else {
                if (gamePlayer.getPlayer() == player) {
                    handle(e, game);
                }
            }
        }

    }
    //10761
    private void handle(PlayerInteractEvent event, GameDefinition game) {
        if (event.hasBlock() && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();
            if (game.getToFill().contains(chest)){
                if (game.getOpened().contains(chest) || game.getRareItems().size() == 0 || game.getNormalItems().size() == 0) {
                    return;
                }

            chest.getBlockInventory().clear();

            if (new Random().nextFloat() < 0.20) {
                int toFill = new Random().nextInt(8);
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getRareItems().size());
                    chest.getBlockInventory().addItem(game.getRareItems().get(selected));
                }
            } else {
                int toFill = new Random().nextInt(5);
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getNormalItems().size());
                    chest.getBlockInventory().addItem(game.getNormalItems().get(selected));
                }
            }

            game.getOpened().add(chest);
        }else{
                return;
            }
    }
    }

}
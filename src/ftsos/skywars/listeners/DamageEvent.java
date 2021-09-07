package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.task.EndSwGame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamageEvent(EntityDamageEvent e){
            if(!(e.getEntity() instanceof Player)) return;
                Player player = (Player) e.getEntity();

                //if (!((player.getHealth() - e.getFinalDamage()) <= 0)) return;
                if(Skywars.getInstance().getGame(player) == null) return;
                GameDefinition game = Skywars.getInstance().getGame(player);

                if(!game.isState(GameDefinition.GameState.ENDING)) return;

                e.setCancelled(true);





    }

    @EventHandler
    public void lastDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        if (Skywars.getInstance().getGame(player) == null) return;
        if (!((player.getHealth() - e.getFinalDamage()) <= 0)) return;
        GameDefinition game = Skywars.getInstance().getGame(player);

        if (!game.isState(GameDefinition.GameState.ACTIVE)) return;

        e.setCancelled(true);
        Player playerDied = (Player) e.getEntity();
        game.sendMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");
        game.sendSpectatorMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");

        game.activateSpectatorSettings(playerDied);

        if (game.isState(GameDefinition.GameState.ENDING)) {
            playerDied.teleport(game.getLobbyPoint());
        }
        if (game.getPlayers().size() <= 1 && !game.isState(GameDefinition.GameState.ENDING)) {
            game.setState(GameDefinition.GameState.ENDING);
            try {
                game.setWinner(game.getPlayers().get(0));
                game.getSpectators().forEach((GamePlayer spectator) -> {
                    spectator.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "La partida ha finalizado", game.getWinner().getName() + " Gano esta partida");
                });

                game.getWinner().getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ganaste!", game.getWinner().getName() + " Gano esta partida");

                new EndSwGame(game).runTaskLater(Skywars.getInstance(), 100);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

    }

    @EventHandler
    public void fastVoid(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        if (Skywars.getInstance().getGame(player) == null) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.VOID) return;
        GameDefinition game = Skywars.getInstance().getGame(player);

        if (!game.isState(GameDefinition.GameState.ACTIVE)) return;

        e.setCancelled(true);

        Player playerDied = (Player) e.getEntity();
        game.sendMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");
        game.sendSpectatorMessage(ChatColor.BOLD + playerDied.getName() + ChatColor.YELLOW + " Ha Muerto");

        game.activateSpectatorSettings(playerDied);

        if (game.isState(GameDefinition.GameState.ENDING)) {
            playerDied.teleport(game.getLobbyPoint());
        }
        if (game.getPlayers().size() <= 1 && !game.isState(GameDefinition.GameState.ENDING)) {
            game.setState(GameDefinition.GameState.ENDING);
            try {
                game.setWinner(game.getPlayers().get(0));
                game.getSpectators().forEach((GamePlayer spectator) -> {
                    spectator.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "La partida ha finalizado", game.getWinner().getName() + " Gano esta partida");
                });

                game.getWinner().getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ganaste!", game.getWinner().getName() + " Gano esta partida");

                new EndSwGame(game).runTaskLater(Skywars.getInstance(), 100);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

}



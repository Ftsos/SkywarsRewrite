package ftsos.skywars.task;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayersRealTime extends BukkitRunnable {
    private GameDefinition game;
    //private int startInt = 10;

    public PlayersRealTime(GameDefinition game) {
        this.game = game;
        //this.game.setState(GameDefinition.GameState.PREPARATION);
        //this.game.assignSpawnPositions();
        //this.game.sendMessage("[!] Has sido teletransportado");
        //this.game.sendMessage("[!] El juego comienza en " + this.startInt + " segundos");
        //this.game.setMovementFrozen(true);
    }

    public void run(){
        if(this.game.getPlayers().size() <= 1 && game.getGameState() == GameDefinition.GameState.ACTIVE){
            try{
            game.setState(GameDefinition.GameState.ENDING);
            this.game.setWinner(this.game.getPlayers().get(0));
            new CheckPlayerNumber(game).runTaskTimer(Skywars.getInstance(), 0, 20);
            cancel();
            } catch (IndexOutOfBoundsException ingnored) {}
            //this.game.setState(GameDefinition.GameState.ENDING);
            // if(this.gamePlayes)
           // this.game.finishGame(this.game.getPlayers().get(0));
            //this.game.getPlayers().clear();
        } else {
            cancel();
        }
    }
}

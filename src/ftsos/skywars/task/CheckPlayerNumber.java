package ftsos.skywars.task;

import ftsos.skywars.objects.GameDefinition;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckPlayerNumber extends BukkitRunnable {
    private GameDefinition game;
    //private int startInt = 10;

    public CheckPlayerNumber(GameDefinition game) {
        this.game = game;
        //this.game.setState(GameDefinition.GameState.PREPARATION);
        //this.game.assignSpawnPositions();
        //this.game.sendMessage("[!] Has sido teletransportado");
        //this.game.sendMessage("[!] El juego comienza en " + this.startInt + " segundos");
        //this.game.setMovementFrozen(true);
    }

    public void run(){
        if(this.game.getPlayers().size() <= 1){
           // this.game.setState(GameDefinition.GameState.ENDING);
           // if(this.gamePlayes)
            if(this.game.getWinner() != null) {
                this.game.finishGame(this.game.getWinner());
                cancel(); 
            } else {
                cancel();
            }
            //this.game.getPlayers().clear();
        } else {
            cancel();
        }
    }
}

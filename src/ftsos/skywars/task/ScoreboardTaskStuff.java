package ftsos.skywars.task;

import ftsos.skywars.objects.GameDefinition;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardTaskStuff extends BukkitRunnable {

    private int seconds;
    private int minutes;
    private String secondsString;
    private String minutesString;
    private String time;
    private GameDefinition game;
    public ScoreboardTaskStuff(GameDefinition gameFunction) {
        this.game = gameFunction;
    }

    public void run() {
        if(game.isState(GameDefinition.GameState.ACTIVE)){
        seconds = game.getSeconds();
        minutes = game.getMinutes();

        if(seconds == 60){
            minutes++;
            seconds = 0;
        } else {
            seconds++;
        }


        if(String.valueOf(seconds).length() > 1){
            secondsString = String.valueOf(seconds);

        } else {
            secondsString = "0" + seconds;
        }

        if(String.valueOf(minutes).length() > 1){
            minutesString = String.valueOf(minutes);
        } else {
            minutesString = "0" + minutes;
        }

        time = minutesString + ":" + secondsString;
        game.setMinutes(minutes);
        game.setSeconds(seconds);
        game.setTime(time);

        game.getScoreboard().updateTimeScoreboard();

    } else {
           cancel();
        }
    }
}

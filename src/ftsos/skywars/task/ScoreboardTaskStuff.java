package ftsos.skywars.task;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

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
        ArrayList<String> scoreboardText = new ArrayList<String>();
        scoreboardText.add(" ");
        scoreboardText.add("  " + ChatColor.DARK_GREEN + "Time: " + game.getTime() + "  ");
        scoreboardText.add(" ");
        scoreboardText.add("  " + ChatColor.DARK_GREEN + "Players: " + game.getPlayers().size() + "  ");
        scoreboardText.add("  ");
        //todo Scoreboard its backwards start from the last line (ServerIp) and Finish in time xD
        if(Skywars.getInstance().getConfig().contains("serverIp")){
        scoreboardText.add("  " + ChatColor.translateAlternateColorCodes('&', Skywars.getInstance().getConfig().getString("serverIp")) + "  ");
        } else {
            scoreboardText.add("  " + ChatColor.AQUA + "ftsosserver.com" +  "  ");
        }
        game.setScoreboardTexts(scoreboardText);
        game.getScoreboard().updateScoreboard();

    } else {
           cancel();
        }
    }
}

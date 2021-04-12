package ftsos.skywars.scoreboard;

import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardSkywars {

    public void ScoreboardSkywars(GameDefinition game){

    int position = 1;
    for(GamePlayer gamePlayer : game.getPlayers()){
        Player player = gamePlayer.getPlayer();

        scoreboardToPlayer(player, "01:32", position);
        position++;
    }
    }

    public void scoreboardToPlayer(Player player, String time, int position){
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective("Skywars", "dummy", ChatColor.YELLOW + "Skywars");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "Skywars");

        Score score = objective.getScore(ChatColor.GREEN + "Time: " + time);

        score.setScore(position);
        player.setScoreboard(board);
    }

}

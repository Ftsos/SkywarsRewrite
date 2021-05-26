package ftsos.skywars.scoreboard;

import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class ScoreboardSkywars {
    private GameDefinition gameDef;
    private int position = 1;
    //done Change the id and all that stuffs xD

    public ScoreboardSkywars(GameDefinition game){
        if (game != null){
        gameDef = game;
    if(gameDef.isState(GameDefinition.GameState.ACTIVE)){

    for(GamePlayer gamePlayer : gameDef.getPlayers()){
        Player player = gamePlayer.getPlayer();

        //scoreboardTimeToPlayer(player, game.getTime(), position);
        /*ArrayList<String> txtScoreBoard = new ArrayList<String>();
        txtScoreBoard.add()
       scoreboardArrayListTextToPlayer();*/
        //scoreboardTimeToPlayer(player, game.getTime(), position);
        scoreboardArrayListTextToPlayer(gameDef.getScoreboardTexts(),
                player,
                gameDef.getTitleScoreboard(),
                gameDef.getIdScoreboard(),
                gameDef.getDisplaySlot());
    }
    }
    }

    }

    @Deprecated
    public void scoreboardTimeToPlayer(Player player, String time, int position){
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective("Skywars", "dummy", ChatColor.YELLOW + "Skywars");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "Skywars");

        Score score = objective.getScore(ChatColor.GREEN + "Time: " + time);

        score.setScore(position);
        player.setScoreboard(board);
    }

    @Deprecated
    public void updateTimeScoreboard(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective("Skywars", "dummy", ChatColor.YELLOW + "Skywars");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "  Skywars  ");

        Score score = objective.getScore(ChatColor.GREEN + "   Time: " + gameDef.getTime() + "  ");

        score.setScore(position);

        for(GamePlayer gamePlayer : gameDef.getPlayers()) {
            Player player = gamePlayer.getPlayer();

            player.setScoreboard(board);
        }
    }

    public void updateScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();

        //todo problem of the scoreboard is with this
        Objective objective = board.registerNewObjective(gameDef.getIdScoreboard(), "dummy", gameDef.getTitleScoreboard());
        objective.setDisplaySlot(gameDef.getDisplaySlot());
        objective.setDisplayName(gameDef.getTitleScoreboard());

        int currentPosition = gameDef.getScoreboardTexts().size();
        for (String text : gameDef.getScoreboardTexts()) {
            Score score = objective.getScore(text);

            score.setScore(currentPosition);
            currentPosition--;
        }
      //  Score score = objective.getScore(ChatColor.GREEN + "   Time: " + gameDef.getTime() + "  ");

        //score.setScore(position);

        for(GamePlayer gamePlayer : gameDef.getPlayers()) {
            Player player = gamePlayer.getPlayer();

            player.setScoreboard(board);
        }

       /* ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective(id, "dummy", title);
        objective.setDisplaySlot(displaySlot);
        objective.setDisplayName();
        int currentPosition = 0;
        for (String text : texts) {
            Score score = objective.getScore(text);

            score.setScore(currentPosition);
            currentPosition++;
        }



        player.setScoreboard(board);
    }*/
    }

    public void removeScoreboard(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();
        board.getObjectives().forEach((Objective objective) -> {
            objective.unregister();
        });
        for (GamePlayer gamePlayer : gameDef.getPlayers()){
            Player player = gamePlayer.getPlayer();

            player.setScoreboard(board);
        }

        for (GamePlayer gamePlayer : gameDef.getSpectators()){
            Player player = gamePlayer.getPlayer();

            player.setScoreboard(board);
        }
    }

    public void removeScoreboardToPlayer(GamePlayer gamePlayer){
        if(gamePlayer != null){
            Player player = gamePlayer.getPlayer();
            ScoreboardManager manager = Bukkit.getScoreboardManager();

            Scoreboard board = manager.getNewScoreboard();
            board.getObjectives().forEach((Objective objective) -> {
                objective.unregister();
            });
            player.setScoreboard(board);
        }
    }
    public void scoreboardArrayListTextToPlayer(ArrayList<String> texts, Player player, String title, String id, DisplaySlot displaySlot) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective(id, "dummy", title);
        objective.setDisplaySlot(displaySlot);
        //objective.setDisplayName();
        int currentPosition = 0;
        for (String text : texts) {
            Score score = objective.getScore(text);

            score.setScore(currentPosition);
            currentPosition++;
        }



        player.setScoreboard(board);
    }



}

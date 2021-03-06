package ftsos.skywars.objects;

import com.sk89q.worldedit.EditSession;
import com.sun.istack.internal.NotNull;
import ftsos.skywars.RollbackHandler;
import ftsos.skywars.Skywars;
import ftsos.skywars.scoreboard.ScoreboardSkywars;

import ftsos.skywars.task.GameCountDownTask;

import ftsos.skywars.task.ScoreboardTaskStuff;
import ftsos.skywars.task.VoidKill;
import ftsos.skywars.util.SchematicLoad;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameDefinition {
    private Skywars plugin = Skywars.getInstance();


    private String displayName;
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private List<Location> spawnPoints;
    private boolean isTeamGame;
    private Location lobbyPoint;
    private List<ItemStack> normalItems;
    private List<ItemStack> rareItems;
    private String GameName;
    // Active game information
    private GamePlayer winner;
    private List<GamePlayer> players;
    private List<GamePlayer> spectators;
    private GameState gameState = GameState.LOBBY;
    private Map<GamePlayer, Location> gamePlayerToSpawnPoint = new HashMap();
    private Set<Chest> opened;
    private Set<Chest> toFill;

    private int seconds = 0;
    private int minutes = 0;
    private String time = "";

    private ScoreboardSkywars scoreboard;
    private ArrayList<String> scoreboardTexts;
    private String titleScoreboard;
    private String idScoreboard;
    private DisplaySlot displaySlot;

    private World worldBeforeSave;
    private boolean movementFrozen = false;

    private EditSession lobbyEditSession;
    private File lobbySchemFile;

    //private boolean finishing = false;

    public String getTitleScoreboard() {
        return titleScoreboard;
    }

    public void setTitleScoreboard(String titleScoreboard) {
        this.titleScoreboard = titleScoreboard;
    }

    public String getIdScoreboard() {
        return idScoreboard;
    }

    public void setIdScoreboard(String idScoreboard) {
        this.idScoreboard = idScoreboard;
    }

    public DisplaySlot getDisplaySlot() {
        return displaySlot;
    }

    public void setDisplaySlot(DisplaySlot displaySlot) {
        this.displaySlot = displaySlot;
    }

    public ArrayList<String> getScoreboardTexts() {
        return scoreboardTexts;
    }

    public void setScoreboardTexts(ArrayList<String> scoreboardTexts) {
        this.scoreboardTexts = scoreboardTexts;
    }

    public GameDefinition(String gameName) {
        FileConfiguration config = plugin.getConfig();

            this.displayName = config.getString("mapas." + gameName + ".displayName");
            this.maxPlayers = Integer.parseInt(config.getString("mapas." + gameName + ".jugadores"));
            this.minPlayers = Integer.parseInt(config.getString("mapas." + gameName + ".minJugadores"));
            this.GameName = gameName;
            //load the original world
        Skywars.getInstance().getServer().createWorld(new WorldCreator(config.getString("mapas." + gameName + ".worldName")));
        WorldCreator wc = new WorldCreator(config.getString("mapas." + gameName + ".worldName") + "_active");
        wc.copy(Bukkit.getWorld(config.getString("mapas." + gameName + ".worldName")));

        //wc.copy()

        this.worldBeforeSave = Bukkit.createWorld(wc);
        Bukkit.unloadWorld(this.worldBeforeSave, false);
        RollbackHandler.getRollbackHandler().rollback(config.getString("mapas." + gameName + ".worldName"));
        // this.world = Bukkit.getWorld(config.getString("mapas." + gameName + ".worldName") + "_active");
        Skywars.getInstance().getServer().createWorld(new WorldCreator(config.getString("mapas." + gameName + ".worldName") + "_active"));
        this.world = Bukkit.getWorld(config.getString("mapas." + gameName + ".worldName") + "_active");

        try {
            String[] values = config.getString("mapas." + gameName + ".lobbyPoint").split(",");

            double x = Double.parseDouble(values[0]); // X:0 -> X, 0 -> 0
            double y = Double.parseDouble(values[1]);
            double z = Double.parseDouble(values[2]);
            lobbyPoint = new Location(world, x, y, z);
        } catch (Exception ex) {
            Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW +"Hubo un error con el lobby con metadata: " + gameName + ex);
        }
        this.spawnPoints = new ArrayList<>();
        this.scoreboardTexts = new ArrayList<String>();
        for (String point : config.getStringList("mapas." + gameName + ".spawns")) {
            try {
                String[] values = point.split(","); // [X:0, Y:0, Z:0]

                double x = Double.parseDouble(values[0]); // X:0 -> X, 0 -> 0
                double y = Double.parseDouble(values[1]);
                double z = Double.parseDouble(values[2]);

                Location location = new Location(world, x, y, z);
                spawnPoints.add(location);
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + "Error con el metadata del spawnpoint " + point + " del juego: '" + gameName + "'. ExceptionType: " + ex);
            }
        }

        this.opened = new HashSet<>();

        this.normalItems = new ArrayList<>();
        this.rareItems = new ArrayList<>();

        for (String item : config.getStringList("mapas." + gameName + ".normalItems")){
            int count = 1;

            int max = 1;
            int min = 1;

            try {
                Material material = Material.valueOf(item);
                //Material material2 = Material.valueOf("papap");
                //Material.DIAMOND_CHESTPLATE;
                //material2.
                   // count = Math.floor(Math.random() * 32 + 1);

                if(material.getMaxStackSize() == 64)
                {
                    max = 48;
                    min = 10;



                }else if(material.getMaxStackSize() == 16) {
                    if(Material.ENDER_PEARL == material) {
                        max = 4;
                        min = 1;

                    }else{
                        max = 16;
                        min = 10;

                    }
                }else if(material.getMaxStackSize() == 1) {
                    max = 1;
                    min = 1;
                }else{
                    min = 1;
                    max = 1;
                }
                if(max > min)
                {
                    count = ThreadLocalRandom.current().nextInt(min, max);
                } else {
                    count = max;
                }
                this.normalItems.add(new ItemStack(material, count));
            } catch (Exception ex) {
                ex.printStackTrace();
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + gameName + ": ese item " + item + " no existe xD");
            }
        }

        for (String item : config.getStringList("mapas." + gameName + ".rareItems")){
            int count = 1;
            int max = 1;
            int min = 1;
            try {
                Material material = Material.valueOf(item);
                // count = Math.floor(Math.random() * 32 + 1);

                if(material.getMaxStackSize() == 64)
                {
                    max = 48;
                    min = 10;



                }else if(material.getMaxStackSize() == 16) {
                    if(Material.ENDER_PEARL == material) {
                        max = 4;
                        min = 1;

                    }else{
                        max = 16;
                        min = 10;

                    }
                }else if(material.getMaxStackSize() == 1) {
                    max = 1;
                    min = 1;
                }else{
                    min = 1;
                    max = 1;
                }
                if(max > min)
                {
                    count = ThreadLocalRandom.current().nextInt(min, max);
                } else {
                    count = max;
                }
                this.rareItems.add(new ItemStack(material, count));
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + gameName + ": ese item " + item + " no existe xD");
            }
        }

        this.isTeamGame = Boolean.parseBoolean(config.getString("mapas." + gameName + ".isTeamGame"));
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.idScoreboard = "skywars";
        this.titleScoreboard = ChatColor.YELLOW + "" + ChatColor.BOLD + "  Skywars  ";
        this.displaySlot = DisplaySlot.SIDEBAR;
        //voy a anadir lo de los cofres
        this.toFill = new HashSet<Chest>();

        for (String chestLocationString : config.getStringList("mapas." + gameName + ".chests")){
            try {
                String worldString = chestLocationString.split(",")[0];
                String xS = chestLocationString.split(",")[1];
                String yS = chestLocationString.split(",")[2];
                String zS = chestLocationString.split(",")[3];
                Double x = Double.parseDouble(xS);
                Double y = Double.parseDouble(yS);
                Double z = Double.parseDouble(zS);
                // World world = Bukkit.getWorld(worldString);
                Location chestLocation = new Location(world, x, y, z);
                chestLocation.getBlock().setType(Material.CHEST);
                //chestLocation.toString();
                toFill.add((Chest) chestLocation.getBlock().getState());
            } catch (Exception ex) {
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW +"Hubo un error con el chest con metadata: " + gameName + ex);
            }
        }

        try {
            if(config.contains("lobbySchem") && config.contains("lobbySchem.schemFile")){
                lobbySchemFile = new File(config.getString("lobbySchem.schemFile"));
                lobbyEditSession = SchematicLoad.loadSchem(lobbySchemFile, lobbyPoint);
            } else {
                Skywars.getInstance().getServer().getLogger().severe(ChatColor.RED + "Skywars> " + ChatColor.WHITE + " Recuerda poner el lobbySchem en la config");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean joinGame(GamePlayer gamePlayer) {
        if(gamePlayer.isTeamClass() && !isTeamGame) {
            return false;
        }
        if(isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            if (getPlayers().size() == getMaxPlayers()) {
                gamePlayer.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Esta Arena esta llena.");
                return false;
            }
            //add the player xD
            getPlayers().add(gamePlayer);
            gamePlayer.teleport(lobbyPoint);
            sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] " + gamePlayer.getName() + " (" + getPlayers().size() + "/" + getMaxPlayers() + ") " + "Se ha Unido!");

            gamePlayer.getPlayer().getInventory().clear();
            gamePlayer.getPlayer().getInventory().setArmorContents(null);
            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
            gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
            gamePlayer.getPlayer().setFoodLevel(20);
            if (getPlayers().size() >= getMinPlayers() && !isState(GameState.STARTING)) {
                setState(GameState.STARTING);
                sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] El juego comenzara en 20 segundos");
                startCountdown();
            }
            Skywars.getInstance().setGame(gamePlayer.getPlayer(), this);
            return true;
        } else {
            activateSpectatorSettings(gamePlayer.getPlayer());
            Skywars.getInstance().setGame(gamePlayer.getPlayer(), this);
            return true;
        }

    }

    public boolean isState(GameState state) {
        return getGameState() == state;
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public List<GamePlayer> getSpectators() {
        return spectators;
    }

    public boolean isTeamGame() {
        return isTeamGame;
    }

    public void startCountdown() {
        new GameCountDownTask(this).runTaskTimer(Skywars.getInstance(), 0, 20);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ScoreboardSkywars getScoreboard() {
        return scoreboard;
    }

    public EditSession getLobbyEditSession() {
        return lobbyEditSession;
    }

    public File getLobbySchemFile() {
        return lobbySchemFile;
    }

    public void setScoreboard(ScoreboardSkywars scoreboard) {
        this.scoreboard = scoreboard;
        startTimer();
    }

    public Location getLobbyPoint() {
        return lobbyPoint;
    }

    public void startTimer(){
        new ScoreboardTaskStuff(this).runTaskTimer(Skywars.getInstance(), 0, 20);
    }

    public void assignSpawnPositions() {
        //int id = 0;
        for (int id = 0; id < this.players.size(); id++) {

            try {
                //gamePlayerToSpawnPoint.put(gamePlayer, spawnPoints.get(id));

                GamePlayer gamePlayer = this.players.get(id);
                gamePlayer.teleport(spawnPoints.get(id));


                gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
            } catch (IndexOutOfBoundsException ex) {
                Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + "No hay Suficientes Spawns para la cantidad maxima de jugadores GEEEENIO (El juego es " + getDisplayName() + ")" + ex);
            }
        }
    }

    @NotNull public void finishGame(GamePlayer winner) {
        if(this.gameState == GameState.ENDING){
            this.gameState = GameState.FINISHING;
            //todx change the winner for each player
            //this.getSpectators().forEach((GamePlayer spectator) -> {
            //    spectator.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "La partida ha finalizado", winner.getName() + " Gano esta partida");
            //});
            /*this.getPla().forEach((GamePlayer spectator) -> {
                spectator.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "La partida ha finalizado", winner.getName() + " Gano esta partida", 10, 10, 10);
            });*/
           // winner.getPlayer().sendTitle(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ganaste!", winner.getName() + " Gano esta partida");

            for (int i = 0; i < this.getPlayers().size(); i++) {
                GamePlayer gamePlayer = this.getPlayers().get(i);
                if (Skywars.getInstance().getLobbyPoint() != null) {
                    gamePlayer.getPlayer().teleport(Skywars.getInstance().getLobbyPoint());
                    //todx Change the time to 10 segs and reset to pass the world
                    gamePlayer.getPlayer().resetTitle();
                    gamePlayer.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Tu partida de Skywars ha terminado");
                    gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    gamePlayer.getPlayer().getInventory().setArmorContents(null);
                    gamePlayer.getPlayer().getInventory().clear();
                    gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
                    gamePlayer.getPlayer().teleport(Skywars.getInstance().getLobbyPoint());
                } else {
                    gamePlayer.getPlayer().setHealth(0);
                    Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + "Error con el lobby point ya que no existe (o probablemente es null)");
                }
            }


            sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Tu partida de Skywars ha terminado");
            //getPlayers().get(getPlayers().indexOf(winner));
            for (int i = 0; i < this.getSpectators().size(); i++) {
                GamePlayer spectator = getSpectators().get(i);
                spectator.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Tu partida de Skywars ha terminado");
                spectator.getPlayer().setGameMode(GameMode.SURVIVAL);
                spectator.getPlayer().getInventory().setArmorContents(null);
                spectator.getPlayer().getInventory().clear();
                spectator.getPlayer().setHealth(spectator.getPlayer().getMaxHealth());
                spectator.getPlayer().teleport(Skywars.getInstance().getLobbyPoint());
            }
            this.getScoreboard().removeScoreboard();
            this.spectators.clear();
            this.players.clear();
           // `Rollback`Handler.getRollbackHandler().rollback(world);
            //plugin.games.remove(this);

            for (int i = 0; i < this.getWorld().getPlayers().size(); i++) {
                Player player = this.getWorld().getPlayers().get(i);
                player.teleport(Skywars.getInstance().getLobbyPoint());
            };

            this.scoreboard = null;
            Skywars.getInstance().finishGame(this);
        }
    }

    public void activateSpectatorSettings(Player player) {
        GamePlayer gamePlayer = getGamePlayer(player);

        player.setHealth(gamePlayer.getPlayer().getMaxHealth());
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(this.lobbyPoint);
        this.scoreboard.removeScoreboardToPlayer(gamePlayer);
        if (gamePlayer != null) {
            switchToSpectator(gamePlayer);
        }
    }

    public void sendMessage(String mensaje) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendMessage(mensaje);
        }

    }

    public void sendSpectatorMessage(String mensaje){

        for(GamePlayer gamePlayer : getSpectators()){
            gamePlayer.sendMessage(mensaje);
        }

    }

    public void switchToSpectator(GamePlayer gamePlayer) {

        getPlayers().remove(gamePlayer);
     /*  new CheckPlayerNumber(this).runTaskTimer(Skywars.getInstance(), 0, 200);
        new PlayersRealTime(this).runTaskTimer(Skywars.getInstance(), 0, 20);*/

        //gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
        getSpectators().add(gamePlayer);


    }


    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.isTeamClass()) {
                // Handle
            } else {
                if (gamePlayer.getPlayer() == player) {
                    return gamePlayer;
                }
            }
        }

        for (GamePlayer gamePlayer : getSpectators()) {
            if (gamePlayer.isTeamClass()) {
                // Handle
            } else {
                if (gamePlayer.getPlayer() == player) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public void leave(GamePlayer player) {
        if(spectators.contains(player)){
        spectators.remove(player);
        //todx usar el metodo para un comando o un objeto
        Skywars.getInstance().removePlayer(player.getPlayer());
        player.getPlayer().teleport(lobbyPoint);
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        if(scoreboard != null){
        this.scoreboard.removeScoreboardToPlayer(player);
        }
        }
        if(players.contains(player)){

        players.remove(player);
        Skywars.getInstance().removePlayer(player.getPlayer());

        player.getPlayer().teleport(lobbyPoint);
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        if(this.scoreboard != null){
        this.scoreboard.removeScoreboardToPlayer(player);
        }

        }
    }

    public String getGameName(){
        return GameName;
    }

    public void setMovementFrozen(boolean movementFrozen) {
        this.movementFrozen = movementFrozen;
    }

    public boolean isMovementFrozen() {
        return movementFrozen;
    }

    public World getWorld() {
        return world;
    }

    public Set<Chest> getOpened() {
        return opened;
    }

    public Set<Chest> getToFill() {
        return toFill;
    }
    //TODO more than 2 players
    public List<ItemStack> getRareItems() {
        return rareItems;
    }

    public List<ItemStack> getNormalItems() {
        return normalItems;
    }
    public void setWinner(GamePlayer gamePlayer) {
        this.winner = gamePlayer;
    }

    public GamePlayer getWinner(){
        return this.winner;
    }
    public enum GameState {
        LOBBY, STARTING, PREPARATION, ACTIVE, ENDING, FINISHING
    }
}

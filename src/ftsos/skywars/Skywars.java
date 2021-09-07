package ftsos.skywars;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import ftsos.skywars.admin.AdminGuiManager;
import ftsos.skywars.admin.AdminKitGiveCommand;
import ftsos.skywars.admin.AdminKitListener;
import ftsos.skywars.cage.CageManager;
import ftsos.skywars.comandos.AddCageCommand;
import ftsos.skywars.comandos.SkywarsCommand;
import ftsos.skywars.listeners.*;
import ftsos.skywars.listeners.EventListener;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import ftsos.skywars.objects.SwPlayer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public class Skywars extends JavaPlugin {
	public String rutaConfig;
	private static Skywars instance;
	PluginDescriptionFile pdffile = getDescription();
	public String version = pdffile.getVersion();
	public String nombre = pdffile.getName();
	private Map<Player, GameDefinition> playerGameMap = new HashMap<>();
	public Set<GameDefinition> games = new HashSet<>();
	public WorldEditPlugin worldEditPlugin;
	public CageManager cageManager;
	public AdminGuiManager adminGuiManager;
	public EventHandler eventHandler;
	public List<SwPlayer> players;
	public void onEnable() {
		instance = this;
		Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[" + nombre + "]: " + version + ": " + "Ha Sido Activado");
		registerCommands();
		registerEvents();
		registerConfig();
		if(this.getConfig().getConfigurationSection("mapas") != null){
			for(String gameName : this.getConfig().getConfigurationSection("mapas").getKeys(false)){
				GameDefinition game = new GameDefinition(gameName);
				boolean status = this.registerGame(game);
			}
		}

		if(Bukkit.getPluginManager().getPlugin("WorldEdit") != null){
			 this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		} else {
			this.getServer().getPluginManager().disablePlugin(this);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "El plugin No se pudo activar por que world edit no esta activado");
		}

		this.cageManager = new CageManager();
		this.adminGuiManager = new AdminGuiManager();
		List<EventListener> listeners = new ArrayList<EventListener>();
		listeners.add(new AdminKitListener());
		this.eventHandler = new EventHandler(listeners);
		this.players = new ArrayList<SwPlayer>();
		if(this.getConfig().getConfigurationSection("players") != null){
			for(String player : this.getConfig().getConfigurationSection("players").getKeys(false)){
				getLogger().info(ChatColor.RED + String.valueOf(Bukkit.getOfflinePlayer(UUID.fromString(player)).hasPlayedBefore()));
				if(Bukkit.getOfflinePlayer(UUID.fromString(player)).hasPlayedBefore()){
				players.add(new SwPlayer(getConfig().getIntegerList("players." + player + ".ownedCages"), Bukkit.getOfflinePlayer(UUID.fromString(player)).getPlayer(), getConfig().getInt("players." + player + ".cageIndex")));

				};
			}
		}

	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[" + nombre + "]: " + version + ": " + "Ha Sido Desactivado");

		for (Map.Entry<Player, GameDefinition> entry : playerGameMap.entrySet()) {
			entry.getKey().teleport(getLobbyPoint());
			entry.getKey().getInventory().clear();
			entry.getKey().getInventory().setArmorContents(null);
			entry.getKey().setHealth(entry.getKey().getMaxHealth());
			entry.getKey().setGameMode(GameMode.SURVIVAL);
			for (Player player : getServer().getOnlinePlayers()) {
				player.teleport(getLobbyPoint());
			}
			for (GameDefinition game : getGames()) {
				for (Player player : game.getWorld().getPlayers()) {
					player.teleport(getLobbyPoint());
				}
				String rootDirectory = getServer().getWorldContainer().getAbsolutePath();
				File destFolder = new File(rootDirectory + "/" + game.getWorld().getName() + "_active");

				getServer().unloadWorld(game.getWorld(), false);
				//game.getWorld().getWorldFolder().delete();


				//RollbackHandler.getRollbackHandler().delete(destFolder);

				FileUtils.deleteQuietly(destFolder);
			}

			instance = null;
		}

	}

	public void registerCommands() {
		this.getCommand("skywars").setExecutor(new SkywarsCommand(this));
		this.getCommand("swadminkit").setExecutor(new AdminKitGiveCommand(this));
		this.getCommand("addcagecommand").setExecutor(new AddCageCommand());
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new ChestOpen(), this);
		pm.registerEvents(new PlayerDie(this), this);
		pm.registerEvents(new BlockInteract(), this);
		//todo later XD
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new PlayerHit(), this);
		pm.registerEvents(new ChangedWorld(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new DamageEvent(), this);
		pm.registerEvents(new PlayerJoin(), this);
	}

	private Location lobbyPoint = null;


	public Location getLobbyPoint() {
		if (lobbyPoint == null) {
			int x = 0;
			int y = 0;
			int z = 0;

			String world = "world";

			try {
				x = Skywars.getInstance().getConfig().getInt("lobby-point.x");
				y = Skywars.getInstance().getConfig().getInt("lobby-point.y");
				z = Skywars.getInstance().getConfig().getInt("lobby-point.z");
				world = Skywars.getInstance().getConfig().getString("lobby-point.world");
			} catch (Exception ex) {
				Skywars.getInstance().getLogger().severe("Lobby point failed with exception: " + ex);
				ex.printStackTrace();
			}

			lobbyPoint = new Location(Bukkit.getWorld(world), x, y, z);

		}
		return lobbyPoint;
	}


	public static Skywars getInstance() {
		return instance;
	}


	public void registerConfig() {
		File config = new File(this.getDataFolder(), "config.yml");
		rutaConfig = config.getPath();
		if (!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}



	}

	public void setGame(Player player, GameDefinition game) {

		if (game == null) {
			this.playerGameMap.remove(player);
		} else {
			this.playerGameMap.put(player, game);
		}

	}

	public GameDefinition getGame(String gameName) {
		for (GameDefinition game : games) {
			if (game.getDisplayName().equalsIgnoreCase(gameName)) {
				return game;
			}
		}

		return null;
	}

	public void finishGame(GameDefinition game) {
		if(game != null){
			games.remove(game);
			game.getPlayers().forEach((GamePlayer gamePlayer) -> {
				gamePlayer.getPlayer().getInventory().clear();
				gamePlayer.getPlayer().getInventory().setArmorContents(null);
				gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
				gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
			});
			playerGameMap.entrySet().removeIf(entry -> (game.equals(entry.getValue())));
			String worldName = game.getWorld().getName();

			String rootDirectory = Skywars.getInstance().getServer().getWorldContainer().getAbsolutePath();
			game.getWorld().getPlayers().forEach((Player player) -> {
				player.teleport(getLobbyPoint());
			});

			getServer().unloadWorld(game.getWorld(), false);
			File destFolder = new File(rootDirectory + "/" + worldName + "_active");
			RollbackHandler.getRollbackHandler().delete(destFolder);



			restartGame(game);
		}
	}

	public void restartGame(GameDefinition game) {
		if (game != null) {
			GameDefinition newGame = new GameDefinition(game.getGameName());
			this.registerGame(newGame);
		}
	}

	public GameDefinition getGame(Player player) {
		return this.playerGameMap.get(player);
	}
	public Set<GameDefinition> getGames() {
		return games;
	}

	public boolean registerGame(GameDefinition game) {
		games.add(game);
		return true;
	}

	public void removePlayer(Player player) {
		this.playerGameMap.remove(player);
	}

}


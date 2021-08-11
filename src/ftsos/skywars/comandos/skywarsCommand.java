package ftsos.skywars.comandos;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class skywarsCommand implements CommandExecutor {


    private Skywars plugin;

    public skywarsCommand(Skywars plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //todx hacer los de los chest xD

        if (args.length == 0) {
            sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Escribe /skywars help o /sw help");
        } else if (args[0].equalsIgnoreCase("help")) {
            if (sender.isOp()) {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Skywars Plugin Help \n /sw admin add <Nombre de arena> \n /sw admin <Nombre de arena> spawn (add a spawn for a person) \n /sw admin <Nombre de arena> normalItems (add a Normal Items) \n /sw admin <Nombre de arena> chest (add a chest) \n Annd You have to add a rareItems from the config, I'm so lazy xD");
            } else {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Skywars es un minijuego de tirarse por los cielos por islas y ser el ultimo sobreviviente \n Puedes ocupar /skywars join <Nombre de la Partida> o /sw join <Nombre de la Partida> para unite a una arena automaticamente y jugar tu primera partida");
            }

        } else if (args[0].equalsIgnoreCase("admin")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Escribe /skywars help o /sw help");

            } else {
                if (args.length > 3) {
                    FileConfiguration config = plugin.getConfig();
                    if(args[1].equalsIgnoreCase("add")) {
                        //config.contains();
                        if (!args[2].equalsIgnoreCase("") || !args[2].equalsIgnoreCase(" ")) {
                            //ayuda
                            sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "escribe /skywars help o /sw help");
                        }else{
                            if (!config.contains("mapas." + args[2]) && config.getString("mapas." + args[2]) == null && config.getString("mapas." + args[2]) == null && args.length > 5) {
                                    if(!(sender instanceof Player)) {
                                        Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "No puedes ejecutar este comando desde la consola");
                                    }else {
                                        Player player = (Player) sender;
                                    config.set("mapas." + args[2] + ".jugadores", "0");
                                    config.set("mapas." + args[2] + ".minJugadores", args[4]);
                                    config.set("mapas." + args[2] + ".displayName", args[3]);
                                    config.set("mapas." + args[2] + ".worldName", player.getWorld().getName());
                                    plugin.saveConfig();
                                    plugin.reloadConfig();
                                    sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Mapa creado Satisfactoriamente");
                                }
                            }else{
                                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ya existe un mapa con ese nombre");
                            }
                        }
                    }else if(config.contains("mapas." + args[1]) && config.getString("mapas." + args[1]) != null) {
                        if(args[2].equalsIgnoreCase("spawn")) {
                            if (!(sender instanceof Player)) {
                             sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "No puedes ejecutar este comando desde la consola");
                            }else {
                                Player player = (Player) sender;
                                Location lugar = player.getLocation();
                                if(config.contains("mapas." + args[2] + ".spawns")) {
                                    List<String> spawns = config.getStringList("mapas." + args[2] + ".spawns");
                                    spawns.add(lugar.toString());
                                    config.set("mapas." + args[2] + ".spawns", spawns);
                                    plugin.saveConfig();
                                    plugin.reloadConfig();
                                   // config.set("mapas." + args[2] + ".spawns." + Integer.toString(Integer.parseInt(config.getString("mapas." + args[2] + ".jugadores")) + 1), lugar.toString());
                                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Spawn puesto correctamente");
                                }else{
                                    //List<String> spawns = config.getStringList("mapas." + args[2] + ".spawns");
                                    List<String> spawns2 = new ArrayList<>();
                                    spawns2.add(lugar.toString());
                                    config.set("mapas." + args[2] + ".spawns", spawns2);
                                    plugin.saveConfig();
                                    plugin.reloadConfig();
                                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Spawn puesto correctamente");
                                }



                            }
                        }else if(args[2].equalsIgnoreCase("normalItems") && args.length < 4) {
                            if(config.contains("mapas." + args[2] + ".normalItems"))
                            {
                                boolean exito = false;
                                List<String> normalItems = config.getStringList("mapas." + args[2] + ".normalItems");
                                try {
                                    Material material = Material.valueOf(args[3]);
                                    exito = true;
                                }catch(Exception ex) {
                                    //sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ese Bloque no existe");
                                    Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + args[2] + ": ese item " + args[3] + " no existe xD");

                                    exito = false;
                                }finally {
                                    if(exito)
                                    {
                                        normalItems.add(args[3]);
                                        config.set("mapas." + args[2] + ".normalItems", normalItems);
                                        plugin.saveConfig();
                                        plugin.reloadConfig();
                                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Bloque anadido exitosamente xD");
                                    }else{
                                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ese Bloque no existe");
                                    }
                                }
                            } else {
                                List<String> newNormalItems = new ArrayList<>();
                                boolean newExito = false;
                                try {
                                    Material material = Material.valueOf(args[3]);
                                    newExito = true;
                                }catch(Exception ex) {
                                    //sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ese Bloque no existe");
                                    Skywars.getInstance().getLogger().severe(ChatColor.BOLD + "" + ChatColor.YELLOW + args[2] + ": ese item " + args[3] + " no existe xD");

                                    newExito = false;
                                }finally {
                                    if(newExito)
                                    {
                                        newNormalItems.add(args[3]);
                                        config.set("mapas." + args[2] + ".normalItems", newNormalItems);
                                        plugin.saveConfig();
                                        plugin.reloadConfig();
                                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Bloque anadido exitosamente xD");
                                    }else{
                                        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Ese Bloque no existe");
                                    }
                                }
                            }
                        } else if (args[2].equalsIgnoreCase("chest")){
                            if(!(sender instanceof  Player)){
                                sender.sendMessage(ChatColor.BOLD + "" + ChatColor. YELLOW + "[!] No puedes ejecutar este comando desde la consola");
                            } else {
                                Player playerChest = (Player) sender;

                                Location chestLocation = playerChest.getLocation();
                                String world = chestLocation.getWorld().getName();
                                String x = String.valueOf(chestLocation.getBlockX());
                                String y = String.valueOf(chestLocation.getBlockY());
                                String z = String.valueOf(chestLocation.getBlockZ());
                                //done Chest Location
                                String finalChestLocation = world + "," + x + "," + y + "," + z;
                                if(config.contains("mapas." + args[1] + ".chests") && config.getStringList("mapas." + args[1] + ".chests") != null){
                                    List<String> chests = config.getStringList("mapas." + args[1] + ".chests");

                                    chests.add(finalChestLocation);
                                    config.set("mapas." + args[1] + ".chests", chests);
                                    plugin.reloadConfig();
                                } else {
                                    List<String> chestLocations = new ArrayList<String>();

                                    chestLocations.add(finalChestLocation);

                                    config.set("mapas." + args[1] + ".chests", chestLocations);
                                    plugin.reloadConfig();
                                }
                                //config.set("mapas." + args[1] + ".chests");

                            }
                        }
                    }
                }else{
                    //mandar ayuda
                    sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Escribe /skywars help o /sw help");
                }
            }

        } else if(args[0].equalsIgnoreCase("leave")){
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] No puedes ejecutar este comando desde la consola");

            } else {
                Player player = (Player) sender;
                if(plugin.getGame(player) != null){
                    GameDefinition game = plugin.getGame(player);
                    GamePlayer gamePlayer = game.getGamePlayer(player);
                    game.leave(gamePlayer);
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Saliste de tu partida Correctamente");
                }else{
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] No te encuetras en ninguna partida");
                }
            }
        } else if(args[0].equalsIgnoreCase("join") && args.length == 2){
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] No puedes ejecutar este comando desde la consola");
            }else{
                Player player = (Player) sender;
            if(plugin.getGame(args[1]) != null && plugin.getGame(player) == null){
                GameDefinition game = plugin.getGame(args[1]);
                if (game.isState(GameDefinition.GameState.LOBBY) || game.isState(GameDefinition.GameState.STARTING)) {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    game.joinGame(gamePlayer);
                }


            }else{
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Este juego no existe o no esta activado, o ya estas en uno ;)");
            }
            }
        } else {
            sender.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "[!] Escribe /skywars help o /sw help");
        }
        return false;
    }
}
package ftsos.skywars.cage;

import ftsos.skywars.Skywars;
import ftsos.skywars.util.SchematicLoad;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

public class Cage {

    private File schemFile;
    public Cage(String fileSchemName){

        //todo Cages xD, but for the game player

        Bukkit.getPluginManager().getPlugin("Skywars").getDataFolder() + ;
        this.schemFile = new File()
    }

    public void pasteIt(Location spawnLocation){
        SchematicLoad.loadSchem(this.schemFile, spawnLocation);
    }


}

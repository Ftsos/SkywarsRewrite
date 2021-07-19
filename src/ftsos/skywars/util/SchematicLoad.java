package ftsos.skywars.util;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import com.sk89q.worldedit.world.DataException;
import ftsos.skywars.Skywars;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicLoad {

    public static EditSession loadSchem(File schem, Location location) throws DataException, IOException, MaxChangedBlocksException {
       WorldEditPlugin worldEditPlugin = Skywars.getInstance().worldEditPlugin;
        /*if(location.getWorld() != null){
            EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
            try
            {
                ClipboardFormat format = ClipboardFormats.findByFile(schem);
              ClipboardReader reader = format.getReader(new FileInputStream(schem));
                    Clipboard clipboard = reader.read();

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(session)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);

            }
            catch (Exception e)
            {
                e.printStackTrace();

            }*/
      //  }
      //  EditSession es = new EditSession(new BukkitWorld(location.getWorld()), -1);
        //ClipboardFormat cc = ClipboardFormat.loadSchematic(schem);
      //  cc.paste(es, location.toVector(), false);

        EditSession es = new EditSession(new BukkitWorld(location.getWorld()), 999999999);
        CuboidClipboard cc = CuboidClipboard.loadSchematic(schem);
        cc.paste(es, new Vector(location.getX(), location.getY(), location.getZ()), false);

        return es;

    }

}

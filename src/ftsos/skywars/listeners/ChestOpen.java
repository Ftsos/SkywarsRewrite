package ftsos.skywars.listeners;

import ftsos.skywars.Skywars;
import ftsos.skywars.objects.GameDefinition;
import ftsos.skywars.objects.GamePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ChestOpen implements Listener {
    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GameDefinition game = Skywars.getInstance().getGame(player);

        if(game != null && game.getGamePlayer(player) != null){
            if(game.isState(GameDefinition.GameState.LOBBY) || game.isState(GameDefinition.GameState.PREPARATION) || game.isState(GameDefinition.GameState.STARTING)){
                e.setCancelled(true);
                return;
            }

            GamePlayer gamePlayer = game.getGamePlayer(player);

            if(gamePlayer.isTeamClass()) {
                if(gamePlayer.getTeam().isPlayer(player)){
                    handle(e, game);
                }
            } else {
                if (gamePlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                    handle(e, game);
                }
            }
        }

    }
    //10761
    private void handle(PlayerInteractEvent event, GameDefinition game) {
        if (event.hasBlock() && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();
            if (game.getToFill().contains(chest)){
                if (game.getOpened().contains(chest) || game.getRareItems().size() == 0 || game.getNormalItems().size() == 0) {
                    return;
                }

            chest.getBlockInventory().clear();
            //chest populate
          /*  if (new Random().nextFloat() < 0.20) {
                int toFill = new Random().nextInt(8) + 1;
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getRareItems().size());
                    chest.getBlockInventory().addItem(game.getRareItems().get(selected));
                }
            } else {
                int toFill = new Random().nextInt(5) + 1;
                for (int x = 0; x < toFill; x++) {
                    int selected = new Random().nextInt(game.getNormalItems().size());
                    chest.getBlockInventory().addItem(game.getNormalItems().get(selected));
                }
            }*/
                ArrayList<ChestItem> chestItems = new ArrayList<ChestItem>();
                int rareItemsProbability = 20;
                int normalItemsProbability = 80;
                if(Skywars.getInstance().getConfig().contains("rareItems")){
                   rareItemsProbability = Skywars.getInstance().getConfig().getInt("rareItems");
                }
                if(Skywars.getInstance().getConfig().contains("normalItems")){
                    normalItemsProbability = Skywars.getInstance().getConfig().getInt("normalItems");
                }
              for(ItemStack itemStack : game.getNormalItems()){

                chestItems.add(new ChestItem(itemStack, normalItemsProbability));
              }
              for (ItemStack itemStack : game.getRareItems()) {
                  chestItems.add(new ChestItem(itemStack, rareItemsProbability));
              }

              populateChest(chest, chestItems);
            game.getOpened().add(chest);
            game.getToFill().remove(chest);
        }else{
                return;
            }
    }
    }
    public class ChestItem {

        private ItemStack item;
        private int chance;

        public ChestItem(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }
    }


    public void populateChest(Chest chest, ArrayList<ChestItem> chestItemList) {
        Inventory inventory = chest.getBlockInventory();
        int added = 0;

        for (ChestItem chestItem : chestItemList) {
            if (new Random().nextInt(99) + 1 <= chestItem.getChance()) {
                inventory.addItem(chestItem.getItem());

                if (added++ > inventory.getSize()) {
                    break;
                }
            }
        }
    }



}
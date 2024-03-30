package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.builders.types.CratePrizeMenu;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import java.util.HashMap;
import java.util.Map;

public class WarCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    private final Map<ItemStack, String> colorCodes = new HashMap<>();

    public WarCrate(Key key, Crate crate, Player player, int size) {
        super(key, crate, player, size);
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Crate crate = getCrate();
        Key key = getKey();
        Player player = getPlayer();

        // Crate event failed so we return.
        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, checkHand);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            // Remove closer/picker
            //this.crateManager.removeCloser(player);
            //this.crateManager.removePicker(player);

            return;
        }

        //this.crateManager.addPicker(player, false);
        //this.crateManager.addCloser(player, false);

        addCrateTask(new BukkitRunnable() {
            int full = 0;
            int open = 0;

            @Override
            public void run() {
                if (this.full < 25) {
                    setRandomPrizes();

                    playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_LAVA_POP");
                }

                this.open++;

                if (this.open >= 3) {
                    player.openInventory(getInventory());

                    this.open = 0;
                }

                this.full++;

                if (this.full == 26) {
                    playSound("stop-sound", SoundCategory.PLAYERS, "BLOCK_LAVA_POP");

                    setRandomGlass();

                    //crateManager.addPicker(player, true);
                }
            }
        }.runTaskTimer(this.plugin, 1, 3));
    }

    private void setRandomPrizes() {
        Player player = getPlayer();
        Crate crate = getCrate();

        //if (!this.crateManager.isInOpeningList(player) && !(getInventory().getHolder(false) instanceof CratePrizeMenu)) return;

        for (int index = 0; index < 9; index++) {
            setItem(index, crate.pickPrize(player).getDisplayItem(player));
        }
    }

    private void setRandomGlass() {
        //if (!this.crateManager.isInOpeningList(getPlayer()) && !(getInventory().getHolder(false) instanceof CratePrizeMenu)) return;

        if (this.colorCodes.isEmpty()) getColorCode();

        ItemBuilder builder = MiscUtils.getRandomPaneColor();
        builder.setName("&" + this.colorCodes.get(builder.build()) + "&l???");
        ItemStack item = builder.build();

        for (int index = 0; index < 9; index++) {
            setItem(index, item);
        }
    }

    private void getColorCode() {
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.WHITE_STAINED_GLASS_PANE).build(), "f");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.ORANGE_STAINED_GLASS_PANE).build(), "6");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.MAGENTA_STAINED_GLASS_PANE).build(), "d");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE).build(), "3");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.YELLOW_STAINED_GLASS_PANE).build(), "e");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.LIME_STAINED_GLASS_PANE).build(), "a");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.PINK_STAINED_GLASS_PANE).build(), "c");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).build(), "7");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE).build(), "7");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.CYAN_STAINED_GLASS_PANE).build(), "3");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.PURPLE_STAINED_GLASS_PANE).build(), "5");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.BLUE_STAINED_GLASS_PANE).build(), "9");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.BROWN_STAINED_GLASS_PANE).build(), "6");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.GREEN_STAINED_GLASS_PANE).build(), "2");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.RED_STAINED_GLASS_PANE).build(), "4");
        this.colorCodes.put(new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).build(), "8");
    }

    @Override
    public void run() {

    }
}
package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import com.badbones69.crazycrates.api.builders.CrateBuilder;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsgoCrate extends CrateBuilder {

    private final @NotNull UserManager userManager = null;
    private final @NotNull CrateManager crateManager = null;

    public CsgoCrate(Key key, Crate crate, Player player, int size) {
        super(key, crate, player, size);
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Inventory inventory = getInventory();

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

            return;
        }

        // Set the glass/display items to the inventory.
        populate();

        // Open the inventory.
        player.openInventory(inventory);

        addCrateTask(new BukkitRunnable() {
            int time = 1;

            int full = 0;

            int open = 0;

            @Override
            public void run() {
                if (this.full <= 50) { // When Spinning
                    moveItemsAndSetGlass();

                    playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");
                }

                this.open++;

                if (this.open >= 5) {
                    getPlayer().openInventory(getInventory());
                    this.open = 0;
                }

                this.full++;

                if (this.full > 51) {
                    if (MiscUtils.slowSpin(120, 15).contains(this.time)) { // When Slowing Down
                        moveItemsAndSetGlass();

                        playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");
                    }

                    this.time++;

                    if (this.time == 60) { // When done
                        playSound("stop-sound", SoundCategory.PLAYERS, "ENTITY_PLAYER_LEVELUP");

                        //plugin.getCrateManager().endActiveTask(player);

                        ItemStack item = getInventory().getItem(13);

                        if (item != null) {
                            crate.givePrize(player, crate.getPrize(item));
                        }

                        //crateManager.removePlayerFromOpeningList(getPlayer());

                        cancel();

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (player.getOpenInventory().getTopInventory().equals(inventory)) player.closeInventory();
                            }
                        }.runTaskLater(plugin, 40);
                    } else if (this.time > 60) { // Added this due reports of the prizes spamming when low tps.
                        cancel();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 1, 1));
    }

    private void populate() {
        Map<Integer, ItemStack> glass = new HashMap<>();

        Inventory inventory = getInventory();
        Crate crate = getCrate();
        Player player = getPlayer();

        for (int index = 0; index < 10; index++) {
            if (index < 9 && index != 3) glass.put(index, inventory.getItem(index));
        }

        for (int index : glass.keySet()) {
            if (getInventory().getItem(index) == null) {
                setCustomGlassPane(index);
                setCustomGlassPane(index + 18);
            }
        }

        for (int index = 1; index < 10; index++) {
            if (index < 9 && index != 4) glass.put(index, inventory.getItem(index));
        }

        setItem(0, glass.get(1));

        setItem(1, glass.get(2));
        setItem(1 + 18, glass.get(2));

        setItem(2, glass.get(3));
        setItem(2 + 18, glass.get(3));

        setItem(3, glass.get(5));
        setItem(3 + 18, glass.get(5));

        ItemStack itemStack = new ItemBuilder().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
        setItem(4, itemStack);
        setItem(4 + 18, itemStack);

        setItem(5, glass.get(6));
        setItem(5 + 18, glass.get(6));

        setItem(6, glass.get(7));
        setItem(6 + 18, glass.get(7));

        setItem(7, glass.get(8));
        setItem(7 + 18, glass.get(8));

        setCustomGlassPane(8);
        setCustomGlassPane(8 + 18);

        // Set display items.
        for (int index = 9; index > 8 && index < 18; index++) {
            setItem(index, crate.pickPrize(player).getDisplayItem(player));
        }
    }

    private void moveItemsAndSetGlass() {
        Player player = getPlayer();

        List<ItemStack> items = new ArrayList<>();

        for (int i = 9; i > 8 && i < 17; i++) {
            items.add(getInventory().getItem(i));
        }

        setItem(9, getCrate().pickPrize(player).getDisplayItem(player));

        for (int i = 0; i < 8; i++) {
            setItem(i + 10, items.get(i));
        }
    }

    @Override
    public void run() {

    }
}
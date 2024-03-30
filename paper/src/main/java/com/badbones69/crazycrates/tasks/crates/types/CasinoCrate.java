package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.builders.CrateBuilder;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.api.objects.Tier;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.api.enums.types.KeyType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CasinoCrate extends CrateBuilder {

    private final @NotNull CrateManager crateManager = null;
    private final @NotNull UserManager userManager = null;

    public CasinoCrate(Key key, Crate crate, Player player, int size) {
        super(key, crate, player, size);

        runTaskTimer(this.plugin, 1, 1);
    }

    private int counter = 0;
    private int time = 1;
    private int open = 0;

    @Override
    public void run() {
        Inventory inventory = getInventory();

        Player player = getPlayer();

        Crate crate = getCrate();

        // If cancelled, we return.
        if (this.isCancelled) {
            return;
        }

        if (this.counter <= 50) { // When the crate is currently spinning.
            playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");

            cycle();
        }

        this.open++;

        if (this.open >= 5) {
            player.openInventory(inventory);

            this.open = 0;
        }

        this.counter++;

        if (this.counter > 51) {
            if (MiscUtils.slowSpin(120, 15).contains(this.time)) {
                playSound("cycle-sound", SoundCategory.PLAYERS, "BLOCK_NOTE_BLOCK_XYLOPHONE");

                cycle();
            }

            this.time++;

            if (this.time >= 60) { // When the crate task is finished.
                playSound("stop-sound", SoundCategory.PLAYERS, "ENTITY_PLAYER_LEVELUP");

                //this.crateManager.endActiveTask(player);

                crate.givePrize(inventory, 11, player);
                crate.givePrize(inventory, 13, player);
                crate.givePrize(inventory, 15, player);

                //this.crateManager.removePlayerFromOpeningList(player);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory().equals(inventory)) player.closeInventory();
                    }
                }.runTaskLater(this.plugin, 40);

                cancel();

                return;
            }
        }

        this.counter++;
    }

    @Override
    public void open(KeyType keyType, boolean checkHand) {
        if (isCrateEventValid(keyType, checkHand)) {
            return;
        }

        Player player = getPlayer();
        Crate crate = getCrate();
        Key key = getKey();

        //boolean keyCheck = this.userManager.takeKeys(1, player.getUniqueId(), crate.getName(), key.getName(), true, checkHand);

        if (!true) {
            // Send the message about failing to take the key.
            //MiscUtils.failedToTakeKey(player, crate.getName(), key.getName());

            // Remove from opening list.
            //this.crateManager.removePlayerFromOpeningList(player);

            return;
        }

        setDisplayItems(true);

        player.openInventory(getInventory());
    }

    private void setDisplayItems(boolean isStatic) {
        Crate crate = getCrate();

        List<Tier> tiers = crate.getTiers();

        ConfigurationSection section = crate.getSection();

        if (isStatic) {
            for (int index = 0; index < 27; index++) {
                setItem(index, getRandomGlassPane());
            }
        }

        if (section != null) {
            boolean isRandom = section.getBoolean("toggle", false);

            String row_uno = section.getString("types.row-1");
            String row_dos = section.getString("types.row-2");
            String row_tres = section.getString("types.row-3");

            if (isRandom) {
                ThreadLocalRandom random = ThreadLocalRandom.current();

                setItem(2, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(11, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(20, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));

                setItem(4, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(13, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(22, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));

                setItem(6, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(15, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));
                setItem(24, getDisplayItem(tiers.get(random.nextInt(tiers.size()))));

                return;
            }

            setItem(2, getDisplayItem(crate.getTier(row_uno)));
            setItem(11, getDisplayItem(crate.getTier(row_uno)));
            setItem(20, getDisplayItem(crate.getTier(row_uno)));

            setItem(4, getDisplayItem(crate.getTier(row_dos)));
            setItem(13, getDisplayItem(crate.getTier(row_dos)));
            setItem(22, getDisplayItem(crate.getTier(row_dos)));

            setItem(6, getDisplayItem(crate.getTier(row_tres)));
            setItem(15, getDisplayItem(crate.getTier(row_tres)));
            setItem(24, getDisplayItem(crate.getTier(row_tres)));
        }
    }

    private void cycle() {
        for (int index = 0; index < 27; index++) {
            ItemStack itemStack = getInventory().getItem(index);

            if (itemStack != null) {
                if (itemStack.hasItemMeta()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                    if (!container.has(PersistentKeys.crate_prize.getNamespacedKey())) {
                        setItem(index, getRandomGlassPane());
                    }
                }
            }
        }

        setDisplayItems(false);
    }
}
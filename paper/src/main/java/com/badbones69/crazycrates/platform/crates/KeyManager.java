package com.badbones69.crazycrates.platform.crates;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import us.crazycrew.crazycrates.platform.Server;
import us.crazycrew.crazycrates.platform.keys.KeyConfig;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull Logger logger = this.plugin.getLogger();

    private final @NotNull Server instance = this.plugin.getInstance();

    private final Set<Key> keys = new HashSet<>();

    public void load() {
        Preconditions.checkNotNull(this.instance.getKeyFiles(), "Could not read from the keys directory! " + this.instance.getKeyFolder().getAbsolutePath());

        for (File file : this.instance.getKeyFiles()) {
            if (MiscUtils.isLogging()) this.logger.info("Loading key: " + file.getName());

            KeyConfig keyConfig = new KeyConfig(file);

            try {
                keyConfig.load();
            } catch (InvalidConfigurationException exception) {
                this.logger.log(Level.WARNING, file.getName() + " contains invalid YAML structure.", exception);

                continue;
            } catch (IOException exception) {
                this.logger.log(Level.WARNING, "Could not load key file: " + file.getName(), exception);

                continue;
            }

            this.keys.add(new Key(keyConfig));
        }
    }

    public ItemStack getKey(Player player) {
        EntityEquipment equipment = player.getEquipment();

        return equipment.getItemInMainHand().isEmpty() ? equipment.getItemInOffHand() : equipment.getItemInMainHand();
    }

    public Set<ItemStack> getKeys(boolean loopInventory, Player player) {
        InventoryView inventory = player.getOpenInventory();

        Set<ItemStack> items = new HashSet<>();

        if (loopInventory) {
            items.addAll(Arrays.asList(inventory.getBottomInventory().getContents()));
            items.removeIf(key -> key.getType() == Material.AIR || !key.hasItemMeta());
            items.remove(player.getEquipment().getItemInOffHand());

            return items;
        }

        EntityEquipment equipment = player.getEquipment();

        ItemStack main = equipment.getItemInMainHand();
        ItemStack off = equipment.getItemInOffHand();

        if (main.getType() != Material.AIR && main.hasItemMeta()) items.add(equipment.getItemInMainHand());
        if (off.getType() != Material.AIR && off.hasItemMeta()) items.add(equipment.getItemInOffHand());

        return items;
    }

    public Key getKey(String keyName) {
        Key key = null;

        for (Key pair : this.keys) {
            if (!pair.getKeyName().equalsIgnoreCase(keyName)) continue;

            key = pair;

            break;
        }

        return key;
    }

    public boolean takeKeys(boolean loopInventory, Player player, int amount, String crateName, String keyName) {
        boolean isEmpty = crateName.isEmpty() || crateName.isBlank();

        int takeAmount = amount;

        try {
            for (ItemStack item : getKeys(loopInventory, player)) {
                int itemAmount = item.getAmount();

                if (takeAmount - itemAmount >= 0) {
                    MiscUtils.removeMultipleItemStacks(player.getInventory(), item);

                    takeAmount =- itemAmount;
                } else {
                    item.setAmount(itemAmount - takeAmount);

                    takeAmount = 0;
                }

                if (takeAmount <= 0) {
                    return true;
                }
            }
        } catch (Exception exception) {
            if (!isEmpty) {
                MiscUtils.failedToTakeKey(player, crateName, keyName);
            } else {
                MiscUtils.failedToTakeKey(player, keyName);
            }

            return false;
        }

        return false;
    }

    public boolean takeKeys(boolean loopInventory, Player player, int amount, String keyName) {
        return takeKeys(loopInventory, player, amount, "", keyName);
    }

    public Key getKey(Crate crate, ItemMeta itemMeta) {
        Preconditions.checkNotNull(crate, "Crate can't be null.");
        Preconditions.checkNotNull(itemMeta, "Item Meta can't be null.");

        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        if (!data.has(PersistentKeys.crate_key.getNamespacedKey())) {
            // Get the item meta as a string
            String value = itemMeta.getAsString();

            String[] sections = value.split(",");

            String pair = null;

            for (String key : sections) {
                if (key.contains("CrazyCrates-Crate")) {
                    pair = key.trim().replaceAll("\\{", "").replaceAll("\"", "");

                    break;
                }
            }

            if (pair == null) {
                return null;
            }

            return getKey(pair.split(":")[1]);
        }

        String name = data.get(PersistentKeys.crate_key.getNamespacedKey(), PersistentDataType.STRING);

        if (!crate.getKeys().contains(name)) return null;

        return getKey(name);
    }

    public boolean hasKey(Player player, Crate crate, boolean loopInventory) {
        Preconditions.checkNotNull(player, "Player can't be null.");
        Preconditions.checkNotNull(crate, "Crate can't be null.");

        Set<ItemStack> items = getKeys(loopInventory, player);

        boolean hasKey = false;

        if (items.isEmpty()) {
            for (String name : crate.getKeys()) {
                //hasKey = this.userManager.getVirtualKeys(player.getUniqueId(), name) >= 1;

                break;
            }

            return hasKey;
        }

        for (ItemStack item : items) {
            if (!item.hasItemMeta()) continue;

            Key key = getKey(crate, item.getItemMeta());

            if (key != null) {
                hasKey = true;

                break;
            }
        }

        return hasKey;
    }

    public void addKey(Player player, Key key) {
        player.getInventory().addItem(key.getKey(player).build());
    }

    public void addKey(Player player, Key key, int amount) {
        player.getInventory().addItem(key.getKey(player, amount).build());
    }

    public Set<Key> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }
}
package com.badbones69.crazycrates.platform.crates;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.enums.Files;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager extends us.crazycrew.crazycrates.api.users.UserManager {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    private final @NotNull SettingsManager config = ConfigManager.getConfig();

    private final @NotNull FileConfiguration data = Files.data.getFile();

    @Override
    public void addKeys(UUID uuid, String keyName, boolean isVirtual, int amount) {
        if (isVirtual) {
            addVirtualKeys(uuid, keyName, amount);

            return;
        }

        addPhysicalKeys(uuid, keyName, amount);
    }

    /**
     * Take keys from a player.
     *
     * @param uuid      the uuid of the player.
     * @param crateName the name of the crate.
     * @param keyName   the name of the key.
     * @param isVirtual if the key is virtual or physical.
     * @param amount    the amount to take.
     */
    @Override
    public boolean takeKeys(UUID uuid, String crateName, String keyName, boolean isVirtual, int amount) {
        Crate crate = this.crateManager.getCrate(crateName);

        boolean hasKey = crate.getKeys().contains(keyName);

        if (isVirtual && hasKey) {
            return remove(uuid, keyName, amount);
        }

        return this.keyManager.takeKeys(true, getUser(uuid), amount, crateName, keyName);
    }

    @Override
    public boolean takeKeys(UUID uuid, String keyName, boolean isVirtual, int amount) {
        if (isVirtual) {
            return remove(uuid, keyName, amount);
        }

        return this.keyManager.takeKeys(true, getUser(uuid), amount, keyName);
    }

    private boolean remove(UUID uuid, String keyName, int amount) {
        int newAmount = Math.max(getVirtualKeys(uuid, keyName) - amount, 0);

        if (newAmount < 1) {
            this.data.set("Players." + uuid + "." + keyName, null);
        } else {
            this.data.set("Players." + uuid + "." + keyName, newAmount);
        }

        Files.data.save();

        return true;
    }

    @Override
    public int getVirtualKeys(UUID uuid, String keyName) {
        return this.data.getInt("Players." + uuid + "." + keyName, 0);
    }

    @Override
    public int getPhysicalKeys(UUID uuid, String keyName, boolean loopInventory) {
        Player player = getUser(uuid);

        int keys = 0;

        for (ItemStack item : this.keyManager.getKeys(loopInventory, player)) {
            if (this.keyManager.getKey(player) != null) keys += item.getAmount();
        }

        return keys;
    }

    @Override
    public int getTotalKeys(UUID uuid, String keyName) {
        if (getUser(uuid) != null) {
            return getPhysicalKeys(uuid, keyName, true) + getVirtualKeys(uuid, keyName);
        }

        return getVirtualKeys(uuid, keyName);
    }

    @Override
    public void addVirtualKeys(UUID uuid, String keyName, int amount) {
        int keys = getVirtualKeys(uuid, keyName);

        setKeys(uuid, keyName, Math.max(keys + amount, 0));
    }

    @Override
    public boolean hasVirtualKeys(UUID uuid, String keyName) {
        return getVirtualKeys(uuid, keyName) >= 1;
    }

    @Override
    public void setKeys(UUID uuid, String keyName, int amount) {
        this.data.set("Players." + uuid + "." + keyName, amount);

        Files.data.save();
    }

    @Override
    public void addPhysicalKeys(UUID uuid, String keyName, int amount) {
        Player player = getUser(uuid);

        Key key = this.keyManager.getKey(keyName);

        if (MiscUtils.isInventoryFull(player)) {
            if (this.config.getProperty(ConfigKeys.give_virtual_keys_when_inventory_full)) {
                addVirtualKeys(uuid, keyName, amount);

                if (config.getProperty(ConfigKeys.notify_player_when_inventory_full)) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{amount}", String.valueOf(amount));
                    placeholders.put("{player}", player.getName());
                    placeholders.put("{keytype}", KeyType.getType(true).getFriendlyName());
                    placeholders.put("{key}", keyName);

                    player.sendMessage(Messages.cannot_give_player_keys.getMessage(placeholders, player));
                }

                player.getWorld().dropItemNaturally(player.getLocation(), key.getKey(player, amount).build());

                return;
            }

            return;
        }

        player.getInventory().addItem(key.getKey(player, amount).build());
    }

    @Override
    public boolean isUserNull(UUID uuid) {
        return getUser(uuid) != null;
    }

    @Override
    public Player getUser(UUID uuid) {
        return this.plugin.getServer().getPlayer(uuid);
    }
}
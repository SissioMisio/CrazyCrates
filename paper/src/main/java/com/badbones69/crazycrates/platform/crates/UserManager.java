package com.badbones69.crazycrates.platform.crates;

import java.util.UUID;

public class UserManager extends us.crazycrew.crazycrates.api.users.UserManager {

    @Override
    public void addKeys(UUID uuid, String keyName, boolean isVirtual, int amount) {
        /*if (isVirtual) {
            addVirtualKeys(uuid, keyName, amount);

            return;
        }

        addPhysicalKeys(uuid, keyName, amount);*/
    }

    @Override
    public boolean takeKeys(UUID uuid, String crateName, String keyName, boolean isVirtual, int amount) {
        /*Crate crate = this.crateManager.getCrate(crateName);

        boolean hasKey = crate.getKeys().contains(keyName);

        if (isVirtual && hasKey) {
            return remove(uuid, keyName, amount);
        }

        Player player = this.plugin.getServer().getPlayer(uuid);

        return this.keyManager.takeKeys(true, player, amount, crateName, keyName);*/
        return false;
    }

    @Override
    public boolean takeKeys(UUID uuid, String keyName, boolean isVirtual, int amount) {
        /*if (isVirtual) {
            return remove(uuid, keyName, amount);
        }

        Player player = this.plugin.getServer().getPlayer(uuid);

        return this.keyManager.takeKeys(true, player, amount, keyName);*/
        return false;
    }

    private boolean remove(UUID uuid, String keyName, int amount) {
        /*int newAmount = Math.max(getVirtualKeys(uuid, keyName) - amount, 0);

        if (newAmount < 1) {
            this.data.set("Players." + uuid + "." + keyName, null);
        } else {
            this.data.set("Players." + uuid + "." + keyName, newAmount);
        }

        Files.data.save();*/

        return true;
    }

    @Override
    public int getVirtualKeys(UUID uuid, String keyName) {
        //return this.data.getInt("Players." + uuid + "." + keyName, 0);
        return 1;
    }

    @Override
    public int getPhysicalKeys(UUID uuid, String keyName, boolean loopInventory) {
        int keys = 0;

        /*Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            for (ItemStack item : this.keyManager.getKeys(loopInventory, player)) {
                if (this.keyManager.getKey(player) != null) keys += item.getAmount();
            }
        }*/

        return keys;
    }

    @Override
    public int getTotalKeys(UUID uuid, String keyName) {
        /*if (uuid != null) {
            return getPhysicalKeys(uuid, keyName, true) + getVirtualKeys(uuid, keyName);
        }

        return getVirtualKeys(uuid, keyName);*/
        return 1;
    }

    @Override
    public void addVirtualKeys(UUID uuid, String keyName, int amount) {
        //int keys = getVirtualKeys(player, keyName);

        //setKeys(player, keyName, Math.max(keys + amount, 0));
    }

    @Override
    public boolean hasVirtualKeys(UUID uuid, String keyName) {
        //return getVirtualKeys(player, keyName) >= 1;
        return false;
    }

    @Override
    public void setKeys(UUID uuid, String keyName, int amount) {
        //this.data.set("Players." + player.getUniqueId() + "." + keyName, amount);

        //Files.data.save();
    }

    @Override
    public void addPhysicalKeys(UUID uuid, String keyName, int amount) {
        /*Key key = this.keyManager.getKey(keyName);

        if (MiscUtils.isInventoryFull(player)) {
            if (this.config.getProperty(ConfigKeys.give_virtual_keys_when_inventory_full)) {
                addVirtualKeys(player, keyName, amount);

                if (config.getProperty(ConfigKeys.notify_player_when_inventory_full)) {
                    Map<String, String> placeholders = new HashMap<>();

                    placeholders.put("{amount}", String.valueOf(amount));
                    placeholders.put("{player}", player.getName());
                    placeholders.put("{keytype}", KeyType.getType(true).getFriendlyName());
                    placeholders.put("{key}", keyName);

                    player.sendMessage(Messages.cannot_give_player_keys.getMessage(player, placeholders));
                }

                player.getWorld().dropItemNaturally(player.getLocation(), key.getKey(player, amount).build());

                return;
            }

            return;
        }

        player.getInventory().addItem(key.getKey(player, amount).build());*/
    }
}
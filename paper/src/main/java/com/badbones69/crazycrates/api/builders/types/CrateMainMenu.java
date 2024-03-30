package com.badbones69.crazycrates.api.builders.types;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.tasks.InventoryManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import com.badbones69.crazycrates.api.builders.InventoryBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateMainMenu extends InventoryBuilder {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();

    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull UserManager userManager = this.plugin.getUserManager();

    private final @NotNull SettingsManager config = ConfigManager.getConfig();

    public CrateMainMenu(String title, int rows, Player player) {
        super(title, rows, player);
    }

    @Override
    public InventoryBuilder build() {
        Inventory inventory = getGui().getInventory();

        Player player = getPlayer();

        for (Crate crate : this.crateManager.getCrates()) {
            int slot = crate.getSlot();

            if (slot > getRows()) continue;

            slot--;

            if (crate.isInMenu()) {
                inventory.setItem(slot, crate.getDisplayItem());
            }
        }

        if (this.config.getProperty(ConfigKeys.filler_toggle)) {
            String id = this.config.getProperty(ConfigKeys.filler_item);
            String name = this.config.getProperty(ConfigKeys.filler_name);
            List<String> lore = this.config.getProperty(ConfigKeys.filler_lore);

            ItemStack item = new ItemBuilder().setMaterial(id).setName(name).setLore(lore).setTarget(player).build();

            for (int i = 0; i < getRows(); i++) {
                inventory.setItem(i, item.clone());
            }
        }

        if (this.config.getProperty(ConfigKeys.gui_customizer_toggle)) {
            List<String> customizer = this.config.getProperty(ConfigKeys.gui_customizer);

            if (!customizer.isEmpty()) {
                for (String custom : customizer) {
                    int slot = 0;
                    ItemBuilder item = new ItemBuilder();

                    String[] split = custom.split(", ");

                    for (String option : split) {

                        if (option.contains("item:")) item.setMaterial(option.replace("item:", ""));

                        if (option.contains("name:")) {
                            option = option.replace("name:", "");

                            option = getCrates(option);

                            item.setName(option.replaceAll("\\{player}", player.getName()));
                        }

                        if (option.contains("lore:")) {
                            option = option.replace("lore:", "");
                            String[] lore = option.split(",");

                            for (String line : lore) {
                                option = getCrates(option);

                                item.addLore(option.replaceAll("\\{player}", player.getName()));
                            }
                        }

                        if (option.contains("glowing:")) item.setGlow(Boolean.parseBoolean(option.replace("glowing:", "")));

                        if (option.contains("player:")) item.setPlayerName(option.replaceAll("\\{player}", player.getName()));

                        if (option.contains("slot:")) slot = Integer.parseInt(option.replace("slot:", ""));

                        if (option.contains("unbreakable-item")) item.setUnbreakable(Boolean.parseBoolean(option.replace("unbreakable-item:", "")));

                        if (option.contains("hide-item-flags")) item.hideItemFlags(Boolean.parseBoolean(option.replace("hide-item-flags:", "")));
                    }

                    if (slot > getRows()) continue;

                    slot--;

                    inventory.setItem(slot, item.setTarget(player).build());
                }
            }
        }

        return this;
    }

    private String getCrates(String option) {
        Player player = getPlayer();

        /*for (Key key : this.keyManager.getKeys()) {
            String name = key.getName().toLowerCase();

            int virtualKeys = this.userManager.getVirtualKeys(player.getUniqueId(), key.getName());

            int physicalKeys = this.userManager.getPhysicalKeys(player.getUniqueId(), key.getName(), key.getName());

            int totalKeys = virtualKeys + physicalKeys;

            option = option
                    .replaceAll("%" + name + "_physical%", physicalKeys + "")
                    .replaceAll("%" + name + "_total%", totalKeys + "")
                    .replaceAll("%" + name + "%", virtualKeys + "");
        }

        for (Crate crate : this.crateManager.getCrates()) {
            option = option.replaceAll("%" + crate.getName() + "_opened%", this.userManager.getCrateOpened(player.getUniqueId(), crate.getName()) + "");
        }*/

        return option;
    }

    public static class CrateMenuListener implements Listener {

        private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

        private final @NotNull KeyManager keyManager = null;

        private final @NotNull CrateManager crateManager = null;

        private final @NotNull SettingsManager config = ConfigManager.getConfig();

        private final @NotNull InventoryManager inventoryManager = null;

        private final @NotNull UserManager userManager = null;

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!(event.getInventory().getHolder(false) instanceof CrateMainMenu holder)) return;

            Inventory inventory = holder.getGui().getInventory();

            event.setCancelled(true);

            Player player = holder.getPlayer();

            ItemStack item = event.getCurrentItem();

            if (item == null || item.getType() == Material.AIR) return;

            if (!item.hasItemMeta()) return;

            //Crate crate = this.crateManager.getCrate(ItemUtils.getCrate(item.getItemMeta()));
            Crate crate = null;

            switch (event.getClick()) {
                case RIGHT, SHIFT_RIGHT -> {
                    if (crate.isPreviewToggle()) {
                        crate.playSound(player, "click-sound", "UI_BUTTON_CLICK", SoundCategory.PLAYERS);

                        inventory.close();

                        this.inventoryManager.addViewer(player);
                        this.inventoryManager.openNewCratePreview(player, crate, crate.getCrateType() == CrateType.casino);

                        return;
                    }

                    player.sendMessage(Messages.preview_disabled.getMessage("{crate}", crate.getCrateName(), player));
                }

                case LEFT, SHIFT_LEFT -> {
                    //if (this.crateManager.isCrateActive(player)) {
                    //    player.sendMessage(Messages.already_opening_crate.getMessage("{crate}", crate.getCrateName(), player));

                    //    return;
                    //}

                    Key key = null;

                    //if (this.keyManager.getItem(player) == null) {
                    //    for (String keyName : crate.getKeys()) {
                    //        key = this.keyManager.getKey(keyName);

                    //        break;
                    //    }
                    //} else {
                        //key = this.keyManager.getKey(crate, this.keyManager.getItem(player).getItemMeta());
                    //}

                    //if (key == null) return;

                    crate.playSound(player, "click-sound", "UI_BUTTON_CLICK", SoundCategory.PLAYERS);

                    KeyType keyType = KeyType.virtual_key;
                    boolean hasKey = false;

                    //hasKey =

                    if (this.userManager.getVirtualKeys(player.getUniqueId(), "") >= 1) {
                        hasKey = true;
                    } else {
                        //if (this.config.getProperty(ConfigKeys.virtual_accepts_physical_keys) && this.userManager.hasPhysicalKey(player.getUniqueId(), crate.getName(), "", false)) {
                        //    hasKey = true;
                        //    keyType = KeyType.physical_key;
                        //}
                    }

                    if (!hasKey) {
                        if (this.config.getProperty(ConfigKeys.need_key_sound_toggle)) {
                            player.playSound(player.getLocation(), Sound.valueOf(this.config.getProperty(ConfigKeys.need_key_sound)), SoundCategory.PLAYERS, 1f, 1f);
                        }

                        Map<String, String> placeholders = new HashMap<>();

                        placeholders.put("{crate}", crate.getName());
                        //placeholders.put("{key}", key.getName());

                        player.sendMessage(Messages.no_keys.getMessage(placeholders, player));

                        return;
                    }

                    for (String world : this.config.getProperty(ConfigKeys.disabled_worlds)) {
                        if (world.equalsIgnoreCase(player.getWorld().getName())) {
                            player.sendMessage(Messages.world_disabled.getMessage("{world}", player.getWorld().getName(), player));

                            return;
                        }
                    }

                    if (MiscUtils.isInventoryFull(player)) {
                        player.sendMessage(Messages.inventory_not_empty.getMessage("{crate}", crate.getName(), player));

                        return;
                    }

                    //this.crateManager.openCrate(player, crate, key, keyType, player.getLocation(), true, false);
                }
            }
        }
    }
}
package com.badbones69.crazycrates.api.builders.types;

import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.api.objects.Crate;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import com.badbones69.crazycrates.platform.crates.UserManager;
import com.badbones69.crazycrates.platform.crates.objects.Key;
import com.badbones69.crazycrates.platform.utils.ItemUtils;
import com.ryderbelserion.cluster.items.ItemBuilder;
import com.ryderbelserion.cluster.items.ParentBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import com.badbones69.crazycrates.api.builders.InventoryBuilder;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import java.util.HashMap;
import java.util.Map;

public class CrateAdminMenu extends InventoryBuilder {

    private final @NotNull UserManager userManager = this.plugin.getUserManager();
    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    public CrateAdminMenu(String title, int rows, Player player) {
        super(title, rows, player);
    }

    @Override
    public InventoryBuilder build() {
        Gui gui = getGui();

        ItemStack button = ParentBuilder.of(Material.CHEST)
                .setDisplayName("<#e91e63:#fe909a>What is this menu?")
                .addDisplayLore("")
                .addDisplayLore("<red>A cheat sheet menu of all your available keys.")
                .addDisplayLore("<gold>Right click to get <bold><red>8</bold> <gold>keys.")
                .addDisplayLore("<gold>Left click to get <bold><red>1</bold> <gold>key.").build();

        gui.setItem(49, getItem(button));

        Player player = getPlayer();

        for (Key key : this.keyManager.getKeys()) {
            GuiItem item = getItem(key.getKey(player).build(), (event) -> {
                if (!Permissions.CRAZYCRATES_ACCESS.hasPermission(getPlayer())) {
                    player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                    player.sendMessage(Messages.no_permission.getMessage(player));

                    return;
                }

                ItemStack itemStack = event.getCurrentItem();

                if (itemStack == null) return;

                if (!itemStack.hasItemMeta()) return;

                Key value = this.keyManager.getKey(ItemUtils.getKey(itemStack.getItemMeta()));

                if (value == null) return;

                Map<String, String> placeholders = new HashMap<>();

                placeholders.put("{amount}", String.valueOf(1));
                placeholders.put("{key}", key.getKeyName());

                switch (event.getClick()) {
                    case LEFT -> {
                        this.userManager.addKeys(player.getUniqueId(), key.getKeyName(), key.isVirtual(), 1);

                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1f);

                        placeholders.put("{keytype}", KeyType.physical_key.getFriendlyName());
                        placeholders.put("{amount}", "1");

                        player.sendActionBar(Messages.obtaining_keys.getMessage(placeholders, player));
                    }

                    case RIGHT -> {
                        this.userManager.addKeys(player.getUniqueId(), key.getKeyName(), key.isVirtual(), 8);

                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1f);

                        placeholders.put("{keytype}", KeyType.physical_key.getFriendlyName());
                        placeholders.put("{amount}", "8");

                        player.sendActionBar(Messages.obtaining_keys.getMessage(placeholders, player));
                    }
                }
            });

            gui.setItem(gui.getInventory().firstEmpty(), item);
        }

        return this;
    }
}
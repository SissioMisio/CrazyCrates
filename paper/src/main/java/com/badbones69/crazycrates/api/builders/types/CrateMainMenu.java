package com.badbones69.crazycrates.api.builders.types;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.api.builders.InventoryBuilder;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.platform.crates.CrateManager;
import com.badbones69.crazycrates.platform.crates.KeyManager;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.platform.config.ConfigManager;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import java.util.List;

public class CrateMainMenu extends InventoryBuilder {

    private final @NotNull CrateManager crateManager = this.plugin.getCrateManager();
    private final @NotNull KeyManager keyManager = this.plugin.getKeyManager();

    private final @NotNull SettingsManager config = ConfigManager.getConfig();

    public CrateMainMenu(String title, int rows, Player player) {
        super(title, rows, player);
    }

    @Override
    public InventoryBuilder build() {
        Player player = getPlayer();
        Gui gui = getGui();

        if (this.config.getProperty(ConfigKeys.filler_toggle)) {
            String id = this.config.getProperty(ConfigKeys.filler_item);
            String name = this.config.getProperty(ConfigKeys.filler_name);
            List<String> lore = this.config.getProperty(ConfigKeys.filler_lore);

            gui.getFiller().fill(getItem(new ItemBuilder().setMaterial(id).setName(name).setLore(lore).setTarget(player).build()));
        }

        this.crateManager.getCrates().forEach(crate -> {
            int slot = crate.getSlot();

            if (crate.isInMenu()) {
                gui.setItem(slot, getItem(crate.getDisplayItem()));
            }
        });

        return this;
    }
}
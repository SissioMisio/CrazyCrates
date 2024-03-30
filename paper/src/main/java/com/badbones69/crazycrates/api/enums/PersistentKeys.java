package com.badbones69.crazycrates.api.enums;

import com.badbones69.crazycrates.CrazyCratesPaper;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public enum PersistentKeys {

    no_firework_damage("firework", PersistentDataType.BOOLEAN),
    cosmic_mystery_crate("cosmic_mystery_crate", PersistentDataType.INTEGER),
    cosmic_picked_crate("cosmic_picked_crate", PersistentDataType.INTEGER),
    preview_tier_button("preview_tier_button", PersistentDataType.STRING),
    main_menu_button("main_menu_button", PersistentDataType.STRING),
    selector_wand("selector_wand", PersistentDataType.STRING),
    back_button("back_button", PersistentDataType.STRING),
    next_button("next_button", PersistentDataType.STRING),
    crate_prize("crate_prize", PersistentDataType.STRING),
    crate_tier("crate_tier", PersistentDataType.STRING),
    crate_name("crate_name", PersistentDataType.STRING),
    crate_key("crate_key", PersistentDataType.STRING);

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final String key;
    private final PersistentDataType type;

    PersistentKeys(String key, PersistentDataType type) {
        this.key = key;
        this.type = type;
    }

    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(this.plugin, this.plugin.getName().toLowerCase() + "_" + this.key);
    }

    public PersistentDataType getType() {
        return this.type;
    }
}
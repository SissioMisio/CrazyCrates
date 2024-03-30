package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.simpleyaml.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;

public class Tier {

    private final String tierName;
    private final List<String> displayLore;
    private final ItemBuilder itemBuilder;
    private final String displayName;
    private final double chance;
    private final int slot;

    public Tier(String tier, ConfigurationSection section) {
        this.tierName = tier;

        this.displayLore = section.getStringList("Lore").isEmpty() ? Collections.emptyList() : section.getStringList("Lore");

        this.itemBuilder = new ItemBuilder().setMaterial(section.getString("Item", "chest"));

        this.displayName = section.getString("Name", "");

        this.chance = section.getDouble("Chance", 10.0);

        this.slot = section.getInt("Slot");
    }

    /**
     * @return the tier name.
     */
    public String getTierName() {
        return this.tierName;
    }

    /**
     * @return the item without the fluff.
     */
    public ItemBuilder getItem() {
        return this.itemBuilder;
    }

    /**
     * @return the item with the fluff.
     */
    public ItemStack getItem(Player player) {
        ItemBuilder builder = this.itemBuilder;

        builder.setTarget(player);

        builder.setName(this.displayName);

        builder.setLore(this.displayLore);

        ItemMeta itemMeta = builder.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        PersistentKeys key = PersistentKeys.preview_tier_button;

        container.set(key.getNamespacedKey(), key.getType(), this.tierName);

        builder.setItemMeta(itemMeta);

        return builder.build();
    }

    /**
     * @return display lore of the tier.
     */
    public List<String> getDisplayLore() {
        return this.displayLore;
    }

    /**
     * @return display name of the tier.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @return the chance of being picked.
     */
    public double getChance() {
        return this.chance;
    }

    /**
     * @return slot in the inventory.
     */
    public int getSlot() {
        return this.slot;
    }
}
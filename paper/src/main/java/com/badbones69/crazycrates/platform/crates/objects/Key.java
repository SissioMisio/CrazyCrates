package com.badbones69.crazycrates.platform.crates.objects;

import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.ryderbelserion.vital.items.ItemBuilder;
import com.ryderbelserion.vital.items.ParentBuilder;
import org.bukkit.entity.Player;
import us.crazycrew.crazycrates.platform.keys.KeyConfig;
import java.util.ArrayList;

public class Key {

    private final ItemBuilder key;
    private final String keyName;
    private boolean isVirtual;

    public Key(KeyConfig keyConfig) {
        this.key = new ParentBuilder().setMaterial(keyConfig.getMaterial())
                .setDisplayName(keyConfig.getItemName())
                .setDisplayLore(keyConfig.getLore())
                .setGlowing(keyConfig.isGlowing())
                .setUnbreakable(keyConfig.isUnbreakable())
                .addItemFlags(new ArrayList<>() {{
                    this.addAll(keyConfig.getItemFlags());
                }}).setString(PersistentKeys.crate_key.getNamespacedKey(), keyConfig.getKeyName());

        this.keyName = keyConfig.getKeyName();

        this.isVirtual = keyConfig.isVirtual();
    }

    public ItemBuilder getKey() {
        return this.key;
    }

    public ItemBuilder getKey(Player player) {
        return this.key.setTarget(player);
    }

    public ItemBuilder getKey(int amount) {
        return this.key.setAmount(amount);
    }

    public ItemBuilder getKey(Player player, int amount) {
        return this.key.setTarget(player).setAmount(amount);
    }

    public String getKeyName() {
        return this.keyName;
    }

    public boolean isVirtual() {
        return this.isVirtual;
    }

    public void setVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }
}
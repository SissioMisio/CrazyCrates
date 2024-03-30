package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.ryderbelserion.cluster.utils.RegistryUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.utils.EnchantUtil;
import java.util.*;

public class Prize {

    private final ConfigurationSection section;

    private ItemBuilder displayItem = new ItemBuilder();

    private final List<ItemBuilder> builders;
    private final List<String> commands;
    private final List<String> messages;
    private List<String> permissions = new ArrayList<>();
    private final Set<ItemStack> items = new HashSet<>();
    private final ArrayList<Tier> tiers = new ArrayList<>();

    private final String prizeName;
    private final String crateName;

    private boolean isFirework = false;

    private double chance = 0.0;

    private Prize alternativePrize;

    /**
     * Create a new prize.
     * This option is used only for Alternative Prizes.
     *
     * @param section the configuration section.
     */
    public Prize(ConfigurationSection section) {
        this.section = section;
        this.prizeName = this.section.getName();

        this.builders = ItemBuilder.convertStringList(section.getStringList("Items"), this.prizeName);

        this.commands = section.getStringList("Commands");
        this.messages = section.getStringList("Messages");

        this.crateName = "";
    }

    /**
     * Builds a prize object.
     *
     * @param section the config section of the prize.
     * @param crateTiers the tiers to check in the prize.
     * @param name the name of the crate.
     * @param crateType the type of the crate.
     */
    public Prize(ConfigurationSection section, List<Tier> crateTiers, String name, CrateType crateType) {
        this.section = section;

        this.prizeName = this.section.getName();

        this.crateName = name;

        this.commands = section.getStringList("Commands");
        this.messages = section.getStringList("Messages");

        this.isFirework = section.getBoolean("Firework", false);

        this.chance = section.getDouble("Chance", 25.0);

        this.displayItem = new ItemBuilder()
                .setMaterial(this.section.getString("DisplayItem", "red_terracotta"))
                .setName(this.section.getString("DisplayName", WordUtils.capitalizeFully(section.getString("DisplayItem", "red_terracotta").replaceAll("_", " "))))
                .setLore(section.getStringList("Lore"))
                .setGlow(section.getBoolean("Glowing", false))
                .setUnbreakable(section.getBoolean("Unbreakable", false))
                .hideItemFlags(section.getBoolean("HideItemFlags", false))
                .addItemFlags(section.getStringList("Flags"))
                .addPatterns(section.getStringList("Patterns"))
                .setPlayerName(section.getString("Player", " "))
                .setDamage(section.getInt("DisplayDamage", 0));

        if (this.section.contains("DisplayTrim.Pattern")) {
            TrimPattern pattern = RegistryUtils.getTrimPattern(this.section.getString("DisplayTrim.Pattern", "sentry").toLowerCase());

            if (pattern != null) {
                this.displayItem.setTrimPattern(pattern);
            }
        }

        if (this.section.contains("DisplayTrim.Material")) {
            TrimMaterial material = RegistryUtils.getTrimMaterial(this.section.getString("DisplayTrim.Material", "quartz").toLowerCase());

            if (material != null) {
                this.displayItem.setTrimMaterial(material);
            }
        }

        if (this.section.contains("DisplayEnchantments")) {
            for (String enchant : this.section.getStringList("DisplayEnchantments")) {
                Enchantment enchantment = RegistryUtils.getEnchantment(EnchantUtil.getEnchant(enchant.split(":")[0]));

                if (enchantment != null) {
                    this.displayItem.addEnchantment(enchantment, Integer.parseInt(enchant.split(":")[1]));
                }
            }
        }

        if (this.section.contains("Alternative-Prize")) {
            ConfigurationSection alternativeSection = this.section.getConfigurationSection("Alternative-Prize");

            if (alternativeSection != null) {
                boolean isEnabled = alternativeSection.getBoolean("Toggle");

                if (isEnabled) {
                    this.alternativePrize = new Prize(alternativeSection);
                }
            }
        }

        if (crateType == CrateType.casino) {
            if (this.section.contains("Tiers")) {
                for (String tier : this.section.getStringList("Tiers")) {
                    for (Tier key : crateTiers) {
                        if (key.getTierName().equalsIgnoreCase(tier)) {
                            this.tiers.add(key);
                        }
                    }
                }
            } else {
                if (!crateTiers.isEmpty()) {
                    @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);
                    List.of(
                            "The tiers are not defined in prize: " + this.prizeName,
                            "but the " + name + " file the prize is in has tiers defined.",
                            "Please attempt to fix this issue. You can see example files ",
                            "in the examples folder that refreshes when you reload the plugin."
                    ).forEach(plugin.getLogger()::warning);
                }
            }
        }

        List<?> editorItems = section.getList("Editor-Items");

        if (editorItems != null) {
            for (Object key : editorItems) {
                this.items.add((ItemStack) key);
            }
        }

        this.permissions = section.contains("BlackListed-Permissions") ? section.getStringList("BlackListed-Permissions") : Collections.emptyList();

        if (!this.permissions.isEmpty()) {
            this.permissions.replaceAll(String::toLowerCase);
        }

        this.builders = ItemBuilder.convertStringList(this.section.getStringList("Items"), this.prizeName);

        ItemMeta itemMeta = this.displayItem.getItemMeta();

        itemMeta.getPersistentDataContainer().set(PersistentKeys.crate_prize.getNamespacedKey(), PersistentDataType.STRING, this.section.getName());

        this.displayItem.setItemMeta(itemMeta);
    }

    /**
     * @return the configuration section of the prize.
     */
    public ConfigurationSection getSection() {
        return this.section;
    }

    /**
     * Gets the name of the section which can be "fuckyou" or "1"
     *
     * @return the name of the section.
     */
    public String getPrizeName() {
        return this.prizeName;
    }

    /**
     * @return A list of commands to execute.
     */
    public List<String> getCommands() {
        return this.commands;
    }

    /**
     * @return A list of messages to send.
     */
    public List<String> getMessages() {
        return this.messages;
    }

    /**
     * Whether to spawn a firework
     *
     * @return true or false
     */
    public boolean isFirework() {
        return this.isFirework;
    }

    /**
     * @return the max range of the chance.
     */
    public double getMaxRange() {
        return 100.0;
    }

    /**
     * @return the chance the prize can be won.
     */
    public double getChance() {
        return this.chance;
    }

    /**
     * Build an itemstack with no PlaceholderAPI support.
     *
     * @return an itemstack.
     */
    public ItemStack getDisplayItem() {
        return this.displayItem.build();
    }

    /**
     * @return a set of item stacks.
     */
    public Set<ItemStack> getItems() {
        return this.items;
    }

    /**
     * @return a list of tiers.
     */
    public List<Tier> getTiers() {
        return Collections.unmodifiableList(this.tiers);
    }

    /**
     * @return the alternative prize if not null.
     */
    public Prize getAlternativePrize() {
        return this.alternativePrize;
    }

    /**
     * @return true if the prize doesn't have an alternative prize and false if it does.
     */
    public boolean hasAlternativePrize() {
        return this.alternativePrize == null;
    }

    /**
     * @return a list of itemstacks built from the Items: section
     */
    public List<ItemBuilder> getItemBuilders() {
        return this.builders;
    }

    /**
     * Build an itemstack with PlaceholderAPI Support.
     *
     * @param player the player to use as reference.
     * @return an itemstack.
     */
    public ItemStack getDisplayItem(Player player) {
        return this.displayItem.setTarget(player).build();
    }

    /**
     * @return true if they prize has blacklist permissions and false if not.
     */
    public boolean hasPermission(Player player) {
        if (player.isOp()) {
            return false;
        }

        for (String permission : this.permissions) {
            if (player.hasPermission(permission)) return true;
        }

        return false;
    }

    /**
     * @return the name of the crate the prize is from.
     */
    public String getCrateName() {
        return this.crateName;
    }
}
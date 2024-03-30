package com.badbones69.crazycrates.api.objects;

import com.badbones69.crazycrates.CrazyCratesPaper;
import com.badbones69.crazycrates.api.builders.ItemBuilder;
import com.badbones69.crazycrates.api.builders.types.CratePreviewMenu;
import com.badbones69.crazycrates.api.builders.types.CrateTierMenu;
import com.badbones69.crazycrates.api.enums.Messages;
import com.badbones69.crazycrates.api.enums.PersistentKeys;
import com.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import com.badbones69.crazycrates.platform.utils.MiscUtils;
import com.badbones69.crazycrates.platform.utils.MsgUtils;
import com.badbones69.crazycrates.support.PluginSupport;
import com.badbones69.crazycrates.tasks.InventoryManager;
import com.badbones69.crazycrates.tasks.crates.effects.SoundEffect;
import com.badbones69.crazycrates.tasks.crates.other.CasinoCrateManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.api.crates.CrateHologram;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;
import us.crazycrew.crazycrates.platform.crates.types.AbstractCrateManager;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.regex.Matcher.quoteReplacement;

public class Crate {

    private final @NotNull CrazyCratesPaper plugin = JavaPlugin.getPlugin(CrazyCratesPaper.class);

    private final @NotNull InventoryManager inventoryManager = null;

    private final boolean executeCommands;
    private final List<String> commands;

    private final ConfigurationSection section;
    private final CrateHologram crateHologram;
    private final CrateType crateType;
    private final String previewName;
    private final String crateName;
    private final String name;

    private final int startingKeys;
    private final int requiredKeys;
    private final int maxMassOpen;

    private final boolean inMenu;
    private final int slot;

    private int maxPage = 1;
    private final int maxSlots;

    private final List<String> keys;

    private final boolean broadcastMessageToggle;
    private final String broadcastMessage;

    private List<ItemStack> previewItems;
    private final boolean previewToggle;
    private final int previewRows;

    private boolean tierFillerToggle;
    private ItemBuilder tierFillerItem;
    private int tierCrateRows;
    private int tierMaxSlots;

    private final boolean fillerToggle;
    private final ItemBuilder fillerItem;

    private final List<String> prizeMessage;

    private final ItemStack displayItem;

    private final List<Prize> prizes = new ArrayList<>();

    private final CrateConfig crateConfig;

    private final List<Tier> tiers = new ArrayList<>();

    private AbstractCrateManager crateManager;

    /**
     * Builds a crate object.
     *
     * @param crateConfig the config to use
     */
    public Crate(CrateConfig crateConfig) {
        this.crateConfig = crateConfig;

        this.section = crateConfig.getCrateSection();

        this.crateType = crateConfig.getCrateType();

        switch (this.crateType) {
            //case cosmic -> this.crateManager = new CosmicCrateManager(crateConfig);
            case casino -> this.crateManager = new CasinoCrateManager(crateConfig);
        }

        this.crateName = crateConfig.getCrateName();
        this.previewName = this.section.getString("Preview-Name", crateConfig.getName());

        this.name = crateConfig.getName();

        this.startingKeys = this.section.getInt("StartingKeys", 0);
        this.requiredKeys = this.section.getInt("RequiredKeys", 0);
        this.maxMassOpen = this.section.getInt("Max-Mass-Open", 10);
        this.inMenu = this.section.getBoolean("InGUI", true);
        this.slot = this.section.getInt("Slot");

        this.keys = this.section.getStringList("keys");

        this.broadcastMessageToggle = this.section.getBoolean("OpeningBroadCast", false);
        this.broadcastMessage = this.section.getString("BroadCast", "");

        this.executeCommands = this.section.getBoolean("opening-command.toggle", false);
        this.commands = this.section.getStringList("opening-command.commands");

        ConfigurationSection hologram = crateConfig.getHologramSection();

        this.crateHologram = new CrateHologram(
                hologram.getBoolean("Toggle", true),
                hologram.getDouble("Height", 1.5),
                hologram.getInt("Range", 8),
                hologram.getStringList("Message")
        );

        this.prizeMessage = this.section.getStringList("Prize-Message");

        this.displayItem = new ItemBuilder()
                .setMaterial(this.section.getString("Item", "CHEST"))
                .setName(this.section.getString("Name", " "))
                .setLore(this.section.getStringList("Lore"))
                .setGlow(this.section.getBoolean("Glowing", false))
                .setString(PersistentKeys.crate_name.getNamespacedKey(), this.name).build();

        ConfigurationSection preview = crateConfig.getPreviewSection();

        this.previewItems = getPreviewItems();
        this.previewToggle = preview.getBoolean("Toggle", true);
        this.previewRows = preview.getInt("ChestLines", 6);

        this.maxSlots = this.previewRows * 9;

        int max = this.maxSlots;
        int size = this.previewItems.size();
        boolean toggle = this.previewToggle;

        int value = max - (toggle ? 18 : max >= size ? 0 : max != 9 ? 9 : 0);
        for (int amount = size; amount > value; amount -= max - (toggle ? 18 : max != 9 ? 9 : 0), this.maxPage++);

        ConfigurationSection tierPreview = crateConfig.getTierPreviewSection();

        if (tierPreview != null) {
            this.tierCrateRows = tierPreview.getInt("rows", 5);
            this.tierMaxSlots = this.tierCrateRows * 9;

            this.tierFillerToggle = tierPreview.getBoolean("toggle", false);
            this.tierFillerItem = new ItemBuilder()
                    .setMaterial(tierPreview.getString("glass.item", "red_stained_glass_pane"))
                    .setName(tierPreview.getString("glass.name", " "));
        }

        this.fillerToggle = preview.getBoolean("Glass.Toggle", false);
        this.fillerItem = new ItemBuilder()
                .setMaterial(preview.getString("Glass.Item", "gray_stained_glass_pane"))
                .setName(preview.getString("Glass.Name", " "));

        ConfigurationSection section = crateConfig.getPrizeSection();

        if (section != null) {
            Set<String> keys = section.getKeys(false);

            for (String key : keys) {
                ConfigurationSection prizeSection = section.getConfigurationSection(key);

                if (prizeSection == null) continue;

                this.prizes.add(new Prize(prizeSection, this.tiers, this.name, this.crateType));
            }
        }

        if (this.crateManager != null) {
            this.crateManager.getTierSection().getKeys(false).forEach(tier -> {
                String path = "Crate.Tiers." + tier;

                ConfigurationSection tierSection = this.crateConfig.getConfigurationSection(path);

                if (tierSection != null) {
                    this.tiers.add(new Tier(tier, tierSection));
                }
            });

            if (section != null) {
                for (String key : section.getKeys(false)) {
                    ConfigurationSection prize = section.getConfigurationSection(key);

                    this.prizes.add(new Prize(prize, this.tiers, this.name, this.crateType));
                }
            }
        }

        PluginManager server = this.plugin.getServer().getPluginManager();

        if (server.getPermission("crazycrates.open." + getCrateName()) == null) {
            server.addPermission(new Permission("crazycrates.open." + getCrateName(), "Allows you to open " + getCrateName(), PermissionDefault.TRUE));
        }
    }

    /**
     * @return the crate manager.
     */
    public AbstractCrateManager getCrateManager() {
        return this.crateManager;
    }

    /**
     * @return the slot in the menu.
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * @return the max pages
     */
    public int getMaxPage() {
        return this.maxPage;
    }

    /**
     * Get the max amount of slots in the preview.
     *
     * @return the max number of slots in the preview.
     */
    public int getMaxSlots() {
        return this.maxSlots;
    }

    /**
     * @return A list of keys that can be used on the crate.
     */
    public List<String> getKeys() {
        return this.keys;
    }

    /**
     * @return the max amount of keys that can be used with /cc mass-open
     */
    public int getMaxMassOpen() {
        return this.maxMassOpen;
    }

    /**
     * @return A list of messages to send to a player.
     */
    public List<String> getPrizeMessage() {
        return this.prizeMessage;
    }

    /**
     * @return the keys required to open the crate.
     */
    public int getRequiredKeys() {
        return this.requiredKeys;
    }

    /**
     * @return the starting keys.
     */
    public int getStartingKeys() {
        return this.startingKeys;
    }

    /**
     * Should we give starting keys?
     *
     * @return true or false
     */
    public boolean isStartingKeys() {
        return getStartingKeys() >= 1;
    }

    /**
     * @return true or false
     */
    public boolean isPreviewToggle() {
        return this.previewToggle;
    }

    /**
     * Gets the inventory of a preview of prizes for the crate.
     *
     * @return the preview as an Inventory object.
     */
    public Inventory getPreview(Player player) {
        return getPreview(player, this.inventoryManager.getPage(player), false, null);
    }

    /**
     * Gets the inventory of a preview of prizes for the crate.
     *
     * @return the preview as an Inventory object.
     */
    public Inventory getPreview(Player player, int page, boolean isTier, Tier tier) {
        CratePreviewMenu cratePreviewMenu = new CratePreviewMenu(this.previewName, !this.fillerToggle && (this.inventoryManager.inCratePreview(player) || this.maxPage > 1) && this.maxSlots == 9 ? this.maxSlots + 9 : this.maxSlots, page, player, this, isTier, tier);

        return cratePreviewMenu.build().getGui().getInventory();
    }

    /**
     * Loads all the preview items and puts them into a list.
     *
     * @return a list of all the preview items that were created.
     */
    public List<ItemStack> getPreviewItems() {
        List<ItemStack> items = new ArrayList<>();

        for (Prize prize : getPrizes()) {
            items.add(prize.getDisplayItem());
        }

        return items;
    }

    /**
     * Loads all the preview items and puts them into a list.
     *
     * @return a list of all the preview items that were created.
     */
    public List<ItemStack> getPreviewItems(Player player) {
        List<ItemStack> items = new ArrayList<>();

        for (Prize prize : getPrizes()) {
            items.add(prize.getDisplayItem(player));
        }

        return items;
    }

    /**
     * Get prizes for tier specific preview gui's
     *
     * @param tier The tier to check
     * @return list of prizes
     */
    public List<ItemStack> getPreviewItems(Tier tier, Player player) {
        List<ItemStack> prizes = new ArrayList<>();

        for (Prize prize : getPrizes()) {
            if (prize.getTiers().contains(tier)) {
                prizes.add(prize.getDisplayItem(player));
            }
        }

        return prizes;
    }

    /**
     * Gets the inventory of a tier preview of prizes for the crate.
     *
     * @return the tier preview as an Inventory object.
     */
    public Inventory getTierPreview(Player player) {
        CrateTierMenu crateTierMenu = new CrateTierMenu(this.previewName, !this.tierFillerToggle && (this.inventoryManager.inCratePreview(player)) && this.tierMaxSlots == 9 ? this.tierMaxSlots + 9 : this.tierMaxSlots, player, this, getTiers());

        return crateTierMenu.build().getGui().getInventory();
    }

    /**
     * @param baseSlot - default slot to use.
     * @return the finalized slot.
     */
    public int getAbsoluteItemPosition(int baseSlot) {
        return baseSlot + (this.previewRows > 1 ? this.previewRows - 1 : 1) * 9;
    }

    /**
     * @param baseSlot - default slot to use.
     * @return the finalized slot.
     */
    public int getAbsolutePreviewItemPosition(int baseSlot) {
        return baseSlot + (this.tierCrateRows > 1 ? this.tierCrateRows - 1 : 1) * 9;
    }

    /**
     * @return the size of the crate preview
     */
    public int getPreviewRows() {
        return this.previewRows;
    }

    /**
     * @return true or false
     */
    public boolean isFillerToggle() {
        return this.fillerToggle;
    }

    /**
     * @return the filler item that appears in the gui.
     */
    public ItemStack getFillerItem() {
        return this.fillerItem.build();
    }

    /**
     * @return the filler item that appears in the gui.
     */
    public ItemStack getFillerItem(Player player) {
        return this.fillerItem.setTarget(player).build();
    }

    /**
     * @return true or false
     */
    public boolean isTierFillerToggle() {
        return this.tierFillerToggle;
    }

    /**
     * @return the max rows
     */
    public int getTierCrateRows() {
        return this.tierCrateRows;
    }

    /**
     * @return the max slots
     */
    public int getTierMaxSlots() {
        return this.tierMaxSlots;
    }

    /**
     * @return the filler item that appears in the gui.
     */
    public ItemStack getTierFillerItem() {
        return this.tierFillerItem.build();
    }

    /**
     * @return the filler item that appears in the gui.
     */
    public ItemStack getTierFillerItem(Player player) {
        return this.tierFillerItem.setTarget(player).build();
    }

    /**
     * @return the item stack that appears in the gui.
     */
    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    /**
     * Sends a message to a player.
     *
     * @param player the player to send to.
     * @param prize the prize messages to send if found.
     */
    public void sendMessage(Player player, Prize prize) {
        if (this.prizeMessage.isEmpty() && prize.getMessages().isEmpty()) {
            return;
        }

        ItemStack displayItem = prize.getDisplayItem(player);

        String displayName = displayItem.hasItemMeta() ? displayItem.getItemMeta().getDisplayName() : displayItem.getType().getKey().getKey().replaceAll("_", "");

        if (!this.prizeMessage.isEmpty() && prize.getMessages().isEmpty()) {
            for (String message : this.prizeMessage) {
                String value = message
                        .replaceAll("%player%", quoteReplacement(player.getName()))
                        .replaceAll("%crate%", quoteReplacement(this.crateName))
                        .replaceAll("%reward%", quoteReplacement(displayName));

                MsgUtils.sendMessage(player, MiscUtils.isPapiActive() ? PlaceholderAPI.setPlaceholders(player, value) : value, false);
            }

            return;
        }

        for (String message : prize.getMessages()) {
            String value = message
                    .replaceAll("%player%", quoteReplacement(player.getName()))
                    .replaceAll("%crate%", quoteReplacement(this.crateName))
                    .replaceAll("%reward%", quoteReplacement(displayName));

            MsgUtils.sendMessage(player, MiscUtils.isPapiActive() ? PlaceholderAPI.setPlaceholders(player, value) : value, false);
        }
    }

    public void playSound(Player player, String type, String fallback, SoundCategory category) {
        new SoundEffect(this.crateConfig.getSoundSection(), type, fallback, category).play(player, player.getLocation());
    }

    public void playSound(Player player, Location location, String type, String fallback, SoundCategory category) {
        new SoundEffect(this.crateConfig.getSoundSection(), type, fallback, category).play(player, location);
    }

    /**
     * This will broadcast a message to the entire server if the option is enabled.
     */
    public void broadcast(Player player) {
        if (!this.broadcastMessageToggle) return;

        if (this.broadcastMessage.isEmpty() || this.broadcastMessage.isBlank()) return;

        this.plugin.getServer().broadcastMessage(this.broadcastMessage.replaceAll("%prefix%", MsgUtils.getPrefix()).replaceAll("%player%", player.getName()));
    }

    /**
     * This will execute any commands on open.
     */
    public void execute(Player player) {
        if (!this.executeCommands) return;

        if (this.commands.isEmpty()) return;

        this.commands.forEach(command -> {
            String builder;

            if (MiscUtils.isPapiActive()) {
                builder = PlaceholderAPI.setPlaceholders(player, command.replaceAll("%prefix%", MsgUtils.getPrefix()).replaceAll("%player%", player.getName()));
            } else {
                builder = command.replaceAll("%prefix%", MsgUtils.getPrefix()).replaceAll("%player%", player.getName());
            }

            this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), builder);
        });
    }

    /**
     * @return if the item appears in the menu.
     */
    public boolean isInMenu() {
        return this.inMenu;
    }

    /**
     * @return the type of crate.
     */
    public CrateType getCrateType() {
        return this.crateType;
    }

    /**
     * @return the name of the crate.
     */
    public String getCrateName() {
        return this.crateName;
    }

    /**
     * @return the preview name used in the preview menu.
     */
    public String getPreviewName() {
        return this.previewName;
    }

    /**
     * @return the name of the crate file without .yml
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the hologram object.
     */
    public CrateHologram getCrateHologram() {
        return this.crateHologram;
    }

    /**
     * @return the crate section.
     */
    public ConfigurationSection getSection() {
        return this.section;
    }

    /**
     * Check to see if a player can win a prize from a crate.
     *
     * @param player the player you are checking.
     * @return true if they can win at least 1 prize and false if they can't win any.
     */
    public boolean canWinPrizes(Player player) {
        return pickPrize(player) != null;
    }

    /**
     * @return A list of prizes.
     */
    public List<Prize> getPrizes() {
        return Collections.unmodifiableList(this.prizes);
    }

    /**
     * Picks a random prize based on BlackList Permissions and the Chance System. Only used in the Cosmic Crate & Casino Type since it is the only one with tiers.
     *
     * @param player The player that will be winning the prize.
     * @param tier The tier you wish the prize to be from.
     * @return the winning prize based on the crate's tiers.
     */
    public Prize pickPrize(Player player, Tier tier) {
        List<Prize> prizes = new ArrayList<>();
        List<Prize> usablePrizes = new ArrayList<>();

        // ================= Blacklist Check ================= //
        if (player.isOp()) {
            for (Prize prize : getPrizes()) {
                if (prize.getTiers().contains(tier)) usablePrizes.add(prize);
            }
        } else {
            for (Prize prize : getPrizes()) {
                if (prize.hasPermission(player)) {
                    if (prize.hasAlternativePrize()) continue;
                }

                if (prize.getTiers().contains(tier)) usablePrizes.add(prize);
            }
        }

        // ================= Chance Check ================= //
        chanceCheck(prizes, usablePrizes);

        return prizes.get(MiscUtils.useOtherRandom() ? ThreadLocalRandom.current().nextInt(prizes.size()) : new Random().nextInt(prizes.size()));
    }

    /**
     * Picks a random prize based on BlackList Permissions and the Chance System.
     *
     * @param player the player that will be winning the prize.
     * @return the winning prize.
     */
    public Prize pickPrize(Player player) {
        List<Prize> prizes = new ArrayList<>();
        List<Prize> usablePrizes = new ArrayList<>();

        // ================= Blacklist Check ================= //
        if (player.isOp()) {
            usablePrizes.addAll(getPrizes());
        } else {
            for (Prize prize : getPrizes()) {
                if (prize.hasPermission(player)) {
                    if (prize.hasAlternativePrize()) continue;
                }

                usablePrizes.add(prize);
            }
        }

        // ================= Chance Check ================= //
        chanceCheck(prizes, usablePrizes);

        try {
            return prizes.get(MiscUtils.useOtherRandom() ? ThreadLocalRandom.current().nextInt(prizes.size()) : new Random().nextInt(prizes.size()));
        } catch (IllegalArgumentException exception) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to find prize from the " + name + " crate for player " + player.getName() + ".", exception);

            return null;
        }
    }

    /**
     * Picks a random prize based on BlackList Permissions and the Chance System. Spawns the display item at the location.
     *
     * @param player the player that will be winning the prize.
     * @param location the location the firework will spawn at.
     * @return the winning prize.
     */
    public Prize pickPrize(Player player, Location location) {
        Prize prize = pickPrize(player);

        if (prize.isFirework()) MiscUtils.spawnFirework(location, null);

        return prize;
    }

    /**
     * Checks the chances and returns usable prizes.
     *
     * @param prizes The prizes to check
     * @param usablePrizes The usable prizes to check
     */
    private void chanceCheck(List<Prize> prizes, List<Prize> usablePrizes) {
        for (int stop = 0; prizes.isEmpty() && stop <= 2000; stop++) {
            for (Prize prize : usablePrizes) {
                double max = prize.getMaxRange();
                double chance = prize.getChance();
                double num;

                for (int counter = 1; counter <= 1; counter++) {
                    num = MiscUtils.useOtherRandom() ? 1 + ThreadLocalRandom.current().nextDouble(max) : 1 + new Random().nextDouble(max);

                    if (num <= chance) prizes.add(prize);
                }
            }
        }
    }

    /**
     * Gives a prize to a player.
     *
     * @param player the player to get the prize.
     * @param prize the prize to give.
     */
    public void givePrize(Player player, Prize prize) {
        Logger logger = this.plugin.getLogger();

        if (prize == null) {
            if (MiscUtils.isLogging()) logger.warning("No prize was found when giving " + player.getName() + " a prize.");

            return;
        }

        for (ItemStack item : prize.getItems()) {
            if (item == null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("{crate}", getName());
                placeholders.put("{prize}", prize.getPrizeName());

                player.sendMessage(Messages.prize_error.getMessage(placeholders, player));

                continue;
            }

            if (!MiscUtils.isInventoryFull(player)) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        for (ItemBuilder item : prize.getItemBuilders()) {
            ItemBuilder clone = new ItemBuilder(item).setTarget(player);

            if (!MiscUtils.isInventoryFull(player)) {
                player.getInventory().addItem(clone.build());
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), clone.build());
            }
        }

        for (String command : prize.getCommands()) {
            if (command.contains("%random%:")) {
                String cmd = command;
                StringBuilder commandBuilder = new StringBuilder();

                for (String word : cmd.split(" ")) {
                    if (word.startsWith("%random%:")) {
                        word = word.replace("%random%:", "");

                        try {
                            long min = Long.parseLong(word.split("-")[0]);
                            long max = Long.parseLong(word.split("-")[1]);
                            commandBuilder.append(MiscUtils.pickNumber(min, max)).append(" ");
                        } catch (Exception e) {
                            commandBuilder.append("1 ");

                            this.plugin.getLogger().warning("The prize " + prize.getPrizeName() + " in the " + getName() + " crate has caused an error when trying to run a command.");
                            this.plugin.getLogger().warning("Command: " + cmd);
                        }
                    } else {
                        commandBuilder.append(word).append(" ");
                    }
                }

                command = commandBuilder.substring(0, command.length() - 1);
            }

            if (MiscUtils.isPapiActive()) command = PlaceholderAPI.setPlaceholders(player, command);

            String display = prize.getDisplayItem().getItemMeta().getDisplayName();

            String name = display.isEmpty() ? MsgUtils.color(WordUtils.capitalizeFully(prize.getDisplayItem().getType().getKey().getKey().replaceAll("_", " "))) : display;

            MiscUtils.sendCommand(command.replaceAll("%player%", player.getName()).replaceAll("%reward%", name).replaceAll("%crate%", getPreviewName()));
        }

        if (prize.isFirework()) MiscUtils.spawnFirework(player.getLocation().add(0, 1, 0), null);

        this.plugin.getServer().getPluginManager().callEvent(new PlayerPrizeEvent(player, this, getName(), prize));

        sendMessage(player, prize);
    }

    public void givePrize(Inventory inventory, int slot, Player player) {
        ItemStack itemStack = inventory.getItem(slot);

        if (itemStack == null) return;

        Prize prize = getPrize(itemStack);

        givePrize(player, prize);
    }

    /**
     * @param item the item to check.
     * @return the prize you asked for.
     */
    public Prize getPrize(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return getPrize(container.get(PersistentKeys.crate_prize.getNamespacedKey(), PersistentDataType.STRING));
    }

    /**
     * @param prizeName the prize name to check.
     * @return the prize you asked for.
     */
    public Prize getPrize(String prizeName) {
        Prize prize = null;

        for (Prize key : this.prizes) {
            if (key.getPrizeName().equalsIgnoreCase(prizeName)) {
                prize = key;
                break;
            }
        }

        return prize;
    }

    public Tier getTier() {
        if (this.tiers.isEmpty()) return null;

        Tier tier = null;

        for (int stopLoop = 0; stopLoop <= 100; stopLoop++) {
            for (Tier key : this.tiers) {
                double chance = key.getChance();

                double number = MiscUtils.useOtherRandom() ? ThreadLocalRandom.current().nextDouble(100.0) : new Random().nextDouble(100.0);

                if (number >= 1.0 && number <= chance) {
                    tier = key;
                    break;
                }
            }
        }

        return tier;
    }

    public Tier getTier(String tierName) {
        Tier tier = null;

        for (Tier key : this.tiers) {
            if (!key.getTierName().equalsIgnoreCase(tierName)) continue;

            tier = key;
            break;
        }

        return tier;
    }

    public List<Tier> getTiers() {
        return Collections.unmodifiableList(this.tiers);
    }
}
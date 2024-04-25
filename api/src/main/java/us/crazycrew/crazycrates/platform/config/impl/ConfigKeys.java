package us.crazycrew.crazycrates.platform.config.impl;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import java.util.Collections;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    protected ConfigKeys() {}

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Support: https://discord.gg/badbones-s-live-chat-182615261403283459",
                "Github: https://github.com/Crazy-Crew",
                "",
                "Issues: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "Features: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "",
                "List of all sounds: https://jd.papermc.io/paper/1.20/org/bukkit/Sound.html",
                "List of all enchantments: https://jd.papermc.io/paper/1.20/org/bukkit/enchantments/Enchantment.html"
        };

        String[] deprecation = {
                "",
                "Warning: This section is subject to change so it is considered deprecated.",
                "This is your warning before the change happens.",
                ""
        };

        conf.setComment("gui", "Settings related to guis.");
        conf.setComment("gui.inventory", "Inventory settings like size.");
        conf.setComment("gui.inventory.buttons", "The buttons in the gui.");

        conf.setComment("gui.inventory.buttons.menu", "The main menu button.");
        conf.setComment("gui.inventory.buttons.next", "The next button.");
        conf.setComment("gui.inventory.buttons.back", "The back button.");

        conf.setComment("gui.inventory.filler", "Allows you to fill the gui with a singular item.");

        conf.setComment("gui.inventory.customizer", "Allows you to configure items per slot.");

        conf.setComment("crate", "Settings related to crates.");
        conf.setComment("crate.preview", "The preview settings.");
        conf.setComment("crate.keys", "Settings related to how keys function.");

        conf.setComment("crate.unsupported-settings", "Settings that are not supported and can be removed at anytime.");

        conf.setComment("crate.keys.inventory-settings", "Settings related to a player's inventory is not empty.");

        conf.setComment("crate.quad-crate", "Settings related to QuadCrate");

        conf.setComment("root", header);
    }

    @Comment("Whether you want CrazyCrates to shut up or not, This option is ignored by errors.")
    public static final Property<Boolean> verbose_logging = newProperty("root.verbose_logging", true);

    @Comment("This option will let you test a different way of picking random numbers. If you have any issues, You can set it back to false.")
    public static final Property<Boolean> use_different_random = newProperty("root.use-different-random", false);

    @Comment({
            "Sends anonymous statistics about how the plugin is used to bstats.org.",
            "bstats is a service for plugin developers to find out how the plugin being used,",
            "This information helps us figure out how to better improve the plugin."
    })
    public static final Property<Boolean> toggle_metrics = newProperty("root.toggle_metrics", true);

    @Comment("The prefix used in commands")
    public static final Property<String> command_prefix = newProperty("root.command_prefix", "&8[&bCrazyCrates&8]: ");

    @Comment("The prefix used in console")
    public static final Property<String> console_prefix = newProperty("root.console_prefix", "&8[&bCrazyCrates&8] ");

    @Comment("If /crates should open the main menu. Warning: This will remove the menu button from crate previews.")
    public static final Property<Boolean> enable_crate_menu = newProperty("gui.toggle", true);

    @Comment("Whether to show the display item when opening QuickCrate")
    public static final Property<Boolean> show_quickcrate_item = newProperty("crate.quickcrate-display-item", true);

    //@Comment("Logs all crate actions to a .txt file if enabled. It is recommended to delete the file occasionally.")
    //public static final Property<Boolean> log_to_file = newProperty("crate.log-file", false);

    @Comment({
            "This option is unsupported and not recommended for use.",
            "It is not very performant and is recommended to keep false",
            "The option only here for niche use cases for the time being",
            "If at some point it ever gets more difficult to maintain this",
            "The option and code related will be completely removed and not added back."
    })
    public static final Property<Boolean> use_old_key_checks = newProperty("crate.unsupported-settings.old-key-checks", false);

    //@Comment("Logs all crate actions to console if enabled.")
    //public static final Property<Boolean> log_to_console = newProperty("crate.log-console", false);

    @Comment("The name of the gui.")
    public static final Property<String> inventory_name = newProperty("gui.inventory.name", "&b&lCrazy &4&lCrates");

    @Comment("The size of the gui. Valid sizes are 9,18,27,36,45")
    public static final Property<Integer> inventory_size = newProperty("gui.inventory.size", 45);

    @Comment("If crates should knock you back if you have no keys.")
    public static final Property<Boolean> knock_back = newProperty("crate.knock-back", true);

    @Comment("If players should be forced to exit out of the preview during /crates reload")
    public static final Property<Boolean> take_out_of_preview = newProperty("crate.preview.force-exit", false);

    @Comment("Send a message if they were forced out of the preview.")
    public static final Property<Boolean> send_preview_taken_out_message = newProperty("crate.preview.send-message", false);

    @Comment({
            "If a player gets to the menu related to the Prizes gui, Should they be timed out?",
            "",
            "It will wait 10 seconds and if they already collected 3 prizes, It will only give one prize."
    })
    public static final Property<Boolean> cosmic_crate_timeout = newProperty("crate.cosmic-crate-timeout", true);

    @Comment("Should a physical crate accept virtual keys?")
    public static final Property<Boolean> physical_accepts_virtual_keys = newProperty("crate.keys.physical-crate-accepts-virtual-keys", true);

    @Comment("Should a physical crate accept physical keys?")
    public static final Property<Boolean> physical_accepts_physical_keys = newProperty("crate.keys.physical-crate-accepts-physical-keys", true);

    @Comment("Should a virtual crate ( /crates ) accept physical keys?")
    public static final Property<Boolean> virtual_accepts_physical_keys = newProperty("crate.keys.virtual-crate-accepts-physical-keys", true);

    @Comment("Should the player should be given virtual keys if inventory is not empty? If you leave it as false, All keys will be dropped on the ground.")
    public static final Property<Boolean> give_virtual_keys_when_inventory_full = newProperty("crate.keys.inventory-settings.give-virtual-keys", false);

    @Comment("Should the player should be notified when their inventory is not empty?")
    public static final Property<Boolean> notify_player_when_inventory_full = newProperty("crate.keys.inventory-settings.send-message", false);

    @Comment("Should a sound should be played if they have no key?")
    public static final Property<Boolean> need_key_sound_toggle = newProperty("crate.keys.key-sound.toggle", true);

    @Comment("The sound to play.")
    public static final Property<String> need_key_sound = newProperty("crate.keys.key-sound.name", "ENTITY_VILLAGER_NO");

    @Comment("How long should the quad crate be active?")
    public static final Property<Integer> quad_crate_timer = newProperty("crate.quad-crate.timer", 300);

    @Comment("What worlds do you want Crates to be disabled in?")
    public static final Property<List<String>> disabled_worlds = newListProperty("crate.disabled-worlds", List.of(
            "world_nether"
    ));

    @Comment("The item the button should be.")
    public static final Property<String> menu_button_item = newProperty("gui.inventory.buttons.menu.item", "COMPASS");

    @Comment({
            "This will disable our current functionality of our main menu button in crate previews.",
            "It allows you to override and use a menu of your choice from your plugin using a command."
    })
    public static final Property<Boolean> menu_button_override = newProperty("gui.inventory.buttons.menu.override.toggle", false);

    @Comment({
            "A list of commands to run when the main menu button is clicked. The override option above has to be set to true.",
    })
    public static final Property<List<String>> menu_button_command_list = newListProperty("gui.inventory.buttons.menu.override.list", List.of("see {player}"));

    @Comment("The name of the item.")
    public static final Property<String> menu_button_name = newProperty("gui.inventory.buttons.menu.name", "&7&l>> &c&lMenu &7&l<<");

    @Comment("The lore of the item.")
    public static final Property<List<String>> menu_button_lore = newListProperty("gui.inventory.buttons.menu.lore", List.of(
            "&7Return to the menu."
    ));

    @Comment("The item the button should be.")
    public static final Property<String> next_button_item = newProperty("gui.inventory.buttons.next.item", "FEATHER");

    @Comment("The name of the item.")
    public static final Property<String> next_button_name = newProperty("gui.inventory.buttons.next.name", "&6&lNext >>");

    @Comment("The lore of the item.")
    public static final Property<List<String>> next_button_lore = newListProperty("gui.inventory.buttons.next.lore", List.of(
            "&7&lPage: &b{page}"
    ));

    @Comment("The item the button should be.")
    public static final Property<String> back_button_item = newProperty("gui.inventory.buttons.back.item", "FEATHER");

    @Comment("The name of the item.")
    public static final Property<String> back_button_name = newProperty("gui.inventory.buttons.back.name", "&6&l<< Back");

    @Comment("The lore of the item.")
    public static final Property<List<String>> back_button_lore = newListProperty("gui.inventory.buttons.back.lore", List.of(
            "&7&lPage: &b{page}"
    ));

    @Comment("Should the menu should be filled with one type of item?")
    public static final Property<Boolean> filler_toggle = newProperty("gui.inventory.buttons.filler.toggle", false);

    @Comment("The item to fill the menu with.")
    public static final Property<String> filler_item = newProperty("gui.inventory.buttons.filler.item", "BLACK_STAINED_GLASS_PANE");

    @Comment("The name of the item.")
    public static final Property<String> filler_name = newProperty("gui.inventory.buttons.filler.name", " ");

    @Comment("The lore of the item.")
    public static final Property<List<String>> filler_lore = newListProperty("gui.inventory.buttons.filler.lore", Collections.emptyList());

    @Comment("Should the customizer should be enabled?")
    public static final Property<Boolean> gui_customizer_toggle = newProperty("gui.inventory.buttons.customizer.toggle", true);

    @Comment("The items to set to the gui.")
    public static final Property<List<String>> gui_customizer = newListProperty("gui.inventory.buttons.customizer.items", List.of(
            "slot:1, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:2, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:3, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:4, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:5, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:6, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:7, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:8, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:9, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:37, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:38, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:39, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:40, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:41, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:42, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:43, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:44, item:RED_STAINED_GLASS_PANE, name: ",
            "slot:45, item:RED_STAINED_GLASS_PANE, name: ",

            "slot:10, item:BLUE_STAINED_GLASS_PANE, name: ",
            "slot:19, item:BLUE_STAINED_GLASS_PANE, name: ",
            "slot:28, item:BLUE_STAINED_GLASS_PANE, name: ",
            "slot:18, item:BLUE_STAINED_GLASS_PANE, name: ",
            "slot:27, item:BLUE_STAINED_GLASS_PANE, name: ",
            "slot:36, item:BLUE_STAINED_GLASS_PANE, name: ",

            "slot:11, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:13, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:15, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:25, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:17, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:20, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:21, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:22, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:23, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:24, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:25, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:26, item:CYAN_STAINED_GLASS_PANE, name: ",

            "slot:29, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:31, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:32, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:33, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:34, item:CYAN_STAINED_GLASS_PANE, name: ",
            "slot:35, item:CYAN_STAINED_GLASS_PANE, name: "
    ));
}
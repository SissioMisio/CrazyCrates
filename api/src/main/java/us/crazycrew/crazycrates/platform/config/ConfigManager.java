package us.crazycrew.crazycrates.platform.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import us.crazycrew.crazycrates.platform.config.impl.ConfigKeys;
import us.crazycrew.crazycrates.platform.config.impl.messages.*;

import java.io.File;

public class ConfigManager {

    private static SettingsManager config;

    private static SettingsManager messages;

    public static void load(File dataFolder) {
        config = SettingsManagerBuilder
                .withYamlFile(new File(dataFolder, "config.yml"), getBuilder())
                .useDefaultMigrationService()
                .configurationData(ConfigKeys.class)
                .create();

        messages = SettingsManagerBuilder
                .withYamlFile(new File(dataFolder, "messages.yml"), getBuilder())
                .useDefaultMigrationService()
                .configurationData(MiscKeys.class, ErrorKeys.class, PlayerKeys.class, CrateKeys.class, CommandKeys.class)
                .create();
    }

    public static YamlFileResourceOptions getBuilder() {
        return YamlFileResourceOptions.builder().indentationSize(2).build();
    }

    public static void reload() {
        config.reload();

        messages.reload();
    }

    public static void save() {
        config.save();

        messages.save();
    }

    public static SettingsManager getConfig() {
        return config;
    }

    public static SettingsManager getMessages() {
        return messages;
    }
}
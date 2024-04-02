package us.crazycrew.crazycrates.platform.crates;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import java.io.File;
import java.io.IOException;

public class CrateConfig extends YamlConfiguration {

    private final File file;
    private final String name;

    /**
     * Creates a config object.
     *
     * @param file the file to bind
     */
    public CrateConfig(File file) {
        this.file = file;

        this.name = file.getName().replaceAll(".yml", "");
    }

    /**
     * Loads the crate config file.
     *
     * @throws IOException if the file fails to load
     */
    public void load() throws IOException, InvalidConfigurationException {
        load(this.file);
    }

    /**
     * @return the name of the file without .yml
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * @return the crate section.
     */
    public ConfigurationSection getCrateSection() {
        return getConfigurationSection("Crate");
    }

    /**
     * @return the sound section.
     */
    public ConfigurationSection getSoundSection() {
        return getCrateSection().getConfigurationSection("sound");
    }

    /**
     * @return the preview section.
     */
    public ConfigurationSection getPreviewSection() {
        return getCrateSection().getConfigurationSection("Preview");
    }

    /**
     * @return the tier preview section.
     */
    public ConfigurationSection getTierPreviewSection() {
        return getCrateSection().getConfigurationSection("tier-preview");
    }

    /**
     * @return the hologram section.
     */
    public ConfigurationSection getHologramSection() {
        return getCrateSection().getConfigurationSection("Hologram");
    }

    /**
     * @return the prize section.
     */
    public ConfigurationSection getPrizeSection() {
        return getCrateSection().getConfigurationSection("Prizes");
    }

    /**
     * @return the crate type or CSGO if none is found.
     */
    public CrateType getCrateType() {
        return CrateType.getFromName(getCrateSection().getString("CrateType", "CSGO"));
    }

    /**
     * @return the name of the crate.
     */
    public String getCrateName() {
        return getCrateSection().getString("CrateName", " ");
    }
}
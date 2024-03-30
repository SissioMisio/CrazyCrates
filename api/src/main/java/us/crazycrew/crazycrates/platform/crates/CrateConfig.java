package us.crazycrew.crazycrates.platform.crates;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;
import us.crazycrew.crazycrates.api.enums.types.CrateType;

import java.io.File;
import java.io.IOException;

public class CrateConfig extends YamlFile {

    private final YamlFile file;
    private final String name;

    public CrateConfig(File file) {
        this.file = new YamlFile(file.getPath());

        this.name = file.getName().replaceAll(".yml", "");
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return this.file.getConfigurationSection(path);
    }

    @Override
    public void load() throws IOException {
        this.file.createOrLoadWithComments();
    }

    /**
     * @return the name of the file without .yml
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the crate file.
     */
    public YamlFile getFile() {
        return this.file;
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
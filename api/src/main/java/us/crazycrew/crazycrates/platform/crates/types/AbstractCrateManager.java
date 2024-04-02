package us.crazycrew.crazycrates.platform.crates.types;

import org.bukkit.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;

public abstract class AbstractCrateManager {

    protected final CrateConfig config;

    protected final ConfigurationSection section;

    public AbstractCrateManager(CrateConfig config) {
        this.config = config;

        this.section = this.config.getCrateSection();
    }

    public ConfigurationSection getTierSection() {
        return this.section.getConfigurationSection("Tiers");
    }

    public boolean isTierPreviewEnabled() {
        return this.section.getBoolean("tier-preview.toggle", true);
    }

    public int getTierPreviewRows() {
        return this.section.getInt("tier-preview.rows", 5);
    }

    public boolean isTierPreviewFillerEnabled() {
        return this.section.getBoolean("tier-preview.glass.toggle", true);
    }

    public String getTierPreviewFillerName() {
        return this.section.getString("tier-preview.glass.name", " ");
    }

    public String getTierPreviewFillerItem() {
        return this.section.getString("tier-preview.glass.item", "red_stained_glass_pane");
    }
}
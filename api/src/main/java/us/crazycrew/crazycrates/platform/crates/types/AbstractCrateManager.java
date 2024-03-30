package us.crazycrew.crazycrates.platform.crates.types;

import org.simpleyaml.configuration.ConfigurationSection;
import us.crazycrew.crazycrates.platform.crates.CrateConfig;

public abstract class AbstractCrateManager {

    private final CrateConfig config;

    public AbstractCrateManager(CrateConfig config) {
        this.config = config;
    }

    public ConfigurationSection getTierSection() {
        return this.config.getCrateSection().getConfigurationSection("Tiers");
    }

    public boolean isTierPreviewEnabled() {
        return this.config.getCrateSection().getBoolean("tier-preview.toggle", true);
    }

    public int getTierPreviewRows() {
        return this.config.getCrateSection().getInt("tier-preview.rows", 5);
    }

    public boolean isTierPreviewFillerEnabled() {
        return this.config.getCrateSection().getBoolean("tier-preview.glass.toggle", true);
    }

    public String getTierPreviewFillerName() {
        return this.config.getCrateSection().getString("tier-preview.glass.name", " ");
    }

    public String getTierPreviewFillerItem() {
        return this.config.getCrateSection().getString("tier-preview.glass.item", "red_stained_glass_pane");
    }
}
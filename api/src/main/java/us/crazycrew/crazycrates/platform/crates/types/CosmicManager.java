package us.crazycrew.crazycrates.platform.crates.types;

import us.crazycrew.crazycrates.platform.crates.CrateConfig;

import java.util.List;

public abstract class CosmicManager extends AbstractCrateManager {

    private final CrateConfig config;

    public CosmicManager(CrateConfig config) {
        super(config);

        this.config = config;
    }

    public int getTotalPrizeAmount() {
        return this.config.getCrateSection().getInt("Crate-Type-Settings.Total-Prize-Amount", 4);
    }

    public String getMysteryCrateItem() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Mystery-Crate.Item", "CHEST");
    }

    public String getMysteryCrateName() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Mystery-Crate.Name", "&f&l???");
    }

    public List<String> getMysteryCrateLore() {
        return this.config.getCrateSection().getStringList("Crate-Type-Settings.Mystery-Crate.Lore");
    }

    public String getPickedCrateItem() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Picked-Crate.Item", "CHEST");
    }

    public String getPickedCrateName() {
        return this.config.getCrateSection().getString("Crate-Type-Settings.Picked-Crate.Name", "&f&l???");
    }

    public List<String> getPickedCrateLore() {

        return this.config.getCrateSection().getStringList("Crate-Type-Settings.Picked-Crate.Lore");
    }
}
package us.crazycrew.crazycrates.platform.crates.types;

import us.crazycrew.crazycrates.platform.crates.CrateConfig;
import java.util.List;

public abstract class CosmicManager extends AbstractCrateManager {

    public CosmicManager(CrateConfig config) {
        super(config);
    }

    public int getTotalPrizeAmount() {
        return this.section.getInt("Crate-Type-Settings.Total-Prize-Amount", 4);
    }

    public String getMysteryCrateItem() {
        return this.section.getString("Crate-Type-Settings.Mystery-Crate.Item", "CHEST");
    }

    public String getMysteryCrateName() {
        return this.section.getString("Crate-Type-Settings.Mystery-Crate.Name", "&f&l???");
    }

    public List<String> getMysteryCrateLore() {
        return this.section.getStringList("Crate-Type-Settings.Mystery-Crate.Lore");
    }

    public String getPickedCrateItem() {
        return this.section.getString("Crate-Type-Settings.Picked-Crate.Item", "CHEST");
    }

    public String getPickedCrateName() {
        return this.section.getString("Crate-Type-Settings.Picked-Crate.Name", "&f&l???");
    }

    public List<String> getPickedCrateLore() {
        return this.section.getStringList("Crate-Type-Settings.Picked-Crate.Lore");
    }
}
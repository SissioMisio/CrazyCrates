package us.crazycrew.crazycrates.platform.crates.types;

import us.crazycrew.crazycrates.platform.crates.CrateConfig;

public class CasinoManager extends AbstractCrateManager {

    public CasinoManager(CrateConfig config) {
        super(config);
    }

    public boolean isRandom() {
        return this.section.getBoolean("random.toggle", false);
    }

    public String getTierOne() {
        return this.section.getString("random.types.row-1", "Basic");
    }

    public String getTierTwo() {
        return this.section.getString("random.types.row-2", "UnCommon");
    }

    public String getTierThree() {
        return this.section.getString("random.types.row-3", "Rare");
    }
}
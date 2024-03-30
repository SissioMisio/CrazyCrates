package us.crazycrew.crazycrates.platform.crates.types;

import us.crazycrew.crazycrates.platform.crates.CrateConfig;

public class CasinoManager extends AbstractCrateManager {

    private final CrateConfig config;

    public CasinoManager(CrateConfig config) {
        super(config);

        this.config = config;
    }

    public boolean isTiersRandom() {
        return this.config.getCrateSection().getBoolean("random.toggle", false);
    }

    public String getTierOne() {
        return this.config.getCrateSection().getString("random.types.row-1", "Basic");
    }

    public String getTierTwo() {
        return this.config.getCrateSection().getString("random.types.row-2", "UnCommon");
    }

    public String getTierThree() {
        return this.config.getCrateSection().getString("random.types.row-3", "Rare");
    }
}
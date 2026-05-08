package com.caiocesarmods.dtbrbiomesmod.systems;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SeasonalLeafRegistry {

    private static final Map<ResourceLocation, SeasonalLeafConfig>
            CONFIGS = new HashMap<>();

    public static void clear() {
        CONFIGS.clear();
    }

    public static void register(SeasonalLeafConfig config) {

        if (config == null || config.species == null) {
            return;
        }

        CONFIGS.put(config.species, config);
    }

    public static SeasonalLeafConfig get(ResourceLocation species) {
        return CONFIGS.get(species);
    }
}

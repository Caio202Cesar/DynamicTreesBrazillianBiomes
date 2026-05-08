package com.caiocesarmods.dtbrbiomesmod.systems.genfeatures;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import net.minecraft.util.ResourceLocation;

public class ModGenFeatures {

    public static final GenFeature SEASONAL_ALTERNATIVE_LEAVES = new SeasonalAlternativeLeavesGenFeature(regName("seasonal_alt_leaves"));

    private static ResourceLocation regName(String name) {
        return new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID, name);
    }

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(SEASONAL_ALTERNATIVE_LEAVES);
    }
}

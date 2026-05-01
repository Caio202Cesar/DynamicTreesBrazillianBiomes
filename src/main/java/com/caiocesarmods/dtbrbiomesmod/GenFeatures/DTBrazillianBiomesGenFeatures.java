package com.caiocesarmods.dtbrbiomesmod.GenFeatures;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.caiocesarmods.dtbrbiomesmod.GenFeatures.FruitBunchGenFeatures.AcaiBunchGenerator;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import net.minecraft.util.ResourceLocation;

public class DTBrazillianBiomesGenFeatures {

    public static GenFeature ACAI_BUNCH_FEATURE = new AcaiBunchGenerator(
            new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID,"acai_bunch")
    );

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(ACAI_BUNCH_FEATURE);
    }
}

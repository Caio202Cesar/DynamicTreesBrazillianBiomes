package com.caiocesarmods.dtbrbiomesmod.GenFeatures;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.caiocesarmods.dtbrbiomesmod.GenFeatures.FruitBunchGenFeatures.FeatureGenAcaiBunch;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import net.minecraft.util.ResourceLocation;

public class DTBrazillianBiomesGenFeatures {

    public static GenFeature ACAI_BUNCH_FEATURE = new FeatureGenAcaiBunch(
            new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID,"acai_bunch")
    );
/*
    public static final GenFeature JABUTICABA_FLOWERING_BRANCH = new FeatureGenJabuticabaFloweringBranch(
            new ResourceLocation(BrazillianBiomesDTAddon.MOD_ID,"jabuticaba_flowering_branch")
    );*/

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(ACAI_BUNCH_FEATURE);
       // registry.registerAll(JABUTICABA_FLOWERING_BRANCH);
    }
}

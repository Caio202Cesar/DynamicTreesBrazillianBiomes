package com.caiocesarmods.dtbrbiomesmod.Trees.Species;

import com.caiocesarmods.dtbrbiomesmod.Trees.Family.IpeFamily;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;

public class PurpleIpeSpecies extends Species {

    public PurpleIpeSpecies(IpeFamily family) {
        super(new ResourceLocation("dtbrbiomesmod", "purple_ipe"), family);

        this.setShouldGenerateSeedIfNull(true);
        this.generateSeed();

        setBasicGrowingParameters(0.9f, 13f, 3, 3, 1.0f);
    }
}

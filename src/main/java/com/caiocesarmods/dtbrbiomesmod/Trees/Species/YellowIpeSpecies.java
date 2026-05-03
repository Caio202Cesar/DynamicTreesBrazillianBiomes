package com.caiocesarmods.dtbrbiomesmod.Trees.Species;

import com.caiocesarmods.dtbrbiomesmod.Trees.Family.IpeFamily;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;

public class YellowIpeSpecies extends Species {

    public YellowIpeSpecies(IpeFamily family) {

        super(new ResourceLocation("dtbrbiomesmod", "yellow_ipe"), family);

        this.setShouldGenerateSeedIfNull(true);
        this.generateSeed();

        setBasicGrowingParameters(0.9f, 13f, 3, 3, 1.0f);
    }
}

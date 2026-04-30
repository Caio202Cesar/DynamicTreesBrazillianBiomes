package com.caiocesarmods.dtbrbiomesmod.Trees.Species;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;

public class CashewSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(CashewSpecies::new);

    public CashewSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);

        this.setBasicGrowingParameters(
                0.4f,   // growth rate (low)
                9.0f,   // energy (LOW = short tree)
                2,      // lowest branch height
                2,
                1.2f
        );

        this.setSoilLongevity(6);

        // 🌿 Makes branches thinner → more spread (bushy)
        this.setTapering(0.5f);
    }

}

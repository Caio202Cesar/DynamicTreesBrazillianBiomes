/*package com.caiocesarmods.dtbrbiomesmod.Trees.Species;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Random;

public class BrazillianRosewoodFloweringSpecies extends Species {
    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(BrazillianRosewoodFloweringSpecies::new);

    public BrazillianRosewoodFloweringSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name.toString());
        this.family = family;
        this.family.addSpecies(this);
        this.setLeavesProperties(leavesProperties.isValid() ? leavesProperties : family.getCommonLeaves());
        this.setCommonOverride(this.commonOverride);
    }

    public boolean shouldOverrideCommon(IBlockReader world, BlockPos trunkPos) {
        return (new Random(trunkPos.func_218275_a())).nextBoolean();
    }

    public boolean hasCommonOverride() {
        return true;
    }

    public void setCommonOverride(Species.CommonOverride commonOverride) {
        commonOverride = (world, trunkPos) -> (new Random(trunkPos.func_218275_a())).nextBoolean();
        this.commonOverride = commonOverride;
    }

}*/



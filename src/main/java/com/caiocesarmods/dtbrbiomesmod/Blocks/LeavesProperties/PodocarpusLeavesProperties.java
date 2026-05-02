package com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesProperties;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class PodocarpusLeavesProperties extends LeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(PodocarpusLeavesProperties::new);

    public PodocarpusLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(final AbstractBlock.Properties properties) {
        return new DynamicLeavesBlock(this, properties) {
            @OnlyIn(Dist.CLIENT)
            public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
                super.animateTick(state, world, pos, random);

            }
        };
    }
}

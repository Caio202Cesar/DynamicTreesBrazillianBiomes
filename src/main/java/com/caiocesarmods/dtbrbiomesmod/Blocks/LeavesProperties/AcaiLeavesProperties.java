package com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesProperties;

import com.caiocesarmods.dtbrbiomesmod.Blocks.FruitBlocks.AcaiFruitBlock;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class AcaiLeavesProperties extends PalmLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(AcaiLeavesProperties::new);

    public AcaiLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicPalmLeavesBlock(this, properties){
            //When destroying, we update the fruit below if one is there.
            private void updateFruit (IWorld world, BlockPos pos, int offset){
                BlockState downState = world.getBlockState(pos.down(offset));
                if (downState.getBlock() instanceof AcaiFruitBlock){
                    downState.onNeighborChange(world, pos.down(offset), pos.down(offset).up());
                }
            }
            @Override
            public void destroy(IWorld world, BlockPos pos, BlockState state) {
                updateFruit(world, pos, 2);
                super.destroy(world, pos, state);
            }
            @Override
            public void onPlace(BlockState thisState, World world, BlockPos pos, BlockState oldState, boolean bool) {
                updateFruit(world, pos, 2);
                updateFruit(world, pos, 3);
                super.onPlace(thisState, world, pos, oldState, bool);
            }

            @Deprecated
            public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader reader, BlockPos pos) {
                float f = state.getDestroySpeed(reader, pos);
                if (f == -1.0F) {
                    return 0.0F;
                } else {
                    int i = net.minecraftforge.common.ForgeHooks.canHarvestBlock(state, player, reader, pos) ? 30 : 100;
                    return player.getDigSpeed(state, pos) / f / (float)i;
                }
            }
        };
    }
}

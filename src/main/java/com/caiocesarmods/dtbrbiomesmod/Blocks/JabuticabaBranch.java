/*package com.caiocesarmods.dtbrbiomesmod.Blocks;

import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class JabuticabaBranch extends BasicBranchBlock {

    public static final BooleanProperty IS_FLOWERING = BooleanProperty.create("is_flowering");

    public JabuticabaBranch(ResourceLocation name, Properties properties) {
        super(name, properties);

        for (int i = 0; i < this.branchStates.length; i++) {
            if (this.branchStates[i].is(this))
                this.branchStates[i] = this.branchStates[i].setValue(IS_FLOWERING, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(IS_FLOWERING));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(RADIUS) > 6;
        // return state.getValue(IS_FLOWERING) ;
    }

    // here plan when the tree can grow then try leflowering
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (random.nextInt(25) == 0) {
            }
    }


}*/
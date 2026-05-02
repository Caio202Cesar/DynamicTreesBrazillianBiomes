package com.caiocesarmods.dtbrbiomesmod.Blocks;
/*
import com.brbiomesmod.block.TreesGroup;
import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.util.Optionals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class JabuticabaFloweringBranch extends BasicBranchBlock {

    public static final BooleanProperty IS_FLOWERING = BooleanProperty.create("is_flowering");

    public JabuticabaFloweringBranch(ResourceLocation name, Properties properties) {
        super(name, properties);

        for (int i = 0; i < this.branchStates.length; i++) {
            if(this.branchStates[i].is(this))
                this.branchStates[i] = this.branchStates[i].setValue(IS_FLOWERING, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(IS_FLOWERING));
    }

    @Override
    public boolean func_149653_t(BlockState state) {
        return state.getValue(RADIUS)>6;
        // return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (random.nextInt(15) == 0) {
            world.setBlockAndUpdate(pos, state.setValue(IS_FLOWERING, !state.getValue(IS_FLOWERING)));
        }
    }



    @Override
    public boolean shouldGenerateBranchDrops() {
        return super.shouldGenerateBranchDrops();
    }


    @Override
    public Optional<Block> getPrimitiveLog() {
        return Optionals.ofBlock(TreesGroup.JABUTICABA_FLOWERING_LOG.get());
    }


    @Override
    public float getPrimitiveLogs(float volumeIn, List<ItemStack> drops) {
        return super.getPrimitiveLogs(volumeIn, drops);
    }
}*/
package com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesBlocks;

import com.brbiomesmod.Seasons.Season;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Supplier;

public class CashewDynamicLeaves extends DynamicLeavesBlock implements IRandomTickable {
    private final Supplier<Block> nextStage;

    public CashewDynamicLeaves(LeavesProperties leavesProperties, Properties properties, Supplier<Block> nextStage) {
        super(leavesProperties, properties);
        this.nextStage = nextStage;

    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        String currentSeason = Season.getSeason(world.getDayTime());

        if ("SPRING".equals(currentSeason) && nextStage != null && rand.nextInt(30) == 0) {

            int distance = state.get(LeavesBlock.DISTANCE);
            boolean persistent = state.get(LeavesBlock.PERSISTENT);

            BlockState newState = nextStage.get().getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, persistent);

            world.setBlockState(pos, newState, 2);
        }
    }
}

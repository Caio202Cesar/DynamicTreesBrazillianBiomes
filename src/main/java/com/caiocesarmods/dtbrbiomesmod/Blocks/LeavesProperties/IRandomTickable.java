package com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesProperties;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public interface IRandomTickable {

    void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random);
}

package com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesBlocks;

import com.ferreusveritas.dynamictrees.api.Ageable;
import net.minecraft.block.BlockState;

public interface IRandomTickable extends Ageable {
    boolean isRandomlyTicking(BlockState state);
}

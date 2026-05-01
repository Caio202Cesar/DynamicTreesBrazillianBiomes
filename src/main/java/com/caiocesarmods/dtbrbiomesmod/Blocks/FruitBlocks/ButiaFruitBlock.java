package com.caiocesarmods.dtbrbiomesmod.Blocks.FruitBlocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ButiaFruitBlock extends FruitBlock {
    public ButiaFruitBlock(Properties properties, Fruit fruit) {
        super(properties, fruit);
    }

    @Override
    public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up(2)).getBlock() instanceof LeavesBlock;
    }

}

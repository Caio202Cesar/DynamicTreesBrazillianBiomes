package com.caiocesarmods.dtbrbiomesmod.Fruits;

import com.caiocesarmods.dtbrbiomesmod.Blocks.FruitBlocks.ButiaFruitBlock;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;

public class ButiaFruitBunch extends Fruit {
    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(ButiaFruitBunch::new);

    public ButiaFruitBunch(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(AbstractBlock.Properties properties) {
        return new ButiaFruitBlock(properties, this);
    }

}


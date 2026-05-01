package com.caiocesarmods.dtbrbiomesmod.Fruits;

import com.caiocesarmods.dtbrbiomesmod.Blocks.FruitBlocks.AcaiFruitBlock;
import com.caiocesarmods.dtbrbiomesmod.Blocks.FruitBlocks.BuritiFruitBlock;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;

public class BuritiFruitBunch extends Fruit {
    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(BuritiFruitBunch::new);

    public BuritiFruitBunch(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(AbstractBlock.Properties properties) {
        return new BuritiFruitBlock(properties, this);
    }

}


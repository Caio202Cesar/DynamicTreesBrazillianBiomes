package com.caiocesarmods.dtbrbiomesmod.GenFeatures.FruitBunchGenFeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class AcaiBunchGenerator extends GenFeature {

    public static final ConfigurationProperty<Fruit> FRUIT =
            ConfigurationProperty.property("fruit", Fruit.class);

    public AcaiBunchGenerator(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(FRUIT, QUANTITY, FRUITING_RADIUS, PLACE_CHANCE);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(FRUIT, Fruit.NULL)
                .with(QUANTITY, 8)
                .with(FRUITING_RADIUS, 6)
                .with(PLACE_CHANCE, 0.25f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        if (context.endPoints().isEmpty()) {
            return false;
        }
        int qty = configuration.get(QUANTITY);
        qty *= context.fruitProductionFactor();
        for (int i = 0; i < qty; i++) {
            this.placeDuringWorldGen(configuration, configuration.get(FRUIT), context.world(), context.pos(),
                    context.endPoints().get(0), context.seasonValue());
        }
        return true;
    }

    protected void placeDuringWorldGen(GenFeatureConfiguration configuration, Fruit fruit, IWorld world,
                                       BlockPos rootPos, BlockPos leavesPos, Float seasonValue) {
        Direction placeDirection = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        if (shouldPlaceDuringWorldGen(configuration, world, rootPos, leavesPos, placeDirection)) {
            fruit.placeDuringWorldGen(world, leavesPos.offset(placeDirection.getOpposite()), seasonValue);
        }
    }

    protected boolean shouldPlaceDuringWorldGen(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos,
                                                BlockPos leavesPos, Direction placeDirection) {
        return leavesPos.getY() != rootPos.getY() && world.isAirBlock(leavesPos.offset(placeDirection.getOpposite()))
                && world.getRandom().nextFloat() <= configuration.get(PLACE_CHANCE);
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        IWorld world = context.world();
        BlockPos rootPos = context.pos();
        if (TreeHelper.getRadius(world, rootPos.up()) >= configuration.get(FRUITING_RADIUS) && context.natural()) {
            final Fruit fruit = configuration.get(FRUIT);
            final float fruitingFactor = fruit.seasonalFruitProductionFactor(context.worldContext(), rootPos);
            if (fruitingFactor > world.getRandom().nextFloat()) {
                place(configuration, fruit, world, rootPos, getLeavesHeight(rootPos, world).down(),
                        SeasonHelper.getSeasonValue(context.worldContext(), rootPos));
            }
            return true;
        }
        return false;
    }

    private BlockPos getLeavesHeight(BlockPos rootPos, IWorld world) {
        for (int y = 1; y < 20; y++) {
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof LeavesBlock)) {
                return testPos;
            }
        }
        return rootPos;
    }

    protected void place(GenFeatureConfiguration configuration, Fruit fruit, IWorld world, BlockPos rootPos,
                         BlockPos leavesPos, Float seasonValue) {
        Direction placeDirection = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        if (shouldPlace(configuration, world, rootPos, leavesPos, placeDirection)) {
            fruit.place(world, leavesPos.offset(placeDirection.getOpposite()), seasonValue);
        }
    }

    protected boolean shouldPlace(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos,
                                  BlockPos leavesPos, Direction placeDirection) {
        return leavesPos.getY() != rootPos.getY() && world.isAirBlock(leavesPos.offset(placeDirection.getOpposite()))
                && world.getRandom().nextFloat() <= configuration.get(PLACE_CHANCE);
    }

}

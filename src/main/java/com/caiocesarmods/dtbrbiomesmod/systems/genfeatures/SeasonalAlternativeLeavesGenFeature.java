package com.caiocesarmods.dtbrbiomesmod.systems.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemappers.FindEndsNode;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.BlockBounds;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SeasonalAlternativeLeavesGenFeature extends GenFeature {

    public static final ConfigurationProperty<LeavesProperties> SEASONAL_ALT_LEAVES = ConfigurationProperty.property("seasonal_alternative_leaves", LeavesProperties.class);
    public static final ConfigurationProperty<Block> SEASONAL_ALT_LEAVES_BLOCK = ConfigurationProperty.block("seasonal_alternative_leaves_block");

    public static final TypedRegistry.EntryType<GenFeature> TYPE = TypedRegistry.newType(SeasonalAlternativeLeavesGenFeature::new);

    public SeasonalAlternativeLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(SEASONAL_ALT_LEAVES, SEASONAL_ALT_LEAVES_BLOCK, PLACE_CHANCE, QUANTITY);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(SEASONAL_ALT_LEAVES, LeavesProperties.NULL).with(SEASONAL_ALT_LEAVES_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f).with(QUANTITY, 5);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        configuration.get(SEASONAL_ALT_LEAVES).ifValid(properties -> {
            properties.setFamily(species.getFamily());
            species.addValidLeafBlocks(properties);
        });
        return true;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final BlockBounds bounds = context.species().getFamily().expandLeavesBlockBounds(new BlockBounds(context.endPoints()));
        return this.setAltLeaves(configuration, context.world(), bounds, context.bounds(), context.species());
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() == 0) {
            return false;
        }

        final IWorld world = context.world();
        final Species species = context.species();

        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(world, context.pos(), new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();
        if (endPoints.isEmpty()) {
            return false;
        }

        final BlockPos chosenEndPoint = endPoints.get(world.getRandom().nextInt(endPoints.size()));
        final BlockBounds bounds = species.getFamily().expandLeavesBlockBounds(new BlockBounds(chosenEndPoint));

        return setAltLeaves(configuration, world, bounds, SafeChunkBounds.ANY, species);
    }

    private Block getAltLeavesBlock(GenFeatureConfiguration conifuration) {
        LeavesProperties properties = conifuration.get(SEASONAL_ALT_LEAVES);
        if (!properties.isValid() || !properties.getDynamicLeavesBlock().isPresent()) {
            return conifuration.get(SEASONAL_ALT_LEAVES_BLOCK);
        }
        return properties.getDynamicLeavesBlock().get();
    }

    //Change with season
    private BlockState getSwapBlockState(GenFeatureConfiguration configuration, IWorld world, Species species, BlockState state, boolean worldgen) {
        DynamicLeavesBlock originalLeaves = species.getLeavesBlock().orElse(null);
        Block alt = getAltLeavesBlock(configuration);

        DynamicLeavesBlock altLeaves = alt instanceof DynamicLeavesBlock ? (DynamicLeavesBlock) alt : null;
        if (originalLeaves != null && altLeaves != null) {
            if (worldgen || world.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                if (state.getBlock() == originalLeaves) {
                    return altLeaves.properties.getDynamicLeavesState(state.get(LeavesBlock.DISTANCE));
                }
            } else {
                if (state.getBlock() == altLeaves) {
                    return originalLeaves.properties.getDynamicLeavesState(state.get(LeavesBlock.DISTANCE));
                }
            }
        }
        return state;
    }

    private boolean setAltLeaves(GenFeatureConfiguration configuration, IWorld world, BlockBounds leafPositions, SafeChunkBounds safeBounds, Species species) {
        boolean worldGen = safeBounds != SafeChunkBounds.ANY;

        if (worldGen) {
            AtomicBoolean isSet = new AtomicBoolean(false);
            leafPositions.iterator().forEachRemaining((pos) -> {
                if (safeBounds.inBounds(pos, true) && world.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                    if (world.setBlockState(pos, getSwapBlockState(configuration, world, species, world.getBlockState(pos), true), 2)); {
                        isSet.set(true);
                    }
                }
            });
            return isSet.get();
        } else {
            boolean isSet = false;
            List<BlockPos> posList = new LinkedList<>();
            for (BlockPos leafPosition : leafPositions) {
                posList.add(new BlockPos(leafPosition));
            }
            if (posList.isEmpty()) {
                return false;
            }
            for (int i = 0; i < configuration.get(QUANTITY); i++) {
                BlockPos pos = posList.get(world.getRandom().nextInt(posList.size()));
                if (world.setBlockState(pos, getSwapBlockState(configuration, world, species, world.getBlockState(pos), false), 2)) {
                    isSet = true;
                }
            }
            return isSet;
        }
    }
}



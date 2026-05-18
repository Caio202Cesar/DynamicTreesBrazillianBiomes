package com.caiocesarmods.dtbrbiomesmod.systems.genfeatures;

import com.brbiomesmod.Seasons.Season;
import com.caiocesarmods.dtbrbiomesmod.systems.Climate;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mojang.blaze3d.vertex.IVertexBuilder.LOGGER;

public class SeasonalAlternativeLeavesGenFeature extends GenFeature {

    public static final ConfigurationProperty<LeavesProperties> ALT_LEAVES =
            ConfigurationProperty.property("alternative_leaves", LeavesProperties.class);
    public static final ConfigurationProperty<Block> ALT_LEAVES_BLOCK =
            ConfigurationProperty.block("alternative_leaves_block");
    public static final ConfigurationProperty<Season> SEASON =
            ConfigurationProperty.property("season", Season.class);
    public static final ConfigurationProperty<Climate> CLIMATE =
            ConfigurationProperty.property("climate", Climate.class);

    public SeasonalAlternativeLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);


    }

    @Override
    protected void registerProperties() {

        this.register(ALT_LEAVES, ALT_LEAVES_BLOCK, SEASON, CLIMATE, PLACE_CHANCE, QUANTITY);

    }

    public GenFeatureConfiguration createDefaultConfiguration() {

        GenFeatureConfiguration configuration = super.createDefaultConfiguration()
                .with(ALT_LEAVES, LeavesProperties.NULL)
                .with(ALT_LEAVES_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f)
                .with(SEASON, Season.SPRING)
                .with(CLIMATE, Climate.TEMPERATE)
                .with(QUANTITY, 5);

        return configuration;

    }

    private boolean matchesEnvironment(GenFeatureConfiguration configuration,
                                       IWorld world,
                                       BlockPos pos) {

        if (!(world instanceof ServerWorld)) {
            return false;
        }

        ServerWorld serverWorld = (ServerWorld) world;

        String currentSeason =
                Season.getSeason(serverWorld.getDayTime());

        String currentClimate =
                Climate.getClimate(serverWorld, pos);

        return currentSeason.equals(configuration.get(SEASON).name())
                && currentClimate.equals(configuration.get(CLIMATE).name());
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {

        configuration.get(ALT_LEAVES).ifValid(properties -> {

            properties.setFamily(species.getFamily());

            species.addValidLeafBlocks(properties);

        });

        return true;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration,
                                   PostGenerationContext context) {

        if (!matchesEnvironment(configuration,
                context.world(),
                context.pos())) {

            return false;
        }

        final BlockBounds bounds =
                context.species()
                        .getFamily()
                        .expandLeavesBlockBounds(
                                new BlockBounds(context.endPoints())
                        );

        return this.setAltLeaves(configuration,
                context.world(),
                bounds,
                context.bounds(),
                context.species());
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration,
                               PostGrowContext context) {

        if (context.fertility() == 0) {
            return false;
        }

        final IWorld world = context.world();

        if (!(world instanceof ServerWorld)) {
            return false;
        }

        final ServerWorld serverWorld = (ServerWorld) world;

        String currentSeason =
                Season.getSeason(serverWorld.getDayTime());

        String currentClimate =
                Climate.getClimate(serverWorld, context.pos());

        if (!currentSeason.equals(configuration.get(SEASON).name())
                || !currentClimate.equals(configuration.get(CLIMATE).name())) {

            return false;
        }

        final Species species = context.species();

        final FindEndsNode endFinder = new FindEndsNode();

        TreeHelper.startAnalysisFromRoot(
                world,
                context.pos(),
                new MapSignal(endFinder)
        );

        final List<BlockPos> endPoints = endFinder.getEnds();

        if (endPoints.isEmpty()) {
            return false;
        }

        final BlockPos chosenEndPoint =
                endPoints.get(
                        world.getRandom().nextInt(endPoints.size())
                );

        final BlockBounds bounds =
                species.getFamily()
                        .expandLeavesBlockBounds(
                                new BlockBounds(chosenEndPoint)
                        );

        return setAltLeaves(configuration,
                world,
                bounds,
                SafeChunkBounds.ANY,
                species);
    }

    private Block getAltLeavesBlock(GenFeatureConfiguration conifuration) {
        LeavesProperties properties = conifuration.get(ALT_LEAVES);

        if (!properties.isValid() || !properties.getDynamicLeavesBlock().isPresent()) {
            LOGGER.debug("Using ALT_LEAVES_BLOCK fallback.");
            LOGGER.debug("ALT_LEAVES_BLOCK_GENERATED={}", conifuration.get(ALT_LEAVES_BLOCK));

            return conifuration.get(ALT_LEAVES_BLOCK);
        }

        LOGGER.debug("Using dynamic leaves block.");
        LOGGER.debug("Dynamic leaves block={}", properties.getDynamicLeavesBlock().get());

        return properties.getDynamicLeavesBlock().get();
    }

    private BlockState getSwapBlockState(GenFeatureConfiguration configuration, IWorld world, Species species, BlockState state, boolean worldgen) {

        DynamicLeavesBlock originalLeaves = species.getLeavesBlock().orElse(null);

        Block alt = getAltLeavesBlock(configuration);

        DynamicLeavesBlock altLeaves = alt instanceof DynamicLeavesBlock ? (DynamicLeavesBlock) alt : null;

        if (originalLeaves != null && altLeaves != null) {

            if (worldgen || world.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {

                if (state.getBlock() == originalLeaves) {

                    if (state.hasProperty(LeavesBlock.DISTANCE)) {

                        int distance = state.get(LeavesBlock.DISTANCE);

                        // Only replace outer canopy leaves
                        if (distance >= 4) {

                            return altLeaves.properties
                                    .getDynamicLeavesState(distance);
                        }

                    }
                }

            } else {

                if (state.getBlock() == altLeaves) {

                    return originalLeaves.properties.getDynamicLeavesState(state.get(LeavesBlock.DISTANCE));
                }
            }
        }

        LOGGER.debug("Returning original state.");
        return state;
    }

    private boolean setAltLeaves(GenFeatureConfiguration configuration,
                                 IWorld world,
                                 BlockBounds leafPositions,
                                 SafeChunkBounds safeBounds,
                                 Species species) {

        boolean worldGen = safeBounds != SafeChunkBounds.ANY;

        if (worldGen) {

            AtomicBoolean isSet = new AtomicBoolean(false);

            leafPositions.iterator().forEachRemaining((pos) -> {

                boolean inBounds = safeBounds.inBounds(pos, true);

                if (inBounds) {

                    BlockState currentState =
                            world.getBlockState(pos);

                    BlockState replacement =
                            getSwapBlockState(
                                    configuration,
                                    world,
                                    species,
                                    currentState,
                                    true
                            );

                    if (!replacement.equals(currentState)
                            && world.setBlockState(pos, replacement, 2)) {

                        isSet.set(true);
                    }
                }
            });

            return isSet.get();

        } else {

            boolean isSet = false;

            List<BlockPos> posList = new ArrayList<>();

            for (BlockPos leafPosition : leafPositions) {

                posList.add(new BlockPos(leafPosition));
            }

            if (posList.isEmpty()) {

                return false;
            }

            for (int i = 0; i < configuration.get(QUANTITY); i++) {

                BlockPos pos =
                        posList.get(
                                world.getRandom().nextInt(posList.size())
                        );

                BlockState currentState =
                        world.getBlockState(pos);

                BlockState replacement =
                        getSwapBlockState(
                                configuration,
                                world,
                                species,
                                currentState,
                                false
                        );

                if (!replacement.equals(currentState)
                        && world.setBlockState(pos, replacement, 2)) {

                    isSet = true;
                }
            }

            return isSet;
        }
    }
}


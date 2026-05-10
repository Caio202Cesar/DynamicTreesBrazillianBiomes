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

import java.util.LinkedList;
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

        LOGGER.debug("SeasonalAlternativeLeavesGenFeature constructor called.");
        LOGGER.debug("Registry name: {}", registryName);

    }

    @Override
    protected void registerProperties() {
        LOGGER.debug("registerProperties() called.");

        this.register(ALT_LEAVES, ALT_LEAVES_BLOCK, SEASON, CLIMATE, PLACE_CHANCE, QUANTITY);

        LOGGER.debug("Properties registered:");
        LOGGER.debug("ALT_LEAVES={}", ALT_LEAVES);
        LOGGER.debug("ALT_LEAVES_BLOCK={}", ALT_LEAVES_BLOCK);
        LOGGER.debug("SEASON={}", SEASON);
        LOGGER.debug("CLIMATE={}", CLIMATE);
        LOGGER.debug("PLACE_CHANCE={}", PLACE_CHANCE);
        LOGGER.debug("QUANTITY={}", QUANTITY);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {

        LOGGER.debug("createDefaultConfiguration() called.");

        GenFeatureConfiguration configuration = super.createDefaultConfiguration()
                .with(ALT_LEAVES, LeavesProperties.NULL)
                .with(ALT_LEAVES_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f)
                .with(SEASON, Season.SPRING)
                .with(CLIMATE, Climate.TEMPERATE)
                .with(QUANTITY, 5);

        LOGGER.debug("Default configuration created:");
        LOGGER.debug("DEFAULT_ALT_LEAVES={}", configuration.get(ALT_LEAVES));
        LOGGER.debug("DEFAULT_ALT_LEAVES_BLOCK={}", configuration.get(ALT_LEAVES_BLOCK));
        LOGGER.debug("DEFAULT_PLACE_CHANCE={}", configuration.get(PLACE_CHANCE));
        LOGGER.debug("DEFAULT_SEASON={}", configuration.get(SEASON));
        LOGGER.debug("DEFAULT_CLIMATE={}", configuration.get(CLIMATE));
        LOGGER.debug("DEFAULT_QUANTITY={}", configuration.get(QUANTITY));

        return configuration;

    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        LOGGER.debug("shouldApply() called.");
        LOGGER.debug("Species={}", species);
        LOGGER.debug("Configuration ALT_LEAVES={}",

        configuration.get(ALT_LEAVES).ifValid(properties -> {
            LOGGER.debug("ALT_LEAVES properties valid.");
            LOGGER.debug("Properties={}", properties);

            properties.setFamily(species.getFamily());

            LOGGER.debug("Family set on properties: {}", species.getFamily());

            species.addValidLeafBlocks(properties);

            LOGGER.debug("Added valid leaf blocks to species.");

        }));

        LOGGER.debug("shouldApply() returning true.");
        return true;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {

        LOGGER.debug("postGenerate() called.");

        final BlockBounds bounds = context.species().getFamily().expandLeavesBlockBounds(new BlockBounds(context.endPoints()));

        LOGGER.debug("Generated bounds={}", bounds);
        LOGGER.debug("Proper species={}", context.species());
        LOGGER.debug("Endpoints={}", context.endPoints());

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
        LOGGER.debug("getAltLeavesBlock() called.");

        LeavesProperties properties = conifuration.get(ALT_LEAVES);

        LOGGER.debug("LeavesProperties={}", properties);

        if (!properties.isValid() || !properties.getDynamicLeavesBlock().isPresent()) {
            LOGGER.debug("Using ALT_LEAVES_BLOCK fallback.");
            LOGGER.debug("ALT_LEAVES_BLOCK={}", conifuration.get(ALT_LEAVES_BLOCK));

            return conifuration.get(ALT_LEAVES_BLOCK);
        }

        LOGGER.debug("Using dynamic leaves block.");
        LOGGER.debug("Dynamic leaves block={}", properties.getDynamicLeavesBlock().get());

        return properties.getDynamicLeavesBlock().get();
    }

    private BlockState getSwapBlockState(GenFeatureConfiguration configuration, IWorld world, Species species, BlockState state, boolean worldgen) {

        LOGGER.debug("getSwapBlockState() called.");

        LOGGER.debug("Input state={}", state);
        LOGGER.debug("Worldgen={}", worldgen);

        DynamicLeavesBlock originalLeaves = species.getLeavesBlock().orElse(null);

        LOGGER.debug("Original leaves={}", originalLeaves);

        Block alt = getAltLeavesBlock(configuration);

        LOGGER.debug("Alt block={}", alt);

        DynamicLeavesBlock altLeaves = alt instanceof DynamicLeavesBlock ? (DynamicLeavesBlock) alt : null;

        LOGGER.debug("Alt leaves dynamic block={}", altLeaves);

        if (originalLeaves != null && altLeaves != null) {

            if (worldgen || world.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {

                LOGGER.debug("Swap direction: original -> alt");

                if (state.getBlock() == originalLeaves) {

                    LOGGER.debug("State block matches original leaves.");

                    return altLeaves.properties.getDynamicLeavesState(state.get(LeavesBlock.DISTANCE));
                }

            } else {

                LOGGER.debug("Swap direction: alt -> original");

                if (state.getBlock() == altLeaves) {

                    LOGGER.debug("State block matches alt leaves.");
                    return originalLeaves.properties.getDynamicLeavesState(state.get(LeavesBlock.DISTANCE));
                }
            }
        }

        LOGGER.debug("Returning original state.");
        return state;
    }

    private boolean setAltLeaves(GenFeatureConfiguration configuration, IWorld world, BlockBounds leafPositions, SafeChunkBounds safeBounds, Species species) {

        LOGGER.debug("setAltLeaves() called.");
        LOGGER.debug("leafPositions={}", leafPositions);
        LOGGER.debug("safeBounds={}", safeBounds);
        LOGGER.debug("species={}", species);

        boolean worldGen = safeBounds != SafeChunkBounds.ANY;

        LOGGER.debug("worldGen={}", worldGen);

        if (worldGen) {

            LOGGER.debug("Entering worldgen branch.");

            AtomicBoolean isSet = new AtomicBoolean(false);

            leafPositions.iterator().forEachRemaining((pos) -> {

                LOGGER.debug("Checking position={}", pos);

                boolean inBounds = safeBounds.inBounds(pos, true);

                LOGGER.debug("inBounds={}", inBounds);

                float random = world.getRandom().nextFloat();

                LOGGER.debug("randomValue={}", random);
                LOGGER.debug("placeChance={}", configuration.get(PLACE_CHANCE));

                if (inBounds && random < configuration.get(PLACE_CHANCE)) {

                    LOGGER.debug("Attempting swap at {}", pos);

                    BlockState currentState = world.getBlockState(pos);

                    LOGGER.debug("currentState={}", currentState);

                    BlockState replacement =
                            getSwapBlockState(configuration, world, species, currentState, true);

                    LOGGER.debug("replacementState={}", replacement);

                    if (world.setBlockState(pos, replacement, 2)) {

                        LOGGER.debug("Successfully replaced block at {}", pos);

                        isSet.set(true);

                    } else {

                        LOGGER.debug("Failed to replace block at {}", pos);
                    }
                }
            });

            LOGGER.debug("Returning worldgen result={}", isSet.get());

            return isSet.get();

        } else {

            LOGGER.debug("Entering grow branch.");

            boolean isSet = false;

            List<BlockPos> posList = new LinkedList<>();

            for (BlockPos leafPosition : leafPositions) {

                LOGGER.debug("Adding leaf position={}", leafPosition);

                posList.add(new BlockPos(leafPosition));
            }

            LOGGER.debug("Collected positions={}", posList.size());

            if (posList.isEmpty()) {

                LOGGER.debug("Position list empty. Returning false.");

                return false;
            }

            for (int i = 0; i < configuration.get(QUANTITY); i++) {

                LOGGER.debug("Iteration={}", i);

                BlockPos pos =
                        posList.get(world.getRandom().nextInt(posList.size()));

                LOGGER.debug("Chosen position={}", pos);

                BlockState currentState = world.getBlockState(pos);

                LOGGER.debug("currentState={}", currentState);

                BlockState replacement =
                        getSwapBlockState(configuration, world, species, currentState, false);

                LOGGER.debug("replacementState={}", replacement);

                if (world.setBlockState(pos, replacement, 2)) {

                    LOGGER.debug("Successfully replaced block at {}", pos);

                    isSet = true;

                } else {

                    LOGGER.debug("Failed to replace block at {}", pos);
                }
            }

            LOGGER.debug("Returning grow result={}", isSet);

            return isSet;
        }
    }
}


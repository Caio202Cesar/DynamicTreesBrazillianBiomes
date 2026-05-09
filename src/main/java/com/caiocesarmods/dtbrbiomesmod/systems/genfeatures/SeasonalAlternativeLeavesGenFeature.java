package com.caiocesarmods.dtbrbiomesmod.systems.genfeatures;

import com.brbiomesmod.Climate.ClimateSetup;
import com.brbiomesmod.Seasons.Season;
import com.caiocesarmods.dtbrbiomesmod.systems.Climate;
import com.caiocesarmods.dtbrbiomesmod.systems.SeasonalLeafConfig;
import com.caiocesarmods.dtbrbiomesmod.systems.SeasonalLeafRegistry;
import com.caiocesarmods.dtbrbiomesmod.systems.SeasonalLeafRule;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.compat.seasons.SereneSeasonsSeasonProvider;
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
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        this.register(ALT_LEAVES, ALT_LEAVES_BLOCK, PLACE_CHANCE, QUANTITY);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(ALT_LEAVES, LeavesProperties.NULL).with(ALT_LEAVES_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f).with(QUANTITY, 5);
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
        LeavesProperties properties = conifuration.get(ALT_LEAVES);
        if (!properties.isValid() || !properties.getDynamicLeavesBlock().isPresent()) {
            return conifuration.get(ALT_LEAVES_BLOCK);
        }
        return properties.getDynamicLeavesBlock().get();
    }

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
                    if (world.setBlockState(pos, getSwapBlockState(configuration, world, species, world.getBlockState(pos), true), 2)) {
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
    /*private static final Logger LOGGER = LogManager.getLogger();

    public SeasonalAlternativeLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {

        this.register(

                PLACE_CHANCE,
                QUANTITY
        );
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {

        return super.createDefaultConfiguration()
                .with(PLACE_CHANCE, 0.5F)
                .with(QUANTITY, 5);
    }

    @Override
    protected boolean postGenerate(
            GenFeatureConfiguration configuration,
            PostGenerationContext context
    ) {

        final BlockBounds bounds =
                context.species()
                        .getFamily()
                        .expandLeavesBlockBounds(
                                new BlockBounds(context.endPoints())
                        );

        return this.setAltLeaves(
                configuration,
                context.world(),
                bounds,
                context.bounds(),
                context.species()
        );
    }


    @Override
    protected boolean postGrow(
            GenFeatureConfiguration configuration,
            PostGrowContext context
    ) {

        if (context.fertility() == 0) {
            return false;
        }

        final IWorld world = context.world();

        final Species species = context.species();

        final FindEndsNode endFinder =
                new FindEndsNode();

        TreeHelper.startAnalysisFromRoot(
                world,
                context.pos(),
                new MapSignal(endFinder)
        );

        final List<BlockPos> endPoints =
                endFinder.getEnds();

        if (endPoints.isEmpty()) {
            return false;
        }

        final BlockPos chosenEndPoint =
                endPoints.get(
                        world.getRandom()
                                .nextInt(endPoints.size())
                );

        final BlockBounds bounds =
                species.getFamily()
                        .expandLeavesBlockBounds(
                                new BlockBounds(chosenEndPoint)
                        );

        return this.setAltLeaves(
                configuration,
                world,
                bounds,
                SafeChunkBounds.ANY,
                species
        );
    }


    private boolean setAltLeaves(
            GenFeatureConfiguration configuration,
            IWorld world,
            BlockBounds leafPositions,
            SafeChunkBounds safeBounds,
            Species species
    ) {

        boolean worldGen =
                safeBounds != SafeChunkBounds.ANY;

        if (worldGen) {

            AtomicBoolean isSet =
                    new AtomicBoolean(false);

            leafPositions.iterator().forEachRemaining(pos -> {

                if (!safeBounds.inBounds(pos, true)) {
                    return;
                }

                processLeafPosition(
                        configuration,
                        world,
                        species,
                        pos,
                        isSet
                );
            });

            return isSet.get();

        } else {

            boolean isSet = false;

            List<BlockPos> posList =
                    new LinkedList<>();

            for (BlockPos leafPosition : leafPositions) {
                posList.add(new BlockPos(leafPosition));
            }

            if (posList.isEmpty()) {
                return false;
            }

            for (int i = 0;
                 i < configuration.get(QUANTITY);
                 i++) {

                BlockPos pos =
                        posList.get(
                                world.getRandom()
                                        .nextInt(posList.size())
                        );

                boolean changed =
                        processLeafPosition(
                                configuration,
                                world,
                                species,
                                pos,
                                null
                        );

                if (changed) {
                    isSet = true;
                }
            }

            return isSet;
        }
    }


    private boolean processLeafPosition(
            GenFeatureConfiguration configuration,
            IWorld world,
            Species species,
            BlockPos pos,
            AtomicBoolean atomic
    ) {

        LOGGER.info("Processing leaf at {}", pos);

        float seasonValue =
                SeasonHelper.getSeasonValue(world, pos);

        float temperature =
                world.getBiome(pos)
                        .getTemperature(pos);

        float seasonalChance =
                getSeasonalSwapChance(
                        seasonValue,
                        temperature
                );

        float finalChance =
                configuration.get(PLACE_CHANCE)
                        * seasonalChance;

        if (world.getRandom().nextFloat()
                >= finalChance) {

            return false;
        }

        BlockState currentState =
                world.getBlockState(pos);

        BlockState replacement =
                getSeasonalLeaves(
                        world,
                        seasonValue,
                        temperature,
                        currentState,
                        species
                );

        if (replacement == currentState) {
            return false;
        }

        boolean success =
                world.setBlockState(
                        pos,
                        replacement,
                        2
                );

        LOGGER.info( "Current={} Replacement={}", currentState, replacement );


        if (success && atomic != null) {
            atomic.set(true);
        }

        return success;

    }

    public float getSeasonalSwapChance(
            float seasonValue,
            float temperature
    ) {
        //Tropical dry season

        if (temperature >= 0.9F
                && seasonValue > 1.3F
                && seasonValue < 2.7F) {

            return 0.7F;
        }

        //Autumn

        if (seasonValue >= 2.0F
                && seasonValue < 3.0F) {

            if (temperature < 0.3F) {
                return 1.0F;
            }

            if (temperature < 0.8F) {
                return 0.7F;
            }

            return 0.4F;
        }

        //Winter
        if (seasonValue >= 3.0F) {

            if (temperature < 0.8F) {
                return 1.0F;
            }

            return 0.8F;
        }

        //Spring

        if (seasonValue < 0.7F) {
            return 0.8F;
        }

        return 0.0F;

    }

    public BlockState getSeasonalLeaves(
            IWorld world,
            float seasonValue,
            float temperature,
            BlockState currentState,
            Species species
    ) {

        LOGGER.info("Species lookup: {}", species.getRegistryName());

        SeasonalLeafConfig config =
                SeasonalLeafRegistry.get(
                        species.getRegistryName()
                );

        if (config == null) {
            return currentState;
        }

        LOGGER.info("Config found: {}", true);

        SeasonalLeafRule bestRule = null;

        float bestStrength = 0F;

        for (SeasonalLeafRule rule : config.rules) {

            LOGGER.info(
                    "Season={}, Temp={}, Rule={}..{}",
                    seasonValue,
                    temperature,
                    rule.temp_min,
                    rule.temp_max
            );

            if (temperature < rule.temp_min
                    || temperature > rule.temp_max) {

                continue;
            }

            float distance =
                    Math.abs(
                            seasonValue
                                    - rule.peak_season
                    );

            distance =
                    Math.min(distance, 4F - distance);

            if (distance > rule.fade) {
                continue;
            }

            float strength =
                    1F - (distance / rule.fade);

            strength *= rule.chance;

            if (strength > bestStrength) {

                bestStrength = strength;

                bestRule = rule;
            }
        }

        if (bestRule == null) {
            return currentState;
        }

        if (worldRandomFloat(world) > bestStrength) {
            return currentState;
        }

        LeavesProperties properties =
                LeavesProperties.REGISTRY.get(
                        bestRule.leaves_properties
                );

        LOGGER.info( "Leaves properties lookup: {}", bestRule.leaves_properties );

        if (properties == null
                || !properties.isValid()
                || !properties.getDynamicLeavesBlock().isPresent()) {

            return currentState;

        }

        LOGGER.info( "Properties found: {}", true);

        DynamicLeavesBlock dynamicLeaves =
                properties.getDynamicLeavesBlock().get();

        int leafDistance = 1;

        if (currentState.hasProperty(LeavesBlock.DISTANCE)) {
            leafDistance =
                    currentState.get(LeavesBlock.DISTANCE);
        }

        return dynamicLeaves.properties
                .getDynamicLeavesState(leafDistance);

    }

    private float worldRandomFloat(IWorld world) {
        return world.getRandom().nextFloat();
    }
    }/*
     */

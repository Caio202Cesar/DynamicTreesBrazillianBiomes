/*package com.caiocesarmods.dtbrbiomesmod.GenFeatures;

import com.caiocesarmods.dtbrbiomesmod.util.RegisterFinderUtil;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemappers.FindEndsNode;
import com.ferreusveritas.dynamictrees.systems.nodemappers.StateNode;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class FeatureGenJabuticabaFloweringBranch extends GenFeature {
    public static final ConfigurationProperty<String> BRANCH = ConfigurationProperty.property("branch", String.class);

    public FeatureGenJabuticabaFloweringBranch(ResourceLocation registryName) {
        super(registryName);
    }


    @Override
    protected void registerProperties() {
        this.register(BRANCH);
    }

    @Override
    protected GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BRANCH, "air");
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final Block newBranch = RegisterFinderUtil.getBlock(configuration.get(BRANCH));
        if (!(newBranch instanceof BranchBlock))
            return false;
        final Species species = context.species();
        final StateNode stateNode = new StateNode(BlockPos.ZERO);
        final IWorld world = context.world();
        final Random random = context.random();
        TreeHelper.startAnalysisFromRoot(world, context.pos(), new MapSignal(stateNode));
        // final List<BlockPos> leavesPoints;

        stateNode.getBranchConnectionMap().forEach((blockPos, branchConnectionData) -> {
            final BlockState oldBranchState = branchConnectionData.getBlockState();
            if (oldBranchState.equals(species.getFamily().getBranch().get())) {
                // if (((BranchBlock) (oldBranchState.getBlock())).getRadius(oldBranchState) > 5)
                if (random.nextFloat() > 0.65) {
                    world.updateBlock(blockPos, copy(oldBranchState, newBranch.defaultBlockState()), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        });

        return true;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        final Block newBranch = RegisterFinderUtil.getBlock(configuration.get(BRANCH));
        if (!(newBranch instanceof BranchBlock))
            return false;
        final Species species = context.species();
        final StateNode stateNode = new StateNode(BlockPos.ZERO);
        final IWorld world = context.world();
        final Random random = context.random();
        TreeHelper.startAnalysisFromRoot(world, context.pos(), new MapSignal(stateNode));
        // final List<BlockPos> leavesPoints;

        stateNode.getBranchConnectionMap().forEach((blockPos, branchConnectionData) -> {
            final BlockState oldBranchState = branchConnectionData.getBlockState();
            if (oldBranchState.is(species.getFamily().getBranch().get())) {
                // if (((BranchBlock) (oldBranchState.getBlock())).getRadius(oldBranchState) > 5)
                if (random.nextFloat() > 0.995) {
                    world.setBlock(blockPos, copy(oldBranchState, newBranch.defaultBlockState()), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        });
        return true;
    }

    public static <T extends Comparable<T>, V extends T> BlockState copy(BlockState oldBranchState, BlockState newBranchState0) {
        final BlockState[] newBranchState = {newBranchState0};
        oldBranchState.getValues().forEach((property, comparable) -> {
            if (newBranchState[0].hasProperty(property)) {
                newBranchState[0] = newBranchState[0].setValue((Property<T>) property, (V) oldBranchState.getValue(property));
            }
        });
        return newBranchState[0];
    }

    public static BlockState copy2(BlockState oldBranchState, BlockState newBranchState0) {
        final BlockState[] newBranchState = {newBranchState0};
        oldBranchState.getValues().forEach((property, comparable) -> {
            if (property instanceof BooleanProperty) {
                BooleanProperty booleanProperty = (BooleanProperty) property;
                newBranchState[0] = newBranchState[0].setValue(booleanProperty, oldBranchState.getValue(booleanProperty));
            } else if (property instanceof EnumProperty) {
                EnumProperty enumProperty = (EnumProperty) property;
                newBranchState[0] = newBranchState[0].setValue(enumProperty, oldBranchState.getValue(enumProperty));
            } else if (property instanceof IntegerProperty) {
                IntegerProperty integerProperty = (IntegerProperty) property;
                newBranchState[0] = newBranchState[0].setValue(integerProperty, oldBranchState.getValue(integerProperty));
            } else
                return;
        });
        return newBranchState[0];
    }
}*/

package com.caiocesarmods.dtbrbiomesmod.Trees.Family;

import com.brbiomesmod.BrazillianBiomesMod;
import com.brbiomesmod.block.TreesGroup;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NetVolumeNode;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IpeFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE =
            TypedRegistry.newType(IpeFamily::new);

    public IpeFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    protected BasicBranchBlock createBranchBlock(ResourceLocation name) {
        final BasicBranchBlock branch = new BasicBranchBlock(name, this.getProperties()) {
            @Override
            public void stripBranch(BlockState state, World world, BlockPos pos,
                                    PlayerEntity player, ItemStack heldItem) {
            }
        };

        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }

        return branch;
    }
}

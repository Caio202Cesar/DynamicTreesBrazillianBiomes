/*
package com.caiocesarmods.dtbrbiomesmod.Trees.Family;


import net.minecraft.util.ResourceLocation;
import com.caiocesarmods.dtbrbiomesmod.Blocks.JabuticabaBranch;
import com.caiocesarmods.dtbrbiomesmod.Blocks.JabuticabaFloweringBranch;
import com.caiocesarmods.dtbrbiomesmod.DTBrazillianBiomesRegistries;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public class JabuticabaFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(JabutiacabaFamily::new);

    /**
     * Constructor suitable for derivative mods
     *
     * @param name The ResourceLocation of the tree e.g. "mymod:poplar"

    public JabuticabaFamily(ResourceLocation name) {
        super(name);
    }

    private static BranchBlock floweringBranchBlock;

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        if (floweringBranchBlock == null) {
            floweringBranchBlock = this.createFloweringBranchBlock(DTBrazillianBiomesRegistries.rl("jabuticaba_flowering_branch"));
            floweringBranchBlock.setFamily(this);
            addValidBranches(RegistryHandler.addBlock(DTBrazillianBiomesRegistries.rl("jabuticaba_flowering_branch"), floweringBranchBlock));
        }
    }

    protected BranchBlock createFloweringBranchBlock(ResourceLocation name) {
        return new JabuticabaFloweringBranch(name, this.getProperties());
    }

    public static Optional<BranchBlock> getFloweringBranchBlock() {
        return Optional.of(floweringBranchBlock);
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        return new JabuticabaBranch(name, this.getProperties());
    }
}*/

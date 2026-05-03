package com.caiocesarmods.dtbrbiomesmod.Trees;

import com.caiocesarmods.dtbrbiomesmod.Trees.Family.IpeFamily;
import com.caiocesarmods.dtbrbiomesmod.Trees.Species.PinkIpeSpecies;
import com.caiocesarmods.dtbrbiomesmod.Trees.Species.PurpleIpeSpecies;
import com.caiocesarmods.dtbrbiomesmod.Trees.Species.WhiteIpeSpecies;
import com.caiocesarmods.dtbrbiomesmod.Trees.Species.YellowIpeSpecies;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.registry.Registries;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;

public class BrazillianBiomesDTTrees {

    public static Family IPE_FAMILY;

    public static Species YELLOW_IPE;
    public static Species PINK_IPE;
    public static Species PURPLE_IPE;
    public static Species WHITE_IPE;

    public static void register() {

        // 1) Registrar a FAMÍLIA
        IPE_FAMILY = new IpeFamily(new ResourceLocation("dtbrbiomesmod", "ipe"));
        Family.REGISTRY.register(IPE_FAMILY);

        // 2) Criar species usando a mesmíssima família
        YELLOW_IPE = new YellowIpeSpecies((IpeFamily) IPE_FAMILY);
        PINK_IPE   = new PinkIpeSpecies((IpeFamily) IPE_FAMILY);
        PURPLE_IPE   = new PurpleIpeSpecies((IpeFamily) IPE_FAMILY);
        WHITE_IPE   = new WhiteIpeSpecies((IpeFamily) IPE_FAMILY);

        // 3) Registrar species
        Species.REGISTRY.register(YELLOW_IPE);
        Species.REGISTRY.register(PINK_IPE);
        Species.REGISTRY.register(PURPLE_IPE);
        Species.REGISTRY.register(WHITE_IPE);

        // 4) Vincular species à família
        IPE_FAMILY.addSpecies(YELLOW_IPE);
        IPE_FAMILY.addSpecies(PINK_IPE);
        IPE_FAMILY.addSpecies(PURPLE_IPE);
        IPE_FAMILY.addSpecies(WHITE_IPE);

    }
}

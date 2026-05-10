package com.caiocesarmods.dtbrbiomesmod;

import com.caiocesarmods.dtbrbiomesmod.systems.SeasonalLeafReloadListener;
import com.caiocesarmods.dtbrbiomesmod.systems.genfeatures.ModGenFeatures;
import com.caiocesarmods.dtbrbiomesmod.systems.genfeatures.SeasonalAlternativeLeavesGenFeature;
import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.google.gson.Gson;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mojang.blaze3d.vertex.IVertexBuilder.LOGGER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazillianBiomesDTRegistries {

    public static void setup() {

    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(final TypeRegistryEvent<LeavesProperties> event) {

    }

    @SubscribeEvent
    public static void registerFamily(final TypeRegistryEvent<Family> event) {

    }

    @SubscribeEvent
    public static void registerSpecies(final TypeRegistryEvent<Species> event) {

    }

    @SubscribeEvent
    public static void onGenFeatureRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        LOGGER.debug("onGenFeatureRegistry() called.");
        LOGGER.debug("Registering seasonal_alt_leaves GenFeature.");
        event.getRegistry().register(new SeasonalAlternativeLeavesGenFeature(BrazillianBiomesDTAddon.resLoc("seasonal_alt_leaves")));
        LOGGER.debug("seasonal_alt_leaves registered successfully.");

        //ModGenFeatures.register(event.getRegistry());

    }
}

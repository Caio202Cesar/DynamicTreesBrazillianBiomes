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
    public static void onReload(AddReloadListenerEvent event) {

        event.addListener(
                new SeasonalLeafReloadListener()
        );
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        event.getRegistry().register(new SeasonalAlternativeLeavesGenFeature(BrazillianBiomesDTAddon.resLoc("seasonal_alt_leaves")));

        //ModGenFeatures.register(event.getRegistry());

    }
}

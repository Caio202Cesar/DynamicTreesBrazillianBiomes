package com.caiocesarmods.dtbrbiomesmod;

import com.caiocesarmods.dtbrbiomesmod.Blocks.LeavesProperties.PodocarpusLeavesProperties;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTBrazillianBiomesRegistries {

    public static void setup() {

    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(BrazillianBiomesDTAddon.resLoc("podocarpus_lambertii"), PodocarpusLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFamily(final TypeRegistryEvent<Family> event) {

    }

    @SubscribeEvent
    public static void registerSpecies(final TypeRegistryEvent<Species> event) {

    }

}

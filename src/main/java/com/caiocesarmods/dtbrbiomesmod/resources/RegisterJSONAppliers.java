package com.caiocesarmods.dtbrbiomesmod.resources;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.ferreusveritas.dynamictrees.api.treepacks.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.google.gson.JsonElement;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BrazillianBiomesDTAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersFamily(final ApplierRegistryEvent.Reload<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersLeavesProperties(final ApplierRegistryEvent.Reload<LeavesProperties, JsonElement> event) {
        registerLeavesPropertiesAppliers(event.getAppliers());
    }


    public static void registerFamilyAppliers(PropertyAppliers<Family, JsonElement> appliers) {

    }

    public static void registerLeavesPropertiesAppliers(PropertyAppliers<LeavesProperties, JsonElement> appliers) {

    }

    @SubscribeEvent
    public static void registerAppliersFamily(final ApplierRegistryEvent.GatherData<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersLeavesProperties(final ApplierRegistryEvent.GatherData<LeavesProperties, JsonElement> event) {
        registerLeavesPropertiesAppliers(event.getAppliers());
    }
}

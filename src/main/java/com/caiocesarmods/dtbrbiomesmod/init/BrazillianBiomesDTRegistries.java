package com.caiocesarmods.dtbrbiomesmod.init;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import com.caiocesarmods.dtbrbiomesmod.Trees.Family.CashewFamily;
import com.caiocesarmods.dtbrbiomesmod.Trees.Family.KapokFamily;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazillianBiomesDTRegistries {

    @SubscribeEvent
    public static void registerFamilyTypes(final TypeRegistryEvent<Family> event) {
        event.registerType(BrazillianBiomesDTAddon.resLoc("kapok"), KapokFamily.TYPE);
        event.registerType(BrazillianBiomesDTAddon.resLoc("cashew"), CashewFamily.TYPE);

    }
}

package com.caiocesarmods.dtbrbiomesmod.systems;

import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = BrazillianBiomesDTAddon.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ModReloadListeners {

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {

        event.addListener(
                new SeasonalLeafReloadListener()
        );
    }
}


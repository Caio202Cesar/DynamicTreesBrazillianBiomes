package com.caiocesarmods.dtbrbiomesmod.systems;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModReloadListeners {

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {

        event.addListener(
                new SeasonalLeafReloadListener()
        );
    }
}

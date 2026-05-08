package com.caiocesarmods.dtbrbiomesmod.systems;

import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModReloadListeners {

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {

        event.addListener(new IFutureReloadListener() {

            @Override
            public CompletableFuture<Void> reload(
                    IStage stage,
                    IResourceManager resourceManager,
                    IProfiler preparationsProfiler,
                    IProfiler reloadProfiler,
                    Executor backgroundExecutor,
                    Executor gameExecutor
            ) {

                return CompletableFuture.runAsync(() -> {

                    SeasonalLeafReloadListener.reload(resourceManager);

                }, backgroundExecutor);
            }
        });
    }
}

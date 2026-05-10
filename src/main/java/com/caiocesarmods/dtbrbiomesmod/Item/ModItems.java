package com.caiocesarmods.dtbrbiomesmod.Item;

import com.brbiomesmod.item.ModItemGroup;
import com.caiocesarmods.dtbrbiomesmod.BrazillianBiomesDTAddon;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BrazillianBiomesDTAddon.MOD_ID);

    public static final RegistryObject<Item> YERBA_MATE_BERRIES = ITEMS.register("yerba_mate_berries",
            () -> new Item(new Item.Properties().group(ModItemGroup.FRUITS_AND_VEGETABLES_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

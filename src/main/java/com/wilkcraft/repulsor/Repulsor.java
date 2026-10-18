package com.wilkcraft.repulsor;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Repulsor.MODID)
public class Repulsor {
  public static final String MODID = "repulsor";

  public Repulsor(FMLJavaModLoadingContext context) {
    IEventBus modBus = context.getModEventBus();

    ModItems.ITEMS.register(modBus);
    modBus.addListener(this::addToCreativeTab);

    MinecraftForge.EVENT_BUS.register(new RepulsorEvents());
  }

  private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      event.accept(ModItems.REPULSOR);
    }
  }
}
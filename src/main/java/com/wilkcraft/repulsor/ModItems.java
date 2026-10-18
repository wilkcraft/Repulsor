package com.wilkcraft.repulsor;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Repulsor.MODID);

  public static final RegistryObject<Item> REPULSOR = ITEMS.register("repulsor_item",
      () -> new RepulsorItem(new Item.Properties().stacksTo(1)));
}
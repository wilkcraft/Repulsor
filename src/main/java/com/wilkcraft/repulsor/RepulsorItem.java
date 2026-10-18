package com.wilkcraft.repulsor;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RepulsorItem extends Item {

  public RepulsorItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level,
      List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.main")
        .withStyle(ChatFormatting.AQUA));
    tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.fall")
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.op")
        .withStyle(ChatFormatting.RED));

    if (Screen.hasShiftDown()) {
      tooltip.add(Component.empty());
      tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.shift")
          .withStyle(ChatFormatting.GREEN));
      tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.exceptions")
          .withStyle(ChatFormatting.DARK_GRAY));
    } else {
      tooltip.add(Component.translatable("item.repulsor.repulsor_item.tooltip.hold_shift")
          .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }
  }
}
package com.wilkcraft.repulsor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Repulsor.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RepulsorClientEvents {

  private static final int POINTS = 36;
  private static final int LIFETIME = 5;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END)
      return;

    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;
    ClientLevel level = mc.level;
    if (player == null || level == null || mc.isPaused())
      return;
    if (!RepulsorEvents.isActive(player))
      return;

    boolean attract = player.isShiftKeyDown();
    double rotation = player.tickCount * RepulsorEvents.RING_SPIN;

    for (int i = 0; i < POINTS; i++) {
      double angle = rotation + (Math.PI * 2.0 * i) / POINTS;
      double dx = Math.cos(angle);
      double dz = Math.sin(angle);

      double x = player.getX() + dx * RepulsorEvents.RADIUS;
      double z = player.getZ() + dz * RepulsorEvents.RADIUS;
      double y = RepulsorEvents.groundY(level, x, player.getY(), z);

      Particle particle;
      if (attract) {
        particle = mc.particleEngine.createParticle(
            ParticleTypes.DRAGON_BREATH, x, y, z, -dx * 0.15, 0.0, -dz * 0.15);
      } else {
        particle = mc.particleEngine.createParticle(
            ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0, 0.06, 0.0);
      }

      if (particle != null) {
        particle.setLifetime(LIFETIME);
      }
    }
  }
}
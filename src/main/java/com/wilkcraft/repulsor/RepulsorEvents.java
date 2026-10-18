package com.wilkcraft.repulsor;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RepulsorEvents {

  public static final double RADIUS = 4.0;

  private static final double PULL_STOP_DISTANCE = 1.5;

  private static final int OP_LEVEL = 2;

  public static final double RING_SPIN = 0.04;
  private static final int RING_POINTS = 28;
  private static final int RING_INTERVAL = 3;
  private static final double VIEW_DISTANCE_SQR = 64.0 * 64.0;

  public static boolean isActive(Player player) {
    return player.hasPermissions(OP_LEVEL)
        && player.getOffhandItem().is(ModItems.REPULSOR.get());
  }

  @SubscribeEvent
  public void onFall(LivingFallEvent event) {
    if (event.getEntity() instanceof Player player
        && !player.level().isClientSide
        && isActive(player)) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END)
      return;

    Player player = event.player;
    if (!(player.level() instanceof ServerLevel level))
      return;
    if (!isActive(player))
      return;

    boolean attract = player.isShiftKeyDown();

    spawnRingForOthers(level, player, attract);

    AABB area = player.getBoundingBox().inflate(RADIUS + 1.0);

    for (Entity e : level.getEntities(player, area, entity -> shouldAffect(player, entity))) {
      Vec3 diff = e.position().subtract(player.position());
      double dist = diff.length();
      if (dist >= RADIUS)
        continue;

      Vec3 motion = e.getDeltaMovement();

      if (e instanceof Projectile) {
        Vec3 dir = dist < 0.001 ? new Vec3(1, 0, 0) : diff.scale(1.0 / dist);
        double speed = Math.max(0.6, motion.length());
        e.setDeltaMovement(dir.scale(speed));
      } else if (attract) {
        boolean living = e instanceof LivingEntity;
        double stopDistance = living ? PULL_STOP_DISTANCE : 2.2;

        if (dist < stopDistance) {
          if (!living) {
            e.setDeltaMovement(motion.x * 0.5, Math.min(motion.y, 0.0), motion.z * 0.5);
            e.hurtMarked = true;
          }
          continue;
        }

        Vec3 toPlayer = player.position().subtract(e.position());
        Vec3 flat = new Vec3(toPlayer.x, 0, toPlayer.z);
        double flatDist = flat.length();
        Vec3 flatDir = flatDist < 1.0E-3 ? Vec3.ZERO : flat.scale(1.0 / flatDist);
        double pull = Math.min(0.35, 0.15 + flatDist * 0.05);

        double vy = motion.y;

        if (living) {
          if (toPlayer.y > 0.6) {
            vy = Math.max(vy, Math.min(0.4, 0.15 + toPlayer.y * 0.05));
          }
          if (e.horizontalCollision && e.onGround()) {
            vy = Math.max(vy, 0.42);
          }
        } else {
          if (toPlayer.y > 0.02) {
            vy = Math.min(0.3, 0.05 + toPlayer.y * 0.4);
          } else {
            vy = Math.min(vy, 0.0);
          }
        }

        e.setDeltaMovement(flatDir.x * pull, vy, flatDir.z * pull);
      } else {
        Vec3 flat = new Vec3(diff.x, 0, diff.z);
        Vec3 flatDir = flat.lengthSqr() < 1.0E-6 ? new Vec3(1, 0, 0) : flat.normalize();
        double push = 0.35 + (RADIUS - dist) * 0.25;
        e.setDeltaMovement(flatDir.x * push, motion.y, flatDir.z * push);
      }
      e.hurtMarked = true;
    }
  }

  private static void spawnRingForOthers(ServerLevel level, Player owner, boolean attract) {
    if (owner.tickCount % RING_INTERVAL != 0)
      return;

    List<ServerPlayer> viewers = level.players().stream()
        .filter(v -> v != owner && v.distanceToSqr(owner) < VIEW_DISTANCE_SQR)
        .toList();
    if (viewers.isEmpty())
      return;

    ParticleOptions type = attract ? ParticleTypes.DRAGON_BREATH : ParticleTypes.SOUL_FIRE_FLAME;
    double rotation = owner.tickCount * RING_SPIN;

    for (int i = 0; i < RING_POINTS; i++) {
      double angle = rotation + (Math.PI * 2.0 * i) / RING_POINTS;
      double dx = Math.cos(angle);
      double dz = Math.sin(angle);

      double x = owner.getX() + dx * RADIUS;
      double z = owner.getZ() + dz * RADIUS;
      double y = groundY(level, x, owner.getY(), z);

      for (ServerPlayer viewer : viewers) {
        if (attract) {
          level.sendParticles(viewer, type, false, x, y, z, 0, -dx, 0.0, -dz, 0.15);
        } else {
          level.sendParticles(viewer, type, false, x, y, z, 0, 0.0, 1.0, 0.0, 0.06);
        }
      }
    }
  }

  public static double groundY(Level level, double x, double playerY, double z) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, playerY, z);

    for (int i = 0; i < 2 && !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty(); i++) {
      pos.move(Direction.UP);
    }

    for (int i = 0; i < 4; i++) {
      BlockPos below = pos.below();
      if (!level.getBlockState(below).getCollisionShape(level, below).isEmpty()) {
        return pos.getY() + 0.1;
      }
      pos.move(Direction.DOWN);
    }

    return playerY + 0.1;
  }

  private static boolean shouldAffect(Player player, Entity e) {
    if (e.isSpectator())
      return false;

    if (e instanceof ItemEntity
        || e instanceof ExperienceOrb
        || e instanceof HangingEntity
        || e instanceof ArmorStand) {
      return false;
    }

    if (isOwnAnimal(player, e))
      return false;

    if (e == player.getVehicle() || e.isPassengerOfSameVehicle(player))
      return false;

    if (e instanceof Boat boat) {
      if (boat.getPassengers().isEmpty() || boat.getControllingPassenger() == player) {
        return false;
      }
    }

    if (e instanceof Projectile projectile && projectile.getOwner() == player) {
      return false;
    }

    return true;
  }

  private static boolean isOwnAnimal(Player player, Entity e) {
    if (e instanceof TamableAnimal tamable && tamable.isOwnedBy(player))
      return true;

    if (e instanceof AbstractHorse horse
        && horse.isTamed()
        && player.getUUID().equals(horse.getOwnerUUID())) {
      return true;
    }

    if (e instanceof Mob mob && mob.getLeashHolder() == player)
      return true;

    return false;
  }
}
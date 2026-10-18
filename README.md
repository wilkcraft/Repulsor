# 🛡️ Repulsor

**Repulsor** is a Forge mod for Minecraft 1.20.1 that adds a single admin-style item: hold it in your off-hand and nothing can get close to you. Crouch, and everything comes to you instead.

It creates a protective bubble around the player, repelling nearby entities, cancelling fall damage, and pulling entities in on demand, all marked by a glowing ring on the ground.

---

## ✨ Features

### 🛡️ Repel Field

- While the item is in your **off-hand**, entities are pushed away so they can't get within **4 blocks** of you.
- Hostile mobs, passive mobs and other players are all affected.
- Projectiles fired by other entities are deflected away from you.

---

### 🌀 Pull Mode

- **Crouch (Shift)** and the field flips: entities within 4 blocks are pulled toward you instead.
- Entities stop a short distance from you (about 1.5 blocks, and about 2.2 for boats and minecarts) so they don't pile on top of you.
- Living entities can climb small ledges and holes while being pulled.

---

### 🪶 No Fall Damage

- You take no fall damage while holding the item in your off-hand.

---

### 🔵 Visual Ring

- A ring on the ground marks the edge of the field and follows you as you move.
- **Blue soul flames** rise from the ground while repelling.
- **Purple mist** flows toward you while pulling.
- Other players nearby can see your ring too.

---

### 👑 Operators Only

- The effects only work for players with operator permissions (permission level 2 or higher).
- In singleplayer, cheats must be enabled.

---

### 💬 Detailed Tooltip

- Hover over the item to see a short description.
- Hold **Shift** in the inventory to see what pull mode does.
- Available in **English** and **Spanish**.

---

## 🚫 What It Doesn't Affect

The field ignores:

- 📦 Dropped items and ⭐ experience orbs
- 🖼️ Item frames and paintings, and 🧍 armor stands
- 🐺 Your own tamed pets, your tamed horses, donkeys, mules and llamas, and any animal you are holding on a leash
- 🚤 Boats that are empty or that you are steering, and anything you are riding
- 🏹 Projectiles you shot yourself
- 👻 Spectators

Projectiles fired by other entities are always deflected, even in pull mode.

---

## 🚀 Getting Started

The item is in the **Tools & Utilities** creative tab. It has no crafting recipe, but you can also get it with:

```text
/give @s repulsor:repulsor_item
```

Then:

1. 🖐️ Put the **Repulsor** in your off-hand.
2. 🛡️ Walk around: nothing can get within 4 blocks of you.
3. 🌀 Crouch to pull nearby entities toward you.
4. 🪶 Jump from any height without taking fall damage.

---

## 🎮 Multiplayer

- Works in both singleplayer and multiplayer.
- All effects are handled server-side for consistent gameplay.
- Operator permissions are checked on the server.

---

## 🛠️ Technical Details

- Server-side, tick-based entity movement.
- Velocity-based repulsion and attraction (no hard walls).
- Ground-following particle ring, rendered on the client for smooth movement.
- Fall damage cancelled through Forge's fall event.
- Item registered through Forge's deferred registers.
- Multiplayer-compatible.

---

## 🛑 Compatibility

- Requires **Minecraft 1.20.1** and **Forge 47.4.10** or newer.
- Must be installed on **both client and server**.
- No other dependencies.
- Compatible with:
  - Singleplayer
  - Multiplayer / dedicated servers

---

## 👤 Author

Developed by **[Wilkcraft](https://github.com/wilkcraft)**

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

🛡️ Keep everything at bay, or pull it all in!

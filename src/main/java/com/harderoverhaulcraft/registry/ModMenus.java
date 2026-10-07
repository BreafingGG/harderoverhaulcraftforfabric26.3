package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import com.harderoverhaulcraft.menu.RockShaperMenu;
import com.harderoverhaulcraft.menu.TieredBeaconMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
	public static final MenuType<RockShaperMenu> ROCK_SHAPER = Registry.register(
		BuiltInRegistries.MENU, HarderOverhaulCraft.id("rock_shaper"), new MenuType<>(RockShaperMenu::new, FeatureFlags.VANILLA_SET)
	);
	public static final MenuType<TieredBeaconMenu> TIERED_BEACON = Registry.register(
		BuiltInRegistries.MENU, HarderOverhaulCraft.id("tiered_beacon"), new MenuType<>(TieredBeaconMenu::new, FeatureFlags.VANILLA_SET)
	);

	public static void init() {
	}

	private ModMenus() {
	}
}

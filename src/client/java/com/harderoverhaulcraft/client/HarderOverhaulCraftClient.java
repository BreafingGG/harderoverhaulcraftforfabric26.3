package com.harderoverhaulcraft.client;

import com.harderoverhaulcraft.client.screen.RockShaperScreen;
import com.harderoverhaulcraft.client.screen.TieredBeaconScreen;
import com.harderoverhaulcraft.registry.ModBlockEntities;
import com.harderoverhaulcraft.registry.ModMenus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class HarderOverhaulCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.ROCK_SHAPER, RockShaperScreen::new);
		MenuScreens.register(ModMenus.TIERED_BEACON, TieredBeaconScreen::new);
		BlockEntityRenderers.register(ModBlockEntities.TIERED_BEACON, ctx -> new BeaconRenderer<>());
	}
}

package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTab {
	public static final CreativeModeTab TAB = Registry.register(
		BuiltInRegistries.CREATIVE_MODE_TAB,
		HarderOverhaulCraft.id("main"),
		FabricCreativeModeTab.builder()
			.title(Component.translatable("itemGroup.harderoverhaulcraft.main"))
			.icon(() -> new ItemStack(ModItems.ROCK_SHAPER))
			.displayItems((params, output) -> {
				for (Item item : ModItems.ALL) {
					output.accept(item);
				}
			})
			.build()
	);

	public static void init() {
	}

	private ModCreativeTab() {
	}
}

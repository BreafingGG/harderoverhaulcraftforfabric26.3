package com.harderoverhaulcraft;

import com.harderoverhaulcraft.registry.ModBlockEntities;
import com.harderoverhaulcraft.registry.ModBlocks;
import com.harderoverhaulcraft.registry.ModCreativeTab;
import com.harderoverhaulcraft.registry.ModItems;
import com.harderoverhaulcraft.registry.ModMenus;
import com.harderoverhaulcraft.registry.ModWorldGen;
import com.harderoverhaulcraft.registry.WoodenToolDurability;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HarderOverhaulCraft implements ModInitializer {
	public static final String MOD_ID = "harderoverhaulcraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();
		ModBlockEntities.init();
		ModMenus.init();
		ModCreativeTab.init();
		ModWorldGen.init();
		WoodenToolDurability.init();
		LOGGER.info("HarderOverhaulCraft initialised - good luck, you'll need it.");
	}
}

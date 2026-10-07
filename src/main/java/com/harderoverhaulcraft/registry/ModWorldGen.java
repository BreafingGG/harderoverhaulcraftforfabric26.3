package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Adds extra ore veins restricted to Y -64..-48 (see data/harderoverhaulcraft/worldgen/placed_feature).
 * Ore generation above Y -48 is untouched; the extra placements roughly double the
 * vanilla vein count in the near-bedrock band.
 */
public final class ModWorldGen {
	private static final String[] DEEP_ORES = {
		"deep_ore_iron", "deep_ore_gold", "deep_ore_copper", "deep_ore_diamond", "deep_ore_diamond_medium"
	};

	public static void init() {
		for (String name : DEEP_ORES) {
			ResourceKey<PlacedFeature> key = ResourceKey.create(Registries.PLACED_FEATURE, HarderOverhaulCraft.id(name));
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, key);
		}
	}

	private ModWorldGen() {
	}
}

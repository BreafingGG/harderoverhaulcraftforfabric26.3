package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModTags {
	/** Blocks (diamond ores) that need at least a gold-tier pickaxe to drop anything. */
	public static final TagKey<Block> NEEDS_GOLD_TOOL = block("needs_gold_tool");
	/** Gold-reinforced iron tools: like iron, but may also harvest {@link #NEEDS_GOLD_TOOL}. */
	public static final TagKey<Block> INCORRECT_FOR_GOLD_REINFORCED_IRON_TOOL = block("incorrect_for_gold_reinforced_iron_tool");

	// Beacon range materials
	public static final TagKey<Block> BEACON_RANGE_LOGS = block("beacon_range/logs");
	public static final TagKey<Block> BEACON_RANGE_STONE_BRICKS = block("beacon_range/stone_bricks");
	public static final TagKey<Block> BEACON_RANGE_IRON = block("beacon_range/iron");
	public static final TagKey<Block> BEACON_RANGE_GOLD = block("beacon_range/gold");
	public static final TagKey<Block> BEACON_RANGE_OBSIDIAN = block("beacon_range/obsidian");
	public static final TagKey<Block> BEACON_RANGE_DIAMOND = block("beacon_range/diamond");
	public static final TagKey<Block> BEACON_RANGE_NETHERITE = block("beacon_range/netherite");

	private static TagKey<Block> block(String path) {
		return TagKey.create(Registries.BLOCK, HarderOverhaulCraft.id(path));
	}

	private ModTags() {
	}
}

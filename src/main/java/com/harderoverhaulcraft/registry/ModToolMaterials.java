package com.harderoverhaulcraft.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

/**
 * Upgraded tool tiers. Values are derived from the vanilla tier they upgrade:
 * gold-reinforced iron = iron +20% durability / +5% speed,
 * diamond+ and netherite+ = +15% durability / +5% speed.
 * Enchantability and repair materials are inherited from the base tier.
 */
public final class ModToolMaterials {
	public static final ToolMaterial GOLD_REINFORCED_IRON = new ToolMaterial(
		ModTags.INCORRECT_FOR_GOLD_REINFORCED_IRON_TOOL,
		scale(ToolMaterial.IRON.durability(), 1.20F),
		ToolMaterial.IRON.speed() * 1.05F,
		ToolMaterial.IRON.attackDamageBonus(),
		ToolMaterial.IRON.enchantmentValue(),
		ItemTags.IRON_TOOL_MATERIALS
	);
	public static final ToolMaterial DIAMOND_PLUS = new ToolMaterial(
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
		scale(ToolMaterial.DIAMOND.durability(), 1.15F),
		ToolMaterial.DIAMOND.speed() * 1.05F,
		ToolMaterial.DIAMOND.attackDamageBonus(),
		ToolMaterial.DIAMOND.enchantmentValue(),
		ItemTags.DIAMOND_TOOL_MATERIALS
	);
	public static final ToolMaterial NETHERITE_PLUS = new ToolMaterial(
		BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
		scale(ToolMaterial.NETHERITE.durability(), 1.15F),
		ToolMaterial.NETHERITE.speed() * 1.05F,
		ToolMaterial.NETHERITE.attackDamageBonus(),
		ToolMaterial.NETHERITE.enchantmentValue(),
		ItemTags.NETHERITE_TOOL_MATERIALS
	);

	private static int scale(int value, float factor) {
		return Math.round(value * factor);
	}

	private ModToolMaterials() {
	}
}

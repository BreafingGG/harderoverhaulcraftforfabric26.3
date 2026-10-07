package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public final class ModItems {
	/** Every item this mod adds, in creative-tab order. */
	public static final List<Item> ALL = new ArrayList<>();

	public static final Item DIAMOND_NUGGET = register("diamond_nugget", Item::new, new Item.Properties());
	public static final Item ROCK_SHARD = register("rock_shard", Item::new, new Item.Properties());

	public static final Item COMPRESSED_COBBLESTONE = block(ModBlocks.COMPRESSED_COBBLESTONE);
	public static final Item ROCK_SHAPER = block(ModBlocks.ROCK_SHAPER);
	public static final Item CRUDE_BEACON = block(ModBlocks.CRUDE_BEACON);
	public static final Item REFINED_BEACON = block(ModBlocks.REFINED_BEACON);

	// Gold-reinforced iron (attack values mirror vanilla iron tools)
	public static final Item GOLD_REINFORCED_IRON_SWORD = sword("gold_reinforced_iron_sword", ModToolMaterials.GOLD_REINFORCED_IRON, false);
	public static final Item GOLD_REINFORCED_IRON_SHOVEL = shovel("gold_reinforced_iron_shovel", ModToolMaterials.GOLD_REINFORCED_IRON, false);
	public static final Item GOLD_REINFORCED_IRON_PICKAXE = pickaxe("gold_reinforced_iron_pickaxe", ModToolMaterials.GOLD_REINFORCED_IRON, false);
	public static final Item GOLD_REINFORCED_IRON_AXE = axe("gold_reinforced_iron_axe", ModToolMaterials.GOLD_REINFORCED_IRON, 6.0F, -3.1F, false);
	public static final Item GOLD_REINFORCED_IRON_HOE = hoe("gold_reinforced_iron_hoe", ModToolMaterials.GOLD_REINFORCED_IRON, -2.0F, -1.0F, false);

	// Diamond+ (attack values mirror vanilla diamond tools)
	public static final Item DIAMOND_PLUS_SWORD = sword("diamond_plus_sword", ModToolMaterials.DIAMOND_PLUS, false);
	public static final Item DIAMOND_PLUS_SHOVEL = shovel("diamond_plus_shovel", ModToolMaterials.DIAMOND_PLUS, false);
	public static final Item DIAMOND_PLUS_PICKAXE = pickaxe("diamond_plus_pickaxe", ModToolMaterials.DIAMOND_PLUS, false);
	public static final Item DIAMOND_PLUS_AXE = axe("diamond_plus_axe", ModToolMaterials.DIAMOND_PLUS, 5.0F, -3.0F, false);
	public static final Item DIAMOND_PLUS_HOE = hoe("diamond_plus_hoe", ModToolMaterials.DIAMOND_PLUS, -3.0F, 0.0F, false);

	// Netherite+ (attack values mirror vanilla netherite tools, fire resistant)
	public static final Item NETHERITE_PLUS_SWORD = sword("netherite_plus_sword", ModToolMaterials.NETHERITE_PLUS, true);
	public static final Item NETHERITE_PLUS_SHOVEL = shovel("netherite_plus_shovel", ModToolMaterials.NETHERITE_PLUS, true);
	public static final Item NETHERITE_PLUS_PICKAXE = pickaxe("netherite_plus_pickaxe", ModToolMaterials.NETHERITE_PLUS, true);
	public static final Item NETHERITE_PLUS_AXE = axe("netherite_plus_axe", ModToolMaterials.NETHERITE_PLUS, 5.0F, -3.0F, true);
	public static final Item NETHERITE_PLUS_HOE = hoe("netherite_plus_hoe", ModToolMaterials.NETHERITE_PLUS, -4.0F, 0.0F, true);

	private static Item.Properties base(boolean fireResistant) {
		Item.Properties p = new Item.Properties();
		return fireResistant ? p.fireResistant() : p;
	}

	private static Item sword(String name, ToolMaterial m, boolean fr) {
		return register(name, Item::new, base(fr).sword(m, 3.0F, -2.4F));
	}

	private static Item shovel(String name, ToolMaterial m, boolean fr) {
		return register(name, Item::new, base(fr).shovel(m, 1.5F, -3.0F));
	}

	private static Item pickaxe(String name, ToolMaterial m, boolean fr) {
		return register(name, Item::new, base(fr).pickaxe(m, 1.0F, -2.8F));
	}

	private static Item axe(String name, ToolMaterial m, float dmg, float speed, boolean fr) {
		return register(name, Item::new, base(fr).axe(m, dmg, speed));
	}

	private static Item hoe(String name, ToolMaterial m, float dmg, float speed, boolean fr) {
		return register(name, Item::new, base(fr).hoe(m, dmg, speed));
	}

	private static Item block(Block block) {
		ResourceKey<Block> blockKey = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
		return register(blockKey.identifier().getPath(), p -> new BlockItem(block, p), new Item.Properties().useBlockDescriptionPrefix());
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, HarderOverhaulCraft.id(name));
		Item item = factory.apply(properties.setId(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.registerBlocks(Item.BY_BLOCK, item);
		}
		ALL.add(item);
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void init() {
	}

	private ModItems() {
	}
}

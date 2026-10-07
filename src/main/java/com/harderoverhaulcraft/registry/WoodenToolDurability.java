package com.harderoverhaulcraft.registry;

import java.util.List;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;

/** Wooden tools keep only 30% of their vanilla durability (59 -> 17). */
public final class WoodenToolDurability {
	public static final float FACTOR = 0.30F;

	public static int reducedDurability() {
		return Math.max(1, (int) Math.floor(ToolMaterial.WOOD.durability() * FACTOR));
	}

	public static void init() {
		List<Item> woodenTools = List.of(Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_HOE);
		DefaultItemComponentEvents.MODIFY.register(context ->
			context.modify(woodenTools, (builder, item) -> builder.set(DataComponents.MAX_DAMAGE, reducedDurability()))
		);
	}

	private WoodenToolDurability() {
	}
}

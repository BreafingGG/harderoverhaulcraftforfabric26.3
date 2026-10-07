package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import com.harderoverhaulcraft.beacon.TieredBeaconBlockEntity;
import com.harderoverhaulcraft.block.RockShaperBlockEntity;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
	public static final BlockEntityType<RockShaperBlockEntity> ROCK_SHAPER = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		HarderOverhaulCraft.id("rock_shaper"),
		new BlockEntityType<>(RockShaperBlockEntity::new, Set.of(ModBlocks.ROCK_SHAPER))
	);
	public static final BlockEntityType<TieredBeaconBlockEntity> TIERED_BEACON = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		HarderOverhaulCraft.id("tiered_beacon"),
		new BlockEntityType<>(TieredBeaconBlockEntity::new, Set.of(ModBlocks.CRUDE_BEACON, ModBlocks.REFINED_BEACON))
	);

	public static void init() {
	}

	private ModBlockEntities() {
	}
}

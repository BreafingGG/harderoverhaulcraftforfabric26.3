package com.harderoverhaulcraft.registry;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import com.harderoverhaulcraft.beacon.BeaconTier;
import com.harderoverhaulcraft.beacon.TieredBeaconBlock;
import com.harderoverhaulcraft.block.RockShaperBlock;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {
	public static final Block COMPRESSED_COBBLESTONE = register(
		"compressed_cobblestone",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).strength(3.0F, 8.0F)
	);
	public static final Block ROCK_SHAPER = register(
		"rock_shaper",
		RockShaperBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.5F).requiresCorrectToolForDrops().sound(SoundType.STONE)
	);
	public static final Block CRUDE_BEACON = register(
		"crude_beacon",
		p -> new TieredBeaconBlock(BeaconTier.CRUDE, p),
		BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).lightLevel(s -> 10).sound(SoundType.STONE)
	);
	public static final Block REFINED_BEACON = register(
		"refined_beacon",
		p -> new TieredBeaconBlock(BeaconTier.REFINED, p),
		BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F).lightLevel(s -> 13).sound(SoundType.METAL)
	);

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, HarderOverhaulCraft.id(name));
		return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties.setId(key)));
	}

	public static void init() {
	}

	private ModBlocks() {
	}
}

package com.harderoverhaulcraft.beacon;

import com.harderoverhaulcraft.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base (tier 1) range from the 3x3 layer directly below a beacon.
 * The most common material wins; ties go to the higher-value material.
 */
public final class BeaconRange {
	private static final TagKey<Block>[] MATERIALS = new TagKey[]{
		ModTags.BEACON_RANGE_LOGS, ModTags.BEACON_RANGE_STONE_BRICKS, ModTags.BEACON_RANGE_IRON,
		ModTags.BEACON_RANGE_GOLD, ModTags.BEACON_RANGE_OBSIDIAN, ModTags.BEACON_RANGE_DIAMOND,
		ModTags.BEACON_RANGE_NETHERITE
	};
	private static final int[] BASE_RANGE = {16, 32, 48, 64, 80, 96, 112};

	public static int baseRange(Level level, BlockPos beaconPos) {
		int[] counts = new int[MATERIALS.length];
		BlockPos below = beaconPos.below();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				BlockState state = level.getBlockState(below.offset(dx, 0, dz));
				for (int i = MATERIALS.length - 1; i >= 0; i--) {
					if (state.is(MATERIALS[i])) {
						counts[i]++;
						break;
					}
				}
			}
		}
		int best = -1;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > 0 && (best < 0 || counts[i] >= counts[best])) best = i;
		}
		return best < 0 ? 0 : BASE_RANGE[best];
	}

	public static int scaledRange(Level level, BlockPos pos, double multiplier) {
		return (int) Math.ceil(baseRange(level, pos) * multiplier);
	}

	private BeaconRange() {
	}
}

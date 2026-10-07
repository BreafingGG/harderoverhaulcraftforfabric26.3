package com.harderoverhaulcraft.mixin;

import com.harderoverhaulcraft.beacon.BeaconRange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Tier 3 (vanilla) beacon: range = base range from the layer under the beacon x2. */
@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
	@ModifyVariable(method = "applyEffects", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private static double harderoverhaulcraft$tierRange(double range, Level level, BlockPos pos, int levels, Holder<MobEffect> primary, Holder<MobEffect> secondary) {
		int custom = BeaconRange.scaledRange(level, pos, 2.0);
		return custom > 0 ? custom : range;
	}
}

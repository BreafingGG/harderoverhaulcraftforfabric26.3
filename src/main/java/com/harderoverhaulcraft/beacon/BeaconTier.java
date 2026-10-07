package com.harderoverhaulcraft.beacon;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

/** Tier 1 (crude) and tier 2 (refined) beacons. Tier 3 is the vanilla beacon (see BeaconBlockEntityMixin). */
public enum BeaconTier {
	CRUDE(1.0, 0xFFE0B341, List.of(new Option(MobEffects.SPEED, 0), new Option(MobEffects.HASTE, 0))),
	REFINED(1.5, 0xFF7FE3FF, List.of(
		new Option(MobEffects.SPEED, 0), new Option(MobEffects.SPEED, 1),
		new Option(MobEffects.HASTE, 0), new Option(MobEffects.HASTE, 1),
		new Option(MobEffects.RESISTANCE, 0), new Option(MobEffects.JUMP_BOOST, 0)
	));

	public final double rangeMultiplier;
	public final int beamColor;
	public final List<Option> options;

	BeaconTier(double rangeMultiplier, int beamColor, List<Option> options) {
		this.rangeMultiplier = rangeMultiplier;
		this.beamColor = beamColor;
		this.options = options;
	}

	public record Option(Holder<MobEffect> effect, int amplifier) {
	}
}

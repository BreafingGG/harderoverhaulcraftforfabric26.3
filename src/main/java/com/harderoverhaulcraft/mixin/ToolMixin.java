package com.harderoverhaulcraft.mixin;

import com.harderoverhaulcraft.registry.ModTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Golden pickaxes may harvest blocks tagged harderoverhaulcraft:needs_gold_tool (diamond ores). */
@Mixin(Item.class)
public abstract class ToolMixin {
	@Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
	private void harderoverhaulcraft$goldDiamond(ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(Items.GOLDEN_PICKAXE) && state.is(ModTags.NEEDS_GOLD_TOOL)) cir.setReturnValue(true);
	}
}

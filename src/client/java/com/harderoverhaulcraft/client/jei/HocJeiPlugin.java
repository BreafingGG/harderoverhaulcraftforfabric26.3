package com.harderoverhaulcraft.client.jei;

import com.harderoverhaulcraft.HarderOverhaulCraft;
import com.harderoverhaulcraft.block.RockShaperBlockEntity;
import com.harderoverhaulcraft.registry.ModBlocks;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class HocJeiPlugin implements IModPlugin {
	@Override public Identifier getPluginUid() { return HarderOverhaulCraft.id("jei_plugin"); }

	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {
		reg.addRecipeCategories(new RockShaperJeiCategory(reg.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		reg.addRecipes(RockShaperJeiCategory.TYPE, List.of(
			new RockShaperRecipe(false, RockShaperBlockEntity.BASIC_TIME),
			new RockShaperRecipe(true, RockShaperBlockEntity.LAVA_TIME)));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		reg.addCraftingStation(RockShaperJeiCategory.TYPE, ModBlocks.ROCK_SHAPER);
	}
}

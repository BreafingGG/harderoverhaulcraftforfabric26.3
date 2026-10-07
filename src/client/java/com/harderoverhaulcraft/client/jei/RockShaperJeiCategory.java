package com.harderoverhaulcraft.client.jei;

import com.harderoverhaulcraft.registry.ModBlocks;
import com.harderoverhaulcraft.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class RockShaperJeiCategory implements IRecipeCategory<RockShaperRecipe> {
	public static final IRecipeType<RockShaperRecipe> TYPE = IRecipeType.create("harderoverhaulcraft", "rock_shaper", RockShaperRecipe.class);
	private final IDrawable icon;

	public RockShaperJeiCategory(IGuiHelper gui) {
		icon = gui.createDrawableItemLike(ModBlocks.ROCK_SHAPER);
	}

	@Override public IRecipeType<RockShaperRecipe> getRecipeType() { return TYPE; }
	@Override public Component getTitle() { return Component.translatable("jei.harderoverhaulcraft.rock_shaper"); }
	@Override public int getWidth() { return 120; }
	@Override public int getHeight() { return 54; }
	@Override public IDrawable getIcon() { return icon; }

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RockShaperRecipe recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 1).add(Items.COBBLESTONE).setStandardSlotBackground();
		if (recipe.lava()) builder.addInputSlot(1, 33).add(Items.LAVA_BUCKET).setStandardSlotBackground();
		builder.addOutputSlot(61, 17).add(ModItems.ROCK_SHARD).setOutputSlotBackground();
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RockShaperRecipe recipe, IFocusGroup focuses) {
		builder.addAnimatedRecipeArrow(recipe.ticks()).setPosition(26, 17);
	}

	@Override
	public void draw(RockShaperRecipe recipe, IRecipeSlotsView view, GuiGraphicsExtractor g, double mouseX, double mouseY) {
		var font = Minecraft.getInstance().font;
		g.text(font, Component.translatable(recipe.lava() ? "jei.harderoverhaulcraft.lava_mode" : "jei.harderoverhaulcraft.basic_mode"), 24, 38, 0xFF404040, false);
		g.text(font, Component.translatable("jei.harderoverhaulcraft.seconds", recipe.ticks() / 20), 84, 22, 0xFF404040, false);
	}
}

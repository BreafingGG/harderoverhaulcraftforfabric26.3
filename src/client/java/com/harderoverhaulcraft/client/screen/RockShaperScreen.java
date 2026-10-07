package com.harderoverhaulcraft.client.screen;

import com.harderoverhaulcraft.block.RockShaperBlockEntity;
import com.harderoverhaulcraft.menu.RockShaperMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class RockShaperScreen extends AbstractContainerScreen<RockShaperMenu> {
	private static final Identifier BG = Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
	private static final Identifier ARROW = Identifier.withDefaultNamespace("container/furnace/burn_progress");

	public RockShaperScreen(RockShaperMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
		super.extractBackground(g, mouseX, mouseY, a);
		int x = leftPos, y = topPos;
		g.blit(RenderPipelines.GUI_TEXTURED, BG, x, y, 0f, 0f, imageWidth, imageHeight, 256, 256);
		// cover the vanilla flame area, draw lava gauge (0-64)
		g.fill(x + 55, y + 36, x + 73, y + 52, 0xFFC6C6C6);
		int lava = menu.getLavaOps();
		g.fill(x + 30, y + 17, x + 40, y + 69, 0xFF373737);
		int h = lava * 50 / RockShaperBlockEntity.OPS_PER_BUCKET;
		g.fill(x + 31, y + 68 - h, x + 39, y + 68, 0xFFFF7A00);
		int total = menu.getTotalTime();
		int w = total > 0 ? Math.min(24, menu.getProgress() * 24 / total) : 0;
		if (w > 0) g.blitSprite(RenderPipelines.GUI_TEXTURED, ARROW, 24, 16, 0, 0, x + 79, y + 34, w, 16);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		super.extractLabels(g, mouseX, mouseY);
		int lava = menu.getLavaOps();
		g.text(font, Component.translatable("gui.harderoverhaulcraft.lava", lava, RockShaperBlockEntity.OPS_PER_BUCKET), 100, 60, 0xFFB04000, false);
		g.text(font, Component.translatable(lava > 0 ? "gui.harderoverhaulcraft.mode_lava" : "gui.harderoverhaulcraft.mode_basic"), 100, 70, 0xFF404040, false);
	}
}

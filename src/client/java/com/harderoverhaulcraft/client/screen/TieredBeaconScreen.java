package com.harderoverhaulcraft.client.screen;

import com.harderoverhaulcraft.beacon.BeaconTier;
import com.harderoverhaulcraft.menu.TieredBeaconMenu;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TieredBeaconScreen extends AbstractContainerScreen<TieredBeaconMenu> {
	private final List<Button> buttons = new ArrayList<>();

	public TieredBeaconScreen(TieredBeaconMenu menu, Inventory inv, Component title) {
		super(menu, inv, title, 176, 130);
	}

	public static Component optionName(BeaconTier.Option o) {
		return Component.empty().append(o.effect().value().getDisplayName())
			.append(" ").append(Component.translatable("enchantment.level." + (o.amplifier() + 1)));
	}

	@Override
	protected void init() {
		super.init();
		buttons.clear();
		List<BeaconTier.Option> opts = BeaconTier.values()[BeaconTier.values().length - 1].options;
		for (int i = 0; i < opts.size(); i++) {
			final int id = i;
			Button b = Button.builder(optionName(opts.get(i)), btn -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id))
				.bounds(leftPos + 8 + (i % 2) * 82, topPos + 30 + (i / 2) * 24, 78, 20).build();
			buttons.add(addRenderableWidget(b));
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
		super.extractBackground(g, mouseX, mouseY, a);
		g.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF2B2B3A);
		g.fill(leftPos + 1, topPos + 1, leftPos + imageWidth - 1, topPos + imageHeight - 1, 0xFF3C3C55);
		List<BeaconTier.Option> opts = menu.getTier().options;
		for (int i = 0; i < buttons.size(); i++) {
			Button b = buttons.get(i);
			b.visible = i < opts.size();
			if (b.visible) {
				b.setMessage(optionName(opts.get(i)));
				if (i == menu.getSelected()) g.fill(b.getX() - 2, b.getY() - 2, b.getX() + b.getWidth() + 2, b.getY() + b.getHeight() + 2, 0xFFFFD700);
			}
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		g.text(font, title, 8, 6, 0xFFFFFFFF, false);
		int range = menu.getRange();
		Component r = range > 0 ? Component.translatable("gui.harderoverhaulcraft.range", range) : Component.translatable("gui.harderoverhaulcraft.no_base");
		g.text(font, r, 8, 17, 0xFFE0E0E0, false);
	}
}

package com.harderoverhaulcraft.menu;

import com.harderoverhaulcraft.block.RockShaperBlockEntity;
import com.harderoverhaulcraft.registry.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Slot layout matches the vanilla furnace texture: input (56,17), fuel (56,53), output (116,35). */
public class RockShaperMenu extends AbstractContainerMenu {
	private final Container container;
	private final ContainerData data;

	public RockShaperMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, new SimpleContainer(3), new SimpleContainerData(RockShaperBlockEntity.DATA_COUNT));
	}

	public RockShaperMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
		super(ModMenus.ROCK_SHAPER, containerId);
		checkContainerSize(container, 3);
		this.container = container;
		this.data = data;
		addSlot(new Slot(container, RockShaperBlockEntity.SLOT_INPUT, 56, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return RockShaperBlockEntity.isInput(stack);
			}
		});
		addSlot(new Slot(container, RockShaperBlockEntity.SLOT_FUEL, 56, 53) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Items.LAVA_BUCKET);
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});
		addSlot(new Slot(container, RockShaperBlockEntity.SLOT_OUTPUT, 116, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});
		addDataSlots(data);
		addStandardInventorySlots(inventory, 8, 84);
	}

	public int getProgress() {
		return data.get(RockShaperBlockEntity.DATA_PROGRESS);
	}

	public int getTotalTime() {
		return data.get(RockShaperBlockEntity.DATA_TOTAL);
	}

	public int getLavaOps() {
		return data.get(RockShaperBlockEntity.DATA_LAVA);
	}

	@Override
	public boolean stillValid(Player player) {
		return container.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot.hasItem()) {
			ItemStack stack = slot.getItem();
			result = stack.copy();
			if (index < 3) {
				if (!moveItemStackTo(stack, 3, 39, true)) return ItemStack.EMPTY;
				slot.onQuickCraft(stack, result);
			} else if (RockShaperBlockEntity.isInput(stack)) {
				if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
			} else if (stack.is(Items.LAVA_BUCKET)) {
				if (!moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
			} else if (index < 30) {
				if (!moveItemStackTo(stack, 30, 39, false)) return ItemStack.EMPTY;
			} else if (!moveItemStackTo(stack, 3, 30, false)) {
				return ItemStack.EMPTY;
			}
			if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
			else slot.setChanged();
			if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;
			slot.onTake(player, stack);
		}
		return result;
	}
}

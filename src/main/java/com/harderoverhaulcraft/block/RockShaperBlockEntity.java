package com.harderoverhaulcraft.block;

import com.harderoverhaulcraft.menu.RockShaperMenu;
import com.harderoverhaulcraft.registry.ModBlockEntities;
import com.harderoverhaulcraft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/**
 * Turns cobblestone into rock shards.
 * Basic mode: 3600 ticks (3 min) per shard. Lava mode: 200 ticks (10 s) per shard;
 * a lava bucket in the fuel slot is poured into an internal tank worth 64 operations
 * and the empty bucket is handed back in the fuel slot.
 */
public class RockShaperBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
	public static final int SLOT_INPUT = 0, SLOT_FUEL = 1, SLOT_OUTPUT = 2;
	public static final int BASIC_TIME = 3600, LAVA_TIME = 200, OPS_PER_BUCKET = 64;
	public static final int DATA_PROGRESS = 0, DATA_TOTAL = 1, DATA_LAVA = 2, DATA_COUNT = 3;

	private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	private int progress;
	private int lavaOps;

	private final ContainerData data = new ContainerData() {
		@Override
		public int get(int id) {
			return switch (id) {
				case DATA_PROGRESS -> progress;
				case DATA_TOTAL -> currentTotalTime();
				case DATA_LAVA -> lavaOps;
				default -> 0;
			};
		}

		@Override
		public void set(int id, int value) {
			if (id == DATA_PROGRESS) progress = value;
			else if (id == DATA_LAVA) lavaOps = value;
		}

		@Override
		public int getCount() {
			return DATA_COUNT;
		}
	};

	public RockShaperBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ROCK_SHAPER, pos, state);
	}

	private int currentTotalTime() {
		return lavaOps > 0 ? LAVA_TIME : BASIC_TIME;
	}

	public static boolean isInput(ItemStack stack) {
		return stack.is(Items.COBBLESTONE);
	}

	public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, RockShaperBlockEntity be) {
		if (!(level instanceof ServerLevel)) return;
		boolean dirty = false;

		// Refuel: pour a lava bucket into the tank once it is empty.
		ItemStack fuel = be.items.get(SLOT_FUEL);
		if (be.lavaOps <= 0 && fuel.is(Items.LAVA_BUCKET)) {
			be.lavaOps = OPS_PER_BUCKET;
			be.items.set(SLOT_FUEL, new ItemStack(Items.BUCKET));
			dirty = true;
		}

		ItemStack input = be.items.get(SLOT_INPUT);
		ItemStack output = be.items.get(SLOT_OUTPUT);
		boolean canOutput = output.isEmpty() || (output.is(ModItems.ROCK_SHARD) && output.getCount() < output.getMaxStackSize());
		if (isInput(input) && canOutput) {
			be.progress++;
			if (be.progress >= be.currentTotalTime()) {
				be.progress = 0;
				input.shrink(1);
				if (output.isEmpty()) be.items.set(SLOT_OUTPUT, new ItemStack(ModItems.ROCK_SHARD));
				else output.grow(1);
				if (be.lavaOps > 0) be.lavaOps--;
			}
			dirty = true;
		} else if (be.progress != 0) {
			be.progress = 0;
			dirty = true;
		}
		if (dirty) be.setChanged();
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.harderoverhaulcraft.rock_shaper");
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new RockShaperMenu(containerId, inventory, this, data);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, items);
		progress = input.getIntOr("progress", 0);
		lavaOps = input.getIntOr("lava_ops", 0);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		ContainerHelper.saveAllItems(output, items);
		output.putInt("progress", progress);
		output.putInt("lava_ops", lavaOps);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return switch (slot) {
			case SLOT_INPUT -> isInput(stack);
			case SLOT_FUEL -> stack.is(Items.LAVA_BUCKET);
			default -> false;
		};
	}

	private static final int[] UP = {SLOT_INPUT}, DOWN = {SLOT_OUTPUT, SLOT_FUEL}, SIDES = {SLOT_FUEL};

	@Override
	public int[] getSlotsForFace(Direction direction) {
		return direction == Direction.UP ? UP : direction == Direction.DOWN ? DOWN : SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return slot == SLOT_OUTPUT || (slot == SLOT_FUEL && stack.is(Items.BUCKET));
	}
}

package com.harderoverhaulcraft.menu;

import com.harderoverhaulcraft.beacon.BeaconTier;
import com.harderoverhaulcraft.beacon.TieredBeaconBlockEntity;
import com.harderoverhaulcraft.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class TieredBeaconMenu extends AbstractContainerMenu {
	private final ContainerData data;
	private final ContainerLevelAccess access;

	public TieredBeaconMenu(int id, Inventory inv) {
		this(id, inv, new SimpleContainerData(TieredBeaconBlockEntity.DATA_COUNT), ContainerLevelAccess.NULL);
	}

	public TieredBeaconMenu(int id, Inventory inv, ContainerData data, ContainerLevelAccess access) {
		super(ModMenus.TIERED_BEACON, id);
		checkContainerDataCount(data, TieredBeaconBlockEntity.DATA_COUNT);
		this.data = data;
		this.access = access;
		addDataSlots(data);
	}

	public BeaconTier getTier() {
		int t = data.get(TieredBeaconBlockEntity.DATA_TIER);
		BeaconTier[] v = BeaconTier.values();
		return v[Math.max(0, Math.min(v.length - 1, t))];
	}

	public int getSelected() { return data.get(TieredBeaconBlockEntity.DATA_SELECTED); }

	public int getRange() { return data.get(TieredBeaconBlockEntity.DATA_RANGE); }

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id < 0 || id >= getTier().options.size()) return false;
		data.set(TieredBeaconBlockEntity.DATA_SELECTED, id);
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }

	@Override
	public boolean stillValid(Player player) {
		return access.evaluate((level, pos) -> player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0, true);
	}
}

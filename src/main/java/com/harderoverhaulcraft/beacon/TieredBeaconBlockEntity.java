package com.harderoverhaulcraft.beacon;

import com.harderoverhaulcraft.menu.TieredBeaconMenu;
import com.harderoverhaulcraft.registry.ModBlockEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class TieredBeaconBlockEntity extends BlockEntity implements MenuProvider, BeaconBeamOwner {
	public static final int DATA_SELECTED = 0, DATA_RANGE = 1, DATA_TIER = 2, DATA_COUNT = 3;

	private int selected = -1;
	private int range;
	private List<BeaconBeamOwner.Section> beam = List.of();

	private final ContainerData data = new ContainerData() {
		@Override
		public int get(int id) {
			return id == DATA_SELECTED ? selected : id == DATA_RANGE ? range : id == DATA_TIER ? getTier().ordinal() : 0;
		}

		@Override
		public void set(int id, int value) {
			if (id == DATA_SELECTED) {
				selected = value;
				setChanged();
			}
		}

		@Override
		public int getCount() {
			return DATA_COUNT;
		}
	};

	public TieredBeaconBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TIERED_BEACON, pos, state);
	}

	public BeaconTier getTier() {
		return getBlockState().getBlock() instanceof TieredBeaconBlock b ? b.getTier() : BeaconTier.CRUDE;
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TieredBeaconBlockEntity be) {
		if (level.getGameTime() % 80L != 0L) return;
		BeaconTier tier = be.getTier();
		be.range = BeaconRange.scaledRange(level, pos, tier.rangeMultiplier);
		be.beam = be.range > 0 && hasSkyAccess(level, pos) ? List.of(new BeaconBeamOwner.Section(tier.beamColor)) : List.of();
		if (level.isClientSide() || be.beam.isEmpty() || be.selected < 0 || be.selected >= tier.options.size()) return;

		BeaconTier.Option option = tier.options.get(be.selected);
		AABB box = new AABB(pos).inflate(be.range).expandTowards(0.0, level.getHeight(), 0.0);
		for (Player player : level.getEntitiesOfClass(Player.class, box)) {
			player.addEffect(new MobEffectInstance(option.effect(), 260, option.amplifier(), true, true));
		}
	}

	private static boolean hasSkyAccess(Level level, BlockPos pos) {
		int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
		for (BlockPos p = pos.above(); p.getY() < top; p = p.above()) {
			if (level.getBlockState(p).getLightDampening() >= 15) return false;
		}
		return true;
	}

	@Override
	public List<BeaconBeamOwner.Section> getBeamSections() {
		return beam;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		selected = input.getIntOr("selected", -1);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("selected", selected);
	}

	@Override
	public Component getDisplayName() {
		return getBlockState().getBlock().getName();
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		return new TieredBeaconMenu(containerId, inventory, data, ContainerLevelAccess.create(level, worldPosition));
	}
}

package com.harderoverhaulcraft.beacon;

import com.harderoverhaulcraft.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class TieredBeaconBlock extends BaseEntityBlock {
	private final BeaconTier tier;

	public TieredBeaconBlock(BeaconTier tier, BlockBehaviour.Properties properties) {
		super(properties);
		this.tier = tier;
	}

	public BeaconTier getTier() {
		return tier;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TieredBeaconBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModBlockEntities.TIERED_BEACON, TieredBeaconBlockEntity::tick);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide() && level.getBlockEntity(pos) instanceof TieredBeaconBlockEntity be) {
			player.openMenu(be);
		}
		return InteractionResult.SUCCESS;
	}
}

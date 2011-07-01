package net.llamaslayers.minecraft.metroprotecto;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MetroProtectoBlockListener extends BlockListener {
	private final MetroProtecto plugin;

	public MetroProtectoBlockListener(MetroProtecto plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (!plugin.isNearTrain(event.getBlock().getLocation()))
			return;
		if (event.getPlayer() == null) {
			event.setCancelled(true);
			return;
		}
		if (plugin.checkPerm(event.getPlayer(),
				"metroprotecto.neartracks.destroy"))
			return;
		event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (MetroProtecto.trackMaterials.contains(event.getBlockPlaced()
				.getType())
				&& plugin.checkPerm(event.getPlayer(),
						"metroprotecto.protecttracks.build")) {
			plugin.protectLocaton(event.getBlockPlaced().getLocation());
			return;
		}
		if (plugin.isNearTrain(event.getBlockPlaced().getLocation())) {
			if (!plugin.checkPerm(event.getPlayer(),
					"metroprotecto.neartracks.build")) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (!plugin.isNearTrain(event.getBlock().getLocation()))
			return;
		if (event.getPlayer() == null) {
			event.setCancelled(true);
			return;
		}
		if (MetroProtecto.trackMaterials.contains(event.getBlock().getType())) {
			if (!plugin.checkPerm(event.getPlayer(),
					"metroprotecto.protecttracks.destroy")) {
				event.setCancelled(true);
				return;
			}
			plugin.unprotectLocaton(event.getBlock().getLocation());
			return;
		}
		if (!plugin.checkPerm(event.getPlayer(),
				"metroprotecto.neartracks.destroy")) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onBlockFromTo(BlockFromToEvent event) {
		if (plugin.isNearTrain(event.getToBlock().getLocation())) {
			event.setCancelled(true);
		}
	}
}

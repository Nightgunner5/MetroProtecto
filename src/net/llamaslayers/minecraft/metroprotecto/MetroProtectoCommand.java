package net.llamaslayers.minecraft.metroprotecto;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MetroProtectoCommand implements CommandExecutor {
	private final MetroProtecto plugin;
	protected boolean tooSoon = false;

	public MetroProtectoCommand(MetroProtecto plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (tooSoon)
			return true;
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;

		if (!plugin.checkPerm(player, "metroprotecto.protecttracks.find"))
			return true;

		ArrayList<Block> blocks = new ArrayList<Block>();
		int apothem = plugin.getConfiguration().getInt("search_radius", 20);
		for (int x = -apothem; x <= apothem; x++) {
			for (int y = -apothem; y <= apothem; y++) {
				for (int z = -apothem; z <= apothem; z++) {
					Location loc = player.getLocation().add(x, y, z);
					if (plugin.isSpaceProtected(loc)) {
						blocks.add(loc.getBlock());
					}
				}
			}
		}
		player.sendMessage(blocks.size() + " tracks found");
		plugin.temporaryChangeBlock(player, blocks, Material.REDSTONE_TORCH_ON,
				(byte) 0, 1000);

		return true;
	}
}

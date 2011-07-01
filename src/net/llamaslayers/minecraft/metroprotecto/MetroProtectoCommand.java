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
		for (int x = -5; x <= 5; x++) {
			for (int y = -5; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
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

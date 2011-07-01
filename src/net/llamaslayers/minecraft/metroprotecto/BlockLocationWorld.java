package net.llamaslayers.minecraft.metroprotecto;

import java.io.Serializable;

import org.bukkit.Location;

public class BlockLocationWorld extends BlockLocation implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String world;

	public BlockLocationWorld(Location location) {
		super(location);
		world = location.getWorld().getName();
	}

	@Override
	public String toString() {
		return "BlockLocationWorld " + world + " (" + x + ", " + y + ", " + z
				+ ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof BlockLocationWorld))
			return false;
		BlockLocationWorld other = (BlockLocationWorld) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		return true;
	}
}

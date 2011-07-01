package net.llamaslayers.minecraft.metroprotecto;

import java.io.Serializable;

import org.bukkit.Location;

public class BlockLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	public final int x;
	public final int y;
	public final int z;

	public BlockLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLocation(Location location) {
		this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	@Override
	public String toString() {
		return "BlockLocation (" + x + ", " + y + ", " + z + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlockLocation))
			return false;
		BlockLocation other = (BlockLocation) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}

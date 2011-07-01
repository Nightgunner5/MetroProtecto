package net.llamaslayers.minecraft.metroprotecto;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionLocation {
	private static final int REGION_CHUNK_SIZE = 32;
	private static final int REGION_BLOCK_SIZE = 512;
	public final int x;
	public final int z;
	public final String world;

	public RegionLocation(String world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public RegionLocation(World world, int x, int z) {
		this(world.getName(), x, z);
	}

	public RegionLocation(Chunk chunk) {
		this(chunk.getWorld().getName(), chunk.getX() / REGION_CHUNK_SIZE,
				chunk.getZ() / REGION_CHUNK_SIZE);
	}

	public RegionLocation(Location location) {
		this(location.getWorld().getName(), location.getBlockX()
				/ REGION_BLOCK_SIZE, location.getBlockZ() / REGION_BLOCK_SIZE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RegionLocation))
			return false;
		RegionLocation other = (RegionLocation) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}

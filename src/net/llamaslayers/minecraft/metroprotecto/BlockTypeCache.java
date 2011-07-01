package net.llamaslayers.minecraft.metroprotecto;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockTypeCache extends BlockLocationWorld implements Serializable {
	private static final long serialVersionUID = 1L;
	public final int type;
	public final byte data;

	public BlockTypeCache(Block block) {
		super(block.getLocation());
		type = block.getTypeId();
		data = block.getData();
	}

	public void apply(Block block) {
		block.setTypeIdAndData(type, data, false);
	}

	@Override
	public String toString() {
		return "BlockTypeCache " + world + " (" + x + ", " + y + ", " + z
				+ ") " + Material.getMaterial(type).name() + " (" + data + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + data;
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof BlockTypeCache))
			return false;
		BlockTypeCache other = (BlockTypeCache) obj;
		if (data != other.data)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}

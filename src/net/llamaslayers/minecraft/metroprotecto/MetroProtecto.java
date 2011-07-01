package net.llamaslayers.minecraft.metroprotecto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class MetroProtecto extends JavaPlugin {
	public static final List<Material> trackMaterials;
	static {
		ArrayList<Material> materials = new ArrayList<Material>();
		materials.add(Material.RAILS);
		materials.add(Material.POWERED_RAIL);
		materials.add(Material.DETECTOR_RAIL);
		trackMaterials = Collections.unmodifiableList(materials);
	}
	private PermissionHandler permissionHandler;
	private final HashMap<RegionLocation, ArrayList<BlockLocation>> trackCache = new HashMap<RegionLocation, ArrayList<BlockLocation>>();

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		setupPermissions();

		MetroProtectoBlockListener blockListener = new MetroProtectoBlockListener(
				this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_FROMTO, blockListener,
				Event.Priority.Normal, this);
	}

	private void setupPermissions() {
		if (permissionHandler != null)
			return;

		Plugin permissionsPlugin = getServer().getPluginManager().getPlugin(
				"Permissions");

		if (permissionsPlugin == null)
			return;

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	}

	public boolean checkPerm(Player player, String perm) {
		if (permissionHandler == null)
			return player.isOp();
		return permissionHandler.has(player, perm);
	}

	private enum ProtectionBoundType {
		XZ, Y
	}

	private int getTrackLookBound(int start, boolean additive,
			ProtectionBoundType type) {
		if (type == ProtectionBoundType.Y)
			return Math.min(
					Math.max(
							start
									+ getConfiguration().getInt(
											"protection_height", 5)
									* (additive ? 1 : -1), 0), 127);
		return start + getConfiguration().getInt("protection_radius", 3)
				* (additive ? 1 : -1);
	}

	@SuppressWarnings("unchecked")
	private boolean isSpaceProtected(Location location) {
		RegionLocation region = new RegionLocation(location);
		ArrayList<BlockLocation> tracks;
		if (trackCache.containsKey(region)) {
			tracks = trackCache.get(region);
		} else {
			File worldDir = new File(getDataFolder(), region.world);
			if (!worldDir.exists())
				return false;
			File regionFile = new File(worldDir, "r." + region.x + "."
					+ region.z + ".dat");
			if (!regionFile.exists())
				return false;
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(regionFile));
				tracks = (ArrayList<BlockLocation>) in.readObject();
				in.close();
				trackCache.put(region, tracks);
			} catch (IOException ex) {
				return false;
			} catch (ClassNotFoundException ex) {
				return false;
			}
		}

		return tracks.contains(new int[] { location.getBlockX(),
				location.getBlockY(), location.getBlockZ() });
	}

	public boolean isNearTrain(Location location) {
		int x1 = getTrackLookBound(location.getBlockX(), false,
				ProtectionBoundType.XZ), x2 = getTrackLookBound(
				location.getBlockX(), true, ProtectionBoundType.XZ), z1 = getTrackLookBound(
				location.getBlockZ(), false, ProtectionBoundType.XZ), z2 = getTrackLookBound(
				location.getBlockZ(), true, ProtectionBoundType.XZ), y1 = getTrackLookBound(
				location.getBlockY(), false, ProtectionBoundType.Y), y2 = getTrackLookBound(
				location.getBlockY(), true, ProtectionBoundType.Y);

		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					if (isSpaceProtected(new Location(location.getWorld(), x,
							y, z)))
						return true;
				}
			}
		}

		return false;
	}

	private void saveRegionFile(RegionLocation region,
			ArrayList<BlockLocation> locations) {
		File worldDir = new File(getDataFolder(), region.world);
		worldDir.mkdirs();
		File regionFile = new File(worldDir, "r." + region.x + "." + region.z
				+ ".dat");

		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(regionFile));
			out.writeObject(locations);
			out.close();
		} catch (IOException ex) {
		}
	}

	public void protectLocaton(Location location) {
		if (isSpaceProtected(location))
			return;

		RegionLocation region = new RegionLocation(location);
		ArrayList<BlockLocation> tracks;
		if (trackCache.containsKey(region)) {
			tracks = trackCache.get(region);
		} else {
			tracks = new ArrayList<BlockLocation>();
			trackCache.put(region, tracks);
		}

		tracks.add(new BlockLocation(location));

		saveRegionFile(region, tracks);
	}

	public void unprotectLocaton(Location location) {
		if (!isSpaceProtected(location))
			return;

		RegionLocation region = new RegionLocation(location);
		ArrayList<BlockLocation> tracks;
		if (trackCache.containsKey(region)) {
			tracks = trackCache.get(region);
		} else
			return;

		tracks.remove(new BlockLocation(location));

		saveRegionFile(region, tracks);
	}
}

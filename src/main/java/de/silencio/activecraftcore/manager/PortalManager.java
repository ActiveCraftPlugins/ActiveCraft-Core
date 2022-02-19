package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.ComparisonType;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.Portal;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

public class PortalManager {

    public static void create(String name, int x, int y, int z, World world, int to_x, int to_y, int to_z, World to_world) {
        ConfigManager.portalsConfig.set(name, new Portal(
                name, x, y, z, world, to_x, to_y, to_z, to_world
        ), true);
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.END_GATEWAY);
        EndGateway endGateway = (EndGateway) block.getState();
        endGateway.setExactTeleport(true);
        Location loc = new Location(to_world, to_x, to_y, to_z);
        endGateway.setExitLocation(loc);
        endGateway.setAge(-999999999);
        endGateway.update();
    }

    public static void destroy(String name) {
        Portal portal = ConfigManager.portalsConfig.portals().get(name);
        int portalx = portal.x();
        int portaly = portal.y();
        int portalz = portal.z();
        Block block = portal.world().getBlockAt(portalx, portaly, portalz);
        block.setType(Material.AIR);
        ConfigManager.portalsConfig.set(name, null, true);
    }
}

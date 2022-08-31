package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.utils.config.Portal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

import java.util.Map;
import java.util.function.Predicate;

public class PortalManager {

    public static void create(String name, int x, int y, int z, World world, int to_x, int to_y, int to_z, World to_world) {
        ActiveCraftCore.getInstance().getPortalsConfig().set(name,
                Map.of("x", x, "y", y, "z", z, "world", world.getName(), "to_x", to_x, "to_y", to_y, "to_z", to_z, "to_world", to_world.getName())
                , true);
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
        Portal portal = ActiveCraftCore.getInstance().getPortalsConfig().getPortals().get(name);
        int portalx = portal.x();
        int portaly = portal.y();
        int portalz = portal.z();
        Block block = portal.world().getBlockAt(portalx, portaly, portalz);
        block.setType(Material.AIR);
        ActiveCraftCore.getInstance().getPortalsConfig().set(name, null, true);
    }

    public static void clean() {
        ActiveCraftCore.getInstance().getPortalsConfig().getPortals().values().stream()
                .filter(Predicate.not(portal -> portal.world() != null && portal.to_world() != null && portal.world().getBlockAt(portal.x(), portal.y(), portal.z()).getType() == Material.END_GATEWAY))
                .forEach(portal -> ActiveCraftCore.getInstance().getPortalsConfig().set(portal.name(), null, true));
    }
}

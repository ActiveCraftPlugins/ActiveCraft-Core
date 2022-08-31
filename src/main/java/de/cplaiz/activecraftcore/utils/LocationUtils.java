package de.cplaiz.activecraftcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static String locationToString(Location location) {
        String loc = "";
        loc += location.getWorld().getName() + ";";
        loc += location.getX() + ";";
        loc += location.getY() + ";";
        loc += location.getZ() + ";";
        loc += location.getYaw() + ";";
        loc += location.getPitch();
        return loc;
    }

    public static Location locationFromString(String str) {
        String[] args = str.split(";");

        return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
                Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
    }

    public static void teleport(Player player, Location location) {
        player.teleport(location);
        player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.4f, 1.2f);
    }

    public static boolean isOutsideOfBorder(Player p) {
        return isOutsideOfBorder(p.getLocation());
    }

    public static boolean isOutsideOfBorder(Location location) {
        WorldBorder border = location.getWorld().getWorldBorder();
        Location center = border.getCenter();
        double size = border.getSize() / 2;
        double x = location.getX() - center.getX(), z = location.getZ() - center.getZ();
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
}

package org.activecraft.activecraftcore.playermanagement;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.config.FileConfig;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Playerlist extends HashMap<String, UUID> {

    @Getter
    private final FileConfig playerlistConfig = new FileConfig("playerlist.yml");

    public Playerlist() {
        load();
    }

    public void load() {
        playerlistConfig.reload();
        for (String key : playerlistConfig.getKeys(false)) {
            try {
                put(playerlistConfig.getString(key), UUID.fromString(key));
            } catch (IllegalArgumentException e) {
                ActiveCraftCore.getInstance().error("Error occured while loading UUID of \"" + key + "\"");
            }
        }
    }

    public void addPlayerIfAbsent(Player player) {
        Set<String> keys = playerlistConfig.getKeys(false);
        if (keys.contains(player.getUniqueId().toString())) return;
        playerlistConfig.set(player.getUniqueId().toString(), player.getName().toLowerCase());
        playerlistConfig.saveConfig();
        put(player.getName().toLowerCase(), player.getUniqueId());
    }

    public void updateIfChanged(Player player) {
        Profilev2 profile = Profilev2.of(player.getUniqueId());
        if (profile.getName().equals(player.getName())) return;
        profile.name = player.getName();
        playerlistConfig.set(player.getName(), null);
        playerlistConfig.set(player.getUniqueId().toString(), player.getName().toLowerCase());
        playerlistConfig.saveConfig();
        load();
    }

    public String getPlayernameByUUID(String uuid) {
        return playerlistConfig.getString(uuid);
    }

    public String getPlayernameByUUID(UUID uuid) {
        return getPlayernameByUUID(uuid.toString());
    }

    public UUID getUUIDByPlayername(String playername) {
        return get(playername.toLowerCase());
    }
}

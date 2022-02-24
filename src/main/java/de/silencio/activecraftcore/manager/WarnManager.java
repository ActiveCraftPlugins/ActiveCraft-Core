package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.events.PlayerWarnAddEvent;
import de.silencio.activecraftcore.events.PlayerWarnRemoveEvent;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import de.silencio.activecraftcore.utils.config.Warn;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WarnManager {

    private Profile profile;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public WarnManager(Profile profile) {
        this.profile = profile;
    }

    public void add() {
        add(CommandMessages.DEFAULT_WARN_REASON(), "unknown");
    }

    public void add(String reason) {
        add(reason, "unknown");
    }

    public void add(String reason, String source) {
        HashMap<String, Warn> warns = profile.getWarnList();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();

        // create new id
        Random random = new Random();
        String id = String.format("%06x", random.nextInt(0xffffff + 1));
        if (warns != null) {
            while (warns.containsKey(id))
                id = String.format("%06x", random.nextInt(0xffffff + 1));
            // add id to reason to avoid duplicates
            for (Warn w : warns.values()) {
                if (w.reason().equals(reason)) {
                    reason += " #" + id;
                    break;
                }
            }
        }

        //call event
        PlayerWarnAddEvent event = new PlayerWarnAddEvent(profile, new Warn(profile, reason, offsetDateTime.format(dtf), source, id));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        Warn warn = event.getWarn();

        profile.set(Profile.Value.WARNS, profile.getWarns() + 1);
        profile.set(Profile.Value.WARN_LIST, warn.id(), Map.of("reason", warn.reason(), "created", warn.created(), "source", warn.source()));
    }

    public void remove(Warn warn) {
        //call event
        PlayerWarnRemoveEvent event = new PlayerWarnRemoveEvent(profile, warn);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.set(Profile.Value.WARNS, profile.getWarns() - 1);
        profile.set(Profile.Value.WARN_LIST, warn.id(), null);
    }

    public Warn getWarnByReason(String reason) {
        return profile.getWarnList().values().stream().filter(warn -> warn.reason().equals(reason)).findFirst().orElse(null);
    }

    public Warn getWarnById(String id) {
        return profile.getWarnList().get(id);
    }

    public HashMap<String, Warn> getWarns() {
        return profile.getWarnList();
    }
}
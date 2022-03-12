package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.events.PlayerWarnAddEvent;
import de.silencio.activecraftcore.events.PlayerWarnRemoveEvent;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class WarnManager {

    private Player player;
    public String reason;
    public String source;
    public int id;
    public String created;
    private Profile profile;
    private FileConfig warnsConfig;
    private SimpleDateFormat sdf;

    public WarnManager(Player player) {
        this.player = player;
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        profile = ActiveCraftCore.getProfile(player);
        warnsConfig = new FileConfig("warns.yml");
    }

    public void add(String reason, String source) {

        OffsetDateTime offsetDateTime = OffsetDateTime.now();

        //call event
        PlayerWarnAddEvent event = new PlayerWarnAddEvent(profile, reason, new Date(), source);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        reason = reason.replace(".", "%dot%");

        int nextId = warnsConfig.getInt("next-id");
        List<String> warnsList = warnsConfig.getStringList(player.getName() + "." + "warn-list");

        if ((warnsConfig.getString(player.getName() + "." + reason + ".id") == null) || reason.equalsIgnoreCase("warn-list")) {
            if (!warnsList.contains(reason))
                warnsList.add(reason);
            warnsConfig.set(player.getName() + ".warn-list", warnsList);
            warnsConfig.set(player.getName() + "." + reason + ".created", offsetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            warnsConfig.set(player.getName() + "." + reason + ".source", source);
            warnsConfig.set(player.getName() + "." + reason + ".id", nextId);
        } else {
            if (!warnsList.contains(reason + "[" + nextId +"]"))
                warnsList.add(reason + "[" + nextId +"]");
            warnsConfig.set(player.getName() + ".warn-list", warnsList);
            warnsConfig.set(player.getName() + "." + reason + "[" + nextId +"]" + ".created", offsetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            warnsConfig.set(player.getName() + "." + reason + "[" + nextId +"]" + ".source", source);
            warnsConfig.set(player.getName() + "." + reason + "[" + nextId +"]" + ".id", nextId);
        }
        warnsConfig.set("next-id", nextId + 1);
        warnsConfig.saveConfig();

        profile.set(Profile.Value.WARNS, profile.getWarns()+1);
        player.sendMessage(CommandMessages.WARNED_HEADER() + "\n" +
                CommandMessages.WARNED(source, reason.replace("%dot%", "."))
        );
    }

    public void remove(String reason) {
        //call event
        PlayerWarnRemoveEvent event = new PlayerWarnRemoveEvent(profile, reason, new Date(), source);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        List<String> warnsList = warnsConfig.getStringList(player.getName() + "." + "warn-list");
        warnsList.remove(reason.replace(".", "%dot%"));

        warnsConfig.set(player.getName() + ".warn-list", warnsList);
        warnsConfig.set(player.getName() + "." + reason.replace(".", "%dot%"), null);
        warnsConfig.saveConfig();

        profile.set(Profile.Value.WARNS, profile.getWarns()-1);
        player.sendMessage(CommandMessages.WARNED_REMOVE(reason));
    }

    public WarnManager getWarnEntry(String reason) {
        reason = reason.replace("%dot%", ".");
        this.reason = reason;
        reason = reason.replace(".", "%dot%");
        created = warnsConfig.getString(player.getName() + "." + reason + ".created");
        source = warnsConfig.getString(player.getName() + "." + reason + ".source");
        id = warnsConfig.getInt(player.getName() + "." + reason + ".id");
        return this;
    }
}
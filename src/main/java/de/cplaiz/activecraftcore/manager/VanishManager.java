package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.PlayerVanishEvent;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class VanishManager {

    // TODO: 12.06.2022 TESTEN RICHTIG WICHTIG

    private static final Plugin plugin = ActiveCraftCore.getInstance();
    private static List<Player> vanished = new ArrayList<>();

    public static List<Player> getVanished() {
        return vanished;
    }

    public static boolean isVanished(Player player) {
        return vanished.contains(player);
    }

    public static void setVanished(Profilev2 profile, boolean hide, boolean silent) {
        setVanished(profile, hide, null, silent);
    }

    public static void setVanished(Profilev2 profile, boolean hide, CommandSender source, boolean silent) {
        Player target = profile.getPlayer();
        PlayerVanishEvent event = new PlayerVanishEvent(profile, hide);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        profile.setVanished(hide);
        MessageSupplier acCoreMessageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());
        String tagFormat = acCoreMessageSupplier.getMessage("command.vanish.tag", acCoreMessageSupplier.getColorScheme().secondary());
        if (hide) {
            profile.getDisplayManager().addTags(tagFormat);
            vanished.add(target);
        } else {
            profile.getDisplayManager().removeTags(tagFormat);
            vanished.remove(target);
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (target.equals(onlinePlayer)) continue;
            if (!onlinePlayer.hasPermission("activecraft.vanish.see")) {
                if (hide) {
                    onlinePlayer.hidePlayer(plugin, target);
                } else {
                    onlinePlayer.showPlayer(plugin, target);
                }
            }
        }

        if (silent) return;

        MessageFormatter messageFormatter = new MessageFormatter(acCoreMessageSupplier.getActiveCraftMessage())
                .addFormatterPattern("displayname", target.getDisplayName())
                .addFormatterPattern("playername", target.getName());
        if (target != source)
            target.sendMessage(acCoreMessageSupplier.getFormatted(
                    "command.vanish.now-" + (profile.isVanished() ? "in" : "") + "visible-self", messageFormatter));
        String joinFormat = acCoreMessageSupplier.getFormatted("general.join-format", messageFormatter);
        String quitFormat = acCoreMessageSupplier.getFormatted("general.quit-format", messageFormatter);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (target.equals(onlinePlayer)) continue;
            if (onlinePlayer.hasPermission("activecraft.vanish.see")) {
                onlinePlayer.sendMessage(acCoreMessageSupplier.getFormatted(
                        "command.vanish.now-" + (profile.isVanished() ? "in" : "") + "visible-others", messageFormatter));
            } else if (!StringUtils.anyEquals(profile.isVanished() ? quitFormat : joinFormat, "INVALID_STRING", "")){
                onlinePlayer.sendMessage(messageFormatter.format(profile.isVanished() ? quitFormat : joinFormat));
            }
        }
    }

    public static void hideAll(Player player) {
        vanished.forEach(player1 -> player.hidePlayer(plugin, player1));
    }

    public static void setVanishedList(List<Player> vanishedList) {
        vanished = vanishedList;
    }

}

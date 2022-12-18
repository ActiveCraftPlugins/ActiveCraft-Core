package org.activecraft.activecraftcore.manager;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.events.PlayerAfkEvent;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.events.PlayerAfkEvent;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class AfkManager {

    public static void setAfk(Player player, boolean afk) {
        Profilev2 profile = Profilev2.of(player);
        MessageSupplier messageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());

        //call event
        PlayerAfkEvent event = new PlayerAfkEvent(profile, afk);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        //set afk
        profile.setAfk(event.isAfk());
        String tagFormat = messageSupplier.getMessage("command.afk.tag", messageSupplier.getColorScheme().secondary());
        if (event.isAfk()) {
            profile.getDisplayManager().addTags(tagFormat);
        } else {
            profile.getDisplayManager().removeTags(tagFormat);
        }
        String msg = messageSupplier.getFormatted("command.afk." + (event.isAfk() ? "now-afk" : "not-afk"),
                new PlayerMessageFormatter(messageSupplier.getActiveCraftMessage(), profile));
        player.sendMessage(msg);
        if(ActiveCraftCore.getInstance().getMainConfig().isAnnounceAfk())
            Bukkit.getOnlinePlayers().stream()
                    .filter(Predicate.not(player::equals))
                    .forEach(p -> p.sendMessage((ChatColor.GRAY + ColorUtils.removeColorAndFormat(msg))));
    }
}

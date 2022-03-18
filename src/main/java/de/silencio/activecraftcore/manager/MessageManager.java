package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.events.MsgEvent;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.playermanagement.PlayerQueue;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageManager {

    private static final HashMap<CommandSender, Profile> msgPlayerStoring = new HashMap<>();

    public static void sendDM(Profile receiver, CommandSender sender, String message) {
        if (sender instanceof Player) {
            PlayerQueue.add(receiver, () -> {
                Player target = Bukkit.getPlayer(receiver.getName());
                MsgEvent event = new MsgEvent(sender, receiver, message);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                target.sendMessage(CommandMessages.MSG_PREFIX_FROM(sender, event.getMessage()));
                target.playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
                msgPlayerStoring.put(target, Profile.of(sender.getName()));
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.hasPermission("activecraft.msg.spy") && receiver.canReceiveSocialspy() && (player != sender && player != target))
                        .forEach(player -> player.sendMessage(CommandMessages.SOCIALSPY_PREFIX_TO(target, sender, message)));
                if (ConfigManager.getMainConfig().isSocialSpyToConsole())
                    Bukkit.getConsoleSender().sendMessage(CommandMessages.SOCIALSPY_PREFIX_TO(target, sender, event.getMessage()));
            });
        } else {
            PlayerQueue.add(receiver, () -> {
                Player target = Bukkit.getPlayer(receiver.getName());
                MsgEvent event = new MsgEvent(sender, receiver, message);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                target.sendMessage(CommandMessages.CONSOLE_MSG_PREFIX(event.getMessage()));
                target.playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
            });
        }
    }

    public static void reply(CommandSender replier, String message) {
        Profile profile = msgPlayerStoring.get(replier);
        if (profile == null) {
            replier.sendMessage(Errors.INVALID_PLAYER());
            return;
        }
        PlayerQueue.add(profile, () -> {
            Player target = Bukkit.getPlayer(profile.getName());
            MsgEvent event = new MsgEvent(replier, profile, message);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            target.sendMessage(CommandMessages.MSG_PREFIX_FROM(replier, event.getMessage()));
            target.playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("activecraft.msg.spy") && Profile.of(p).canReceiveSocialspy() && (p != replier && p != target))
                    .forEach(p -> p.sendMessage(CommandMessages.SOCIALSPY_PREFIX_TO(target, replier, message)));
            if (ConfigManager.getMainConfig().isSocialSpyToConsole())
                Bukkit.getConsoleSender().sendMessage(CommandMessages.SOCIALSPY_PREFIX_TO(target, replier, event.getMessage()));
        });
    }
}

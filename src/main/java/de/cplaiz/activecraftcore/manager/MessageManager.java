package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.MsgEvent;
import de.cplaiz.activecraftcore.exceptions.InvalidPlayerException;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.playermanagement.OfflinePlayerActionScheduler;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageManager {

    @Getter
    private static final HashMap<CommandSender, Profilev2> msgPlayerStoring = new HashMap<>();

    public static void sendDM(Profilev2 receiver, CommandSender sender, String message) {
        MessageSupplier acCoreMessageSupplier = receiver.getMessageSupplier(ActiveCraftCore.getInstance());
        MessageFormatter msgFormatter = new PlayerMessageFormatter(ActiveCraftCore.getInstance().getActiveCraftMessagev2())
                .setTarget(receiver).setSender(sender);
        MsgEvent event = new MsgEvent(sender, receiver, message);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        if (sender instanceof Player) {
            OfflinePlayerActionScheduler.schedule(receiver, (profile) -> {
                msgFormatter.addFormatterPattern("message", event.getMessage());
                Player target = receiver.getPlayer();
                target.sendMessage(acCoreMessageSupplier.getFormatted("command.msg.format-from", msgFormatter));
                target.playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
                msgPlayerStoring.put(target, Profilev2.of(sender.getName()));
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.hasPermission("activecraft.msg.spy") && receiver.canReceiveSocialspy() && (player != sender && player != target))
                        .forEach(player -> player.sendMessage(acCoreMessageSupplier.getFormatted("command.msg.socialspy-format", msgFormatter)));
                if (ActiveCraftCore.getInstance().getMainConfig().isSocialSpyToConsole())
                    Bukkit.getConsoleSender().sendMessage(acCoreMessageSupplier.getFormatted("command.msg.socialspy-format", msgFormatter));
                return null;
            });
        } else {
            OfflinePlayerActionScheduler.schedule(receiver, (profile) -> {
                Player target = receiver.getPlayer();
                msgFormatter.addFormatterPattern("message", event.getMessage());
                target.sendMessage(acCoreMessageSupplier.getFormatted("command.msg.from-console-format", msgFormatter));
                target.playSound(target.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
                return null;
            });
        }
    }

    public static void openConversation(CommandSender sender, Profilev2 target) {
        msgPlayerStoring.put(sender, target);
    }

    public static void msgToActiveConversation(CommandSender sender, String message) throws InvalidPlayerException {
        if (msgPlayerStoring.get(sender) == null)
            throw new InvalidPlayerException("No open conversation");
        sendDM(msgPlayerStoring.get(sender), sender, message);
    }
}

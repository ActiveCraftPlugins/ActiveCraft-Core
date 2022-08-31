package de.cplaiz.activecraftcore.listener;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.PlayerChatEvent;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.playermanagement.dialog.DialogScope;
import de.cplaiz.activecraftcore.playermanagement.dialog.DialogManager;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.config.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChatMessage(AsyncPlayerChatEvent event) {

        MainConfig mainConfig = ActiveCraftCore.getInstance().getMainConfig();
        String message = ColorUtils.replaceColorAndFormat(event.getMessage());
        Player player = event.getPlayer();
        Profilev2 profile = Profilev2.of(player);
        MessageSupplier acCoreMessageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());

        if (handleDialogs(profile, message))
            return;

        if (mainConfig.isLockChat() && player.hasPermission("activecraft.lockchat.bypass"))
            return;

        boolean muted = profile.isMuted();
        boolean defaultMuted = profile.isDefaultmuted();

        if (muted) {
            player.sendMessage(acCoreMessageSupplier.getMessage("chat.muted"));
            event.setCancelled(true);
        } else if (defaultMuted && mainConfig.isDefaultMuteEnabled()) {
            player.sendMessage(acCoreMessageSupplier.getMessage("chat.defaultmuted"));
            if (mainConfig.getDefaultMuteDuration() >= 0) {
                int timeUntilUnmute = mainConfig.getDefaultMuteDuration() - profile.getPlaytime();
                player.sendMessage(acCoreMessageSupplier.getFormatted("chat.defaultmuted",
                        new MessageFormatter(acCoreMessageSupplier.getActiveCraftMessage(), "time", "" + timeUntilUnmute)));
            }
            event.setCancelled(true);
        } else {
            // call event
            PlayerChatEvent acChatEvent = new PlayerChatEvent(profile, message, event.isAsynchronous());
            ;
            Bukkit.getPluginManager().callEvent(acChatEvent);
            if (acChatEvent.isCancelled()) return;
            if (!mainConfig.getUseCustomChatFormat().getValue())
                return;

            message = acChatEvent.getMessage();

            String format = acCoreMessageSupplier
                    .getFormatted("chat.chat-format",
                            new PlayerMessageFormatter(acCoreMessageSupplier.getActiveCraftMessage(), player)
                                    .addFormatterPattern("message", message, null)
                    );

            format = format.replace("%", "%%");
            event.setFormat(format);
        }
    }

    private boolean handleDialogs(Profilev2 profile, String message) {
        DialogManager dialogManager = profile.getDialogManager();
        DialogScope dialogScope = dialogManager.getActiveDialogScope();
        if (dialogScope == null)
            return false;
        dialogScope.answer(message);
        return true;
    }
}


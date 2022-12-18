package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.utils.TimeUtils;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.utils.TimeUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;

public class LoginListener implements Listener, ActiveCraftCore.MessageImpl {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        String ipAddress = e.getAddress().toString().replace("/", "");
        boolean ipBanned = false;
        BanList ipBanList = Bukkit.getBanList(BanList.Type.IP);
        MessageFormatter msgFormatter = new MessageFormatter();
        for (BanEntry banEntry : ipBanList.getBanEntries()) {
            if (banEntry.getTarget().equals(ipAddress)) {
                ipBanned = true;
                break;
            }
        }
        BanList nameBanList = Bukkit.getBanList(BanList.Type.NAME);
        if (e.getPlayer().isBanned()) {
            e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            BanEntry banEntry = nameBanList.getBanEntry(e.getPlayer().getName());
            assert banEntry != null;
            Date expirationDate = banEntry.getExpiration();
            String expirationString = TimeUtils.getRemainingAsString(expirationDate);
            msgFormatter.addReplacements("reason", banEntry.getReason(), "time", expirationString);
            e.disallow(e.getResult(), msg("command.banscreen.title", ChatColor.RED)
                    + "\n \n" + formatMsg("command.banscreen.expiration" + (expirationString.equals("never") ? "-permanent" : ""), msgFormatter)
                    + "\n" + formatMsg("command.banscreen.reason", msgFormatter));
        } else if (ipBanned) {
            e.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            BanEntry banEntry = ipBanList.getBanEntry(ipAddress);
            assert banEntry != null;
            Date expirationDate = banEntry.getExpiration();
            String expirationString = TimeUtils.getRemainingAsString(expirationDate);
            msgFormatter.addReplacements("reason", banEntry.getReason(), "time", expirationString);
            e.disallow(e.getResult(), msg("command.banscreen.ip-title", ChatColor.RED)
                    + "\n \n" + formatMsg("command.banscreen.expiration" + (expirationString.equals("never") ? "-permanent" : ""), msgFormatter)
                    + "\n" + formatMsg("command.banscreen.reason", msgFormatter));
        }
    }
}

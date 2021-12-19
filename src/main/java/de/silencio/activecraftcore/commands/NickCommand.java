package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.FileConfig;
import de.silencio.activecraftcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class NickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            if (Bukkit.getPlayer(args[0]) == null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (sender.hasPermission("activecraft.nick.self")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < args.length; i++) {
                            if (i != 0) stringBuilder.append(" ");
                            stringBuilder.append(args[i]);
                        }
                        String nickname = stringBuilder.toString();
                        nickname = ColorUtils.replaceColor(nickname);
                        nickname = ColorUtils.replaceFormat(nickname);
                        FileConfig playerdataConfig = new FileConfig("playerdata" + File.separator + player.getName().toLowerCase() + ".yml");

                        setDisplaynameFromConfig(player, playerdataConfig.getString("colornick"), nickname);
                        playerdataConfig.set("nickname", nickname);
                        playerdataConfig.saveConfig();
                        player.sendMessage(CommandMessages.NICK_SET(nickname));
                    }
                } else sender.sendMessage(Errors.NOT_A_PLAYER());

            } else {
                if (args.length > 1) {
                    if (sender.hasPermission("activecraft.nick.others")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            if (i != 1) stringBuilder.append(" ");
                            stringBuilder.append(args[i]);
                        }
                        String nickname = stringBuilder.toString();
                        nickname = ColorUtils.replaceColor(nickname);
                        nickname = ColorUtils.replaceFormat(nickname);

                        Player target = Bukkit.getPlayer(args[0]);
                        if(sender.getName().toLowerCase().equals(target.getName().toLowerCase())) {
                            if (!sender.hasPermission("activecraft.nick.self")) {
                                sender.sendMessage(Errors.CANNOT_TARGET_SELF());
                                return false;
                            }
                        }
                        FileConfig playerdataConfig = new FileConfig("playerdata" + File.separator + target.getName().toLowerCase() + ".yml");

                        //setDisplaycolorFromConfig(target, playerdataConfig.getString("colornick"));
                        playerdataConfig.set("nickname", nickname);
                        playerdataConfig.saveConfig();
                        sender.sendMessage(CommandMessages.NICK_SET_OTHERS(target, nickname));
                        target.sendMessage(CommandMessages.NICK_SET_OTHERS_MESSAGE(sender, nickname));
                        setDisplaynameFromConfig(target, playerdataConfig.getString("colornick"), nickname);
                    } else sender.sendMessage(Errors.NO_PERMISSION());
                } else {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (sender.hasPermission("activecraft.nick.self")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < args.length; i++) {
                                if (i != 0) stringBuilder.append(" ");
                                stringBuilder.append(args[i]);
                            }
                            String nickname = stringBuilder.toString();
                            nickname = ColorUtils.replaceColor(nickname);
                            nickname = ColorUtils.replaceFormat(nickname);
                            FileConfig playerdataConfig = new FileConfig("playerdata" + File.separator + player.getName().toLowerCase() + ".yml");

                            setDisplaynameFromConfig(player, playerdataConfig.getString("colornick"), nickname);
                            playerdataConfig.set("nickname", nickname);
                            playerdataConfig.saveConfig();
                            player.sendMessage(CommandMessages.NICK_SET(nickname));
                        }
                    } else sender.sendMessage(Errors.NOT_A_PLAYER());
                }
            }
        } else sender.sendMessage(Errors.INVALID_ARGUMENTS());
        return true;
    }

    public void setDisplaynameFromConfig(Player p, String colorname, String displayname) {
        for (ChatColor color : ChatColor.values()) {
            if (colorname.toLowerCase().equals(color.name().toLowerCase())) {
                if (!colorname.equals("BOLD") && !colorname.equals("MAGIC") && !colorname.equals("STRIKETHROUGH") &&
                        !colorname.equals("ITALIC") && !colorname.equals("UNDERLINE") && !colorname.equals("RESET")) {
                    p.setDisplayName(color + displayname);
                    p.setPlayerListName(color + displayname);
                }
            }
        }
    }
}
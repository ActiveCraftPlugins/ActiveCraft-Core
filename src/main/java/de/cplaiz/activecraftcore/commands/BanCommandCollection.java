package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.BanManager;
import de.cplaiz.activecraftcore.messages.MessageFormatter;
import de.cplaiz.activecraftcore.messages.Reasons;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import de.cplaiz.activecraftcore.utils.StringUtils;
import de.cplaiz.activecraftcore.utils.TimeUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BanCommandCollection extends ActiveCraftCommandCollection {

    public BanCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new BanCommand(plugin),
                new BanIpCommand(plugin),
                new UnbanCommand(plugin),
                new UnbanIpCommand(plugin),
                new BanlistCommand(plugin)
        );
    }

    public static class BanCommand extends ActiveCraftCommand {

        public BanCommand(ActiveCraftPlugin plugin) {
            super("ban", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            String target = getOfflinePlayer(args[0]).getName();
            messageFormatter.setTarget(target);
            if (BanManager.Name.isBanned(target)) {
                sendMessage(sender, this.rawCmdMsg("already-banned"), true);
                return;
            }
            BanManager.Name.ban(target, args.length >= 3 ? concatArray(args, 2) : Reasons.MODERATOR_BANNED(), TimeUtils.addFromStringToDate(args.length >= 2 ? args[1] : null), sender.getName());
            sendMessage(sender, this.cmdMsg("completed-message"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class UnbanCommand extends ActiveCraftCommand {

        public UnbanCommand(ActiveCraftPlugin plugin) {
            super("unban", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            if (!BanManager.Name.isBanned(args[0])) {
                sendMessage(sender, this.cmdMsg("not-banned"));
                return;
            }
            messageFormatter.setTarget(args[0]);
            BanManager.Name.unban(args[0]);
            sendMessage(sender, this.cmdMsg("unbanned-player"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 && BanManager.Name.getBans().isEmpty() ?
                    BanManager.Name.getBans().stream()
                            .map(BanEntry::getTarget)
                            .collect(Collectors.toList()) :
                    null;
        }
    }

    public static class BanIpCommand extends ActiveCraftCommand {

        public BanIpCommand(ActiveCraftPlugin plugin) {
            super("ban-ip", plugin, "banip", "banip");
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            if (Bukkit.getPlayer(args[0]) != null) {
                Player target = getPlayer(args[0]);
                String address = remove(target.getAddress().getAddress().toString(), "/");
                if (BanManager.IP.isBanned(address)) {
                    sendMessage(sender, this.cmdMsg("already-banned"));
                    return;
                }
                messageFormatter.addReplacement("ip", address);
                messageFormatter.setTarget(getProfile(target));
                BanManager.IP.ban(address, args.length >= 3 ? concatArray(args, 2) : Reasons.MODERATOR_BANNED(), TimeUtils.addFromStringToDate(args.length >= 2 ? args[1] : null), sender.getName());
                sendMessage(sender, this.cmdMsg("completed-message"));
            } else if (StringUtils.isValidInet4Address(args[0])) {
                if (BanManager.IP.isBanned(args[0])) {
                    sendMessage(sender, this.cmdMsg("already-banned"));
                    return;
                }
                messageFormatter.addReplacements("ip", args[0]);
                BanManager.IP.ban(args[0], args.length >= 3 ? concatArray(args, 2) : Reasons.MODERATOR_BANNED(), TimeUtils.addFromStringToDate(args.length >= 2 ? args[1] : null), sender.getName());
                sendMessage(sender, this.cmdMsg("completed-message-ip"));
            } else sendMessage(sender, this.rawCmdMsg("invalid-ip"), true);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class UnbanIpCommand extends ActiveCraftCommand {

        public UnbanIpCommand(ActiveCraftPlugin plugin) {
            super("unban-ip", plugin, "unbanip", "unbanip");
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            if (!BanManager.IP.isBanned(args[0])) {
                sendMessage(sender, this.rawCmdMsg("not-banned"), true);
                return;
            }
            messageFormatter.addReplacements("ip", args[0]);
            BanManager.IP.unban(args[0]);
            sendMessage(sender, this.cmdMsg("unbanned-ip"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 && BanManager.Name.getBans().isEmpty() ?
                    BanManager.IP.getBans().stream()
                            .map(BanEntry::getTarget)
                            .collect(Collectors.toList()) :
                    null;
        }
    }

    public static class BanlistCommand extends ActiveCraftCommand {

        public BanlistCommand(ActiveCraftPlugin plugin) {
            super("banlist", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            if (BanManager.Name.getBans().isEmpty() && BanManager.IP.getBans().isEmpty()) {
                sendMessage(sender, this.cmdMsg("no-bans"));
                return;
            }
            ComponentBuilder componentBuilder = new ComponentBuilder();
            List<String> tempBanListName = BanManager.Name.getBans().stream().map(BanEntry::getTarget).collect(Collectors.toList());
            List<String> tempBanListIP = BanManager.IP.getBans().stream().map(BanEntry::getTarget).collect(Collectors.toList());
            Collections.sort(tempBanListName);
            Collections.sort(tempBanListIP);

            for (int i = 0; i < tempBanListName.size(); i++) {
                String name = tempBanListName.get(i);
                TextComponent textComponent = new TextComponent();
                textComponent.setHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText(this.cmdMsg("unban-on-hover", new MessageFormatter().setTarget(name)))));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban " + name));
                if (i != 0) textComponent.setText(", " + name);
                else textComponent.setText(name);
                componentBuilder.append(textComponent);
            }
            for (int i = 0; i < tempBanListIP.size(); i++) {
                String ip = tempBanListIP.get(i);
                TextComponent textComponent = new TextComponent();
                textComponent.setHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                TextComponent.fromLegacyText(this.cmdMsg("unban-ip-on-hover", new MessageFormatter("ip", ip)))));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban-ip " + ip));
                if (i != 0) textComponent.setText(", " + ip);
                else textComponent.setText(ip);
                componentBuilder.append(textComponent);
            }

            sendMessage(sender, this.cmdMsg("header"));
            sendMessage(sender, componentBuilder.create());
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return null;
        }
    }
}
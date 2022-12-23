package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;


public class TpaCommandCollection extends ActiveCraftCommandCollection {

    public TpaCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new TpaCommand(plugin),
                new TpacceptCommand(plugin),
                new TpadenyCommand(plugin)
        );
    }

    // TODO: 11.06.2022 testen mit 2 person

    private static final HashMap<Player, Player> tpaList = new HashMap<>();
    private static final HashMap<Player, BukkitRunnable> tpaTimerList = new HashMap<>();

    public static class TpaCommand extends ActiveCraftCommand {

        public TpaCommand(ActiveCraftPlugin plugin) {
            super("tpa", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            Player player = getPlayer(sender);
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            Player target = getPlayer(args[0]);
            if (target == player) {
                sendMessage(sender, this.rawCmdMsg("cannot-tpa-self"), true);
                return;
            }

            TextComponent accept = new TextComponent(this.cmdMsg("accept") + " ");
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.cmdMsg("accept-hover", ChatColor.GREEN)).create()));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));

            TextComponent deny = new TextComponent(this.cmdMsg("deny"));
            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.cmdMsg("deny-hover", ChatColor.RED)).create()));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"));

            messageFormatter.setTarget(getProfile(target));
            sendMessage(sender, this.cmdMsg("request-to"));
            sendMessage(target, " ");
            sendMessage(target, this.cmdMsg("request-from"));
            sendMessage(target, accept, deny);
            sendMessage(target, " ");
            tpaList.put(target, player);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class TpacceptCommand extends ActiveCraftCommand {

        public TpacceptCommand(ActiveCraftPlugin plugin) {
            super("tpaccept", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            Player player = getPlayer(sender);
            checkPermission(sender);

            if (!tpaList.containsKey(player)) {
                sendMessage(sender, this.rawCmdMsg("no-requests"), true);
                return;
            }
            Player target = tpaList.get(player);
            messageFormatter.setTarget(getProfile(target));
            Location loc = player.getLocation();
            sendMessage(sender, this.cmdMsg("accepted"));
            if (!ActiveCraftCore.getInstance().getMainConfig().isTimerTpa()) {
                tpaList.get(player).sendActionBar(this.cmdMsg("actionbar"));
                tpaList.get(player).teleport(loc);
                tpaList.remove(player);
                return;
            }
            BukkitRunnable runnable = new BukkitRunnable() {
                int time = 3;

                @Override
                public void run() {
                    if (time == 0) {
                        target.sendActionBar(TpacceptCommand.this.cmdMsg("actionbar"));
                        target.teleport(loc);
                        sendMessage(target, TpacceptCommand.this.cmdMsg("receiver-message"));
                        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                        sendMessage(sender, TpacceptCommand.this.cmdMsg("sender-message"));
                        cancel();
                        tpaTimerList.put(tpaList.get(player), null);
                        tpaList.remove((Player) sender);
                        return;
                    }
                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                    target.sendActionBar(ChatColor.GOLD + "" + time);
                    time--;
                }
            };
            if (tpaTimerList.get(target) != null)
                tpaTimerList.get(target).cancel();
            tpaTimerList.put(tpaList.get(player), runnable);
            runnable.runTaskTimer(ActiveCraftCore.getInstance(), 0, 20);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }

    public static class TpadenyCommand extends ActiveCraftCommand {

        public TpadenyCommand(ActiveCraftPlugin plugin) {
            super("tpadeny", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            Player player = getPlayer(sender);
            checkPermission(sender);
            if (!tpaList.containsKey(player)) {
                sendMessage(sender, this.rawCmdMsg("no-requests"), true);
                return;
            }
            Player target = tpaList.get(player);
            messageFormatter.setTarget(getProfile(target));
            sendMessage(sender, this.cmdMsg("denied"));
            sendMessage(sender, this.cmdMsg("receiver-message"));
            sendMessage(target, this.cmdMsg("sender-message"));
            tpaList.remove((Player) sender);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }
}
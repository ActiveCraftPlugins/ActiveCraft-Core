package de.cplaiz.activecraftcore.commands;

import com.destroystokyo.paper.Title;
import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.messages.MessageFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RestartCommand extends ActiveCraftCommand {

    public RestartCommand(ActiveCraftPlugin plugin) {
        super("restart-server",  plugin);
    }

    private BukkitRunnable runnable;
    private int time;

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        if (args.length != 0 && args[0].equalsIgnoreCase("cancel")) {
            cancelTimer(sender);
            return;
        }
        time = args.length == 0 ? 30 : parseInt(args[0]);
        if (runnable != null && !runnable.isCancelled())
            runnable.cancel();

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(cmdMsg("message")));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spigot:restart");
                    return;
                }
                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0.5f);
                    Title title = new Title(cmdMsg("title", new MessageFormatter("time", time + "")));
                    target.sendTitle(title);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0.5f);
                }
                time--;
            }
        };
        runnable.runTaskTimer(ActiveCraftCore.getInstance(), 0, 20);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("cancel") : null;
    }

    private void cancelTimer(CommandSender sender) {
        if (runnable == null || runnable.isCancelled()) return;
        Title title = new Title(cmdMsg("title", new MessageFormatter("time", "--")));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(title));
        runnable.cancel();
        sendMessage(sender, cmdMsg("cancel"));
    }
}

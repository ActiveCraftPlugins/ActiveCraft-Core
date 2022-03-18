package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandStickCommand extends ActiveCraftCommand implements Listener {

    public CommandStickCommand() {
        super("commandstick");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "commandstick");
        Player player = getPlayer(sender);

        if (isValidCommand(args[0])) {
            ItemStack commandStick = new ItemStack(Material.STICK);
            ItemMeta commandStickMeta = commandStick.getItemMeta();
            commandStickMeta.setDisplayName(ChatColor.GOLD + "Command Stick");
            commandStickMeta.setUnbreakable(true);
            commandStickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            commandStickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Bound Command: /" + ChatColor.AQUA + combineArray(args, 0));
            commandStickMeta.setLore(lore);
            commandStick.setItemMeta(commandStickMeta);
            player.getInventory().addItem(commandStick);
        } else sendMessage(sender, Errors.INVALID_COMMAND());
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onCommandStickInteract(PlayerInteractEntityEvent event) {

        if (event.getRightClicked().getType() == EntityType.PLAYER) {
            Player player = event.getPlayer();
            Entity target = event.getRightClicked();
            ItemStack eventItem = player.getInventory().getItemInHand();
            if (eventItem == null) return;
            if (eventItem.getType() != Material.STICK) return;
            ItemMeta itemMeta = eventItem.getItemMeta();
            if (!(itemMeta.getDisplayName().equals("§6Command Stick"))) return;
            if (!(itemMeta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS) && itemMeta.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE)))
                return;
            if (!itemMeta.isUnbreakable()) return;
            if (itemMeta.lore() == null) return;
            for (String rawLore : itemMeta.getLore()) {
                String command;
                command = ColorUtils.removeColorAndFormat(rawLore);
                command = command.replace("Bound Command: /", "");
                command = command.replace("/", "");
                command = command.replace("@p", target.getName());

                player.performCommand(command);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommandStickInteract(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() == EntityType.PLAYER && event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            Player target = (Player) event.getEntity();
            ItemStack eventItem = player.getInventory().getItemInHand();
            if (eventItem == null) return;
            if (eventItem.getType() != Material.STICK) return;
            ItemMeta itemMeta = eventItem.getItemMeta();
            if (!(itemMeta.getDisplayName().equals("§6Command Stick"))) return;
            if (!(itemMeta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS) && itemMeta.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE)))
                return;
            if (!itemMeta.isUnbreakable()) return;
            if (itemMeta.lore() == null) return;
            for (String rawLore : itemMeta.getLore()) {
                String command;
                command = ColorUtils.removeColorAndFormat(rawLore);
                command = command.replace("Bound Command: /", "");
                command = command.replace("/", "");
                command = command.replace("@p", target.getName());

                player.performCommand(command);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommandStickInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack eventItem = event.getItem();
        if (event.getAction() == Action.PHYSICAL) return;
        if (eventItem == null) return;
        if (eventItem.getType() != Material.STICK) return;
        ItemMeta itemMeta = eventItem.getItemMeta();
        if (!(itemMeta.getDisplayName().equals("§6Command Stick"))) return;
        if (!(itemMeta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS) && itemMeta.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE)))
            return;
        if (!itemMeta.isUnbreakable()) return;
        if (itemMeta.lore() == null) return;
        for (String rawLore : itemMeta.getLore()) {
            String command;
            command = ColorUtils.removeColorAndFormat(rawLore);
            command = command.replace("Bound Command: /", "");
            command = command.replace("/", "");

            player.performCommand(command);
            event.setCancelled(true);
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        if (!(args.length == 1 && ConfigManager.getMainConfig().isHideCommandsAfterPluginName())) return null;
        List<String> pluginNames = new ArrayList<>(List.of("minecraft", "bukkit", "spigot", "paper"));
        Arrays.stream(Bukkit.getPluginManager().getPlugins()).forEach(plugin -> pluginNames.add(plugin.getName().toLowerCase()));
        return Bukkit.getCommandMap().getKnownCommands().keySet().stream()
                .filter(cmd -> pluginNames.stream().noneMatch(pluginName -> cmd.startsWith(pluginName + ":"))).collect(Collectors.toList());
    }
}
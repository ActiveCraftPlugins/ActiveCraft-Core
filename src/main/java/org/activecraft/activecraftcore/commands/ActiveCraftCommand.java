package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.*;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.messages.Errors;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.IntegerUtils;
import org.activecraft.activecraftcore.utils.StringUtils;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.*;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.messages.Errors;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.IntegerUtils;
import org.activecraft.activecraftcore.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter // TODO: 27.08.2022 migrate to accommandv2
public abstract class ActiveCraftCommand implements CommandExecutor, TabCompleter, ActiveCraftCore.MessageImpl {

    protected enum TargetType {
        SELF("self"),
        OTHERS("others");

        private final String code;

        TargetType(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    private final String commandName;
    private final String permission;
    //private final HashMap<CommandSender, Language> preferredLanguage = new HashMap<>();
    private final String messagePrefix;
    protected final MessageFormatter messageFormatter = new MessageFormatter();
    private final ActiveCraftPlugin plugin;
    private final ActiveCraftMessage activeCraftMessage;
    private @Getter static final HashMap<Class<? extends ActiveCraftException>, BiConsumer<ActiveCraftException, CommandSender>> exceptionList = new HashMap<>();

    private static final ActiveCraftMessage accMessage = ActiveCraftCore.getInstance().getActiveCraftMessage();

    static {
        registerErrorMessage(InvalidNumberException.class, Errors.INVALID_NUMBER());
        registerErrorMessage(InvalidNumberException.class, Errors.INVALID_NUMBER());
        registerErrorMessage(InvalidArgumentException.class, Errors.INVALID_ARGUMENTS());
        registerErrorMessage(InvalidColorException.class, Errors.INVALID_COLOR());
        registerErrorMessage(InvalidEntityException.class, Errors.INVALID_ENTITY());
        registerErrorEvent(InvalidHomeException.class, (exception, commandSender) -> {
            InvalidHomeException ihe = (InvalidHomeException) exception;
            commandSender.sendMessage(
                    accMessage.getFormatted("command.home." + (ihe.getProfile().getPlayer() == commandSender ? "self" : "others") + "-not-set",
                            new MessageFormatter(ihe.getProfile(),
                                    "home", ihe.getInvalidString())));
        });
        registerErrorMessage(InvalidPlayerException.class, Errors.INVALID_PLAYER());
        registerErrorMessage(InvalidWorldException.class, Errors.INVALID_WORLD());
        registerErrorMessage(NoPermissionException.class, Errors.NO_PERMISSION());
        registerErrorMessage(NoPlayerException.class, Errors.NOT_A_PLAYER());
        registerErrorEvent(NotHoldingItemException.class, (exception, commandSender) -> {
            commandSender.sendMessage(switch (((NotHoldingItemException) exception).getExpectedItem()) {
                case WRITTEN_BOOK -> Errors.WARNING() + ChatColor.GRAY + accMessage.getRawMessage("command.book.not-holding-book");
                case LEATHER_ITEM -> Errors.WARNING() + ChatColor.GRAY + accMessage.getRawMessage("command.leathercolor.no-leather-item");
                case ANY -> Errors.NOT_HOLDING_ITEM();
            });
        });
        registerErrorMessage(SelfTargetException.class, Errors.CANNOT_TARGET_SELF());
        registerErrorEvent(MaxHomesException.class, (exception, commandSender) -> commandSender.sendMessage(
                accMessage.getMessage("command.sethome.max-homes-" + (
                        commandSender == ((MaxHomesException) exception).getProfile().getPlayer() ? "self" : "others"))));
        registerErrorEvent(ModuleException.class, (exception, commandSender) -> {
            commandSender.sendMessage(
                    Errors.WARNING() + ChatColor.GRAY + accMessage.getRawMessage("command.acmodule." + switch (((ModuleException) exception).getErrorType()) {
                        case DOES_NOT_EXIST -> "not-found";
                        case NOT_INSTALLED -> "not-installed";
                        case ALREADY_INSTALLED -> "already-installed";
                        case NOT_LOADED -> "not-loaded";
                        case ALREADY_LOADED -> "already-loaded";
                        case NOT_ENABLED -> "not-enabled";
                        case ALREADY_ENABLED -> "already-enabled";
                    }));
        });
        registerErrorMessage(InvalidLanguageException.class, Errors.INVALID_LANGUAGE());
    }

    public ActiveCraftCommand(String command, ActiveCraftPlugin plugin) {
        this(command, plugin, command, command);
    }
    public ActiveCraftCommand(String command, ActiveCraftPlugin plugin, String permission) {
        this(command, plugin, permission, command);
    }

    public ActiveCraftCommand(String command, ActiveCraftPlugin plugin, String permission, String messagePrefix) {
        this.commandName = command;
        this.messagePrefix = messagePrefix;
        this.plugin = plugin;
        this.activeCraftMessage = plugin.getActiveCraftMessage();
        this.permission = permission;
    }

    public List<String> getAliases() {
        PluginCommand pluginCommand = getBukkitCommand();
        return pluginCommand != null ? pluginCommand.getAliases() : new ArrayList<>();
    }

    public PluginCommand getBukkitCommand() {
        return Bukkit.getPluginCommand(commandName);
    }

    protected static Player getPlayer(String input) throws InvalidPlayerException {
        if (Bukkit.getPlayer(input) == null) throw new InvalidPlayerException(input);
        return Bukkit.getPlayer(input);
    }

    protected static Player getPlayer(CommandSender sender) throws NoPlayerException {
        checkIsPlayer(sender);
        return (Player) sender;
    }

    protected static OfflinePlayer getOfflinePlayer(CommandSender sender) throws NoPlayerException {
        checkIsOfflinePlayer(sender);
        return (OfflinePlayer) sender;
    }

    protected static OfflinePlayer getOfflinePlayer(String input) throws InvalidPlayerException {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(input);
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + offlinePlayer.getUniqueId() + "/names");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if (http.getResponseCode() != 200)
                throw new InvalidPlayerException(input);
        } catch (IOException e) {
            throw new InvalidPlayerException(input);
        }
        return offlinePlayer;
    }

    protected static World getWorld(String input) throws InvalidWorldException {
        if (Bukkit.getWorld(input) == null) throw new InvalidWorldException(input);
        return Bukkit.getWorld(input);
    }

    protected static Profilev2 getProfile(String playername) throws InvalidPlayerException {
        if (Profilev2.of(playername) == null) throw new InvalidPlayerException(playername);
        return Profilev2.of(playername);
    }

    protected static Profilev2 getProfile(CommandSender sender) throws InvalidPlayerException {
        return getProfile(sender.getName());
    }

    protected static Profilev2 getProfile(Player player) throws InvalidPlayerException {
        return getProfile(player.getName());
    }

    protected static String concatArray(String[] args) {
        return concatArray(args, 0, args.length, " ");
    }

    protected static String concatArray(String[] args, int start) {
        return concatArray(args, start, args.length, " ");
    }

    protected static String concatArray(String[] args, int start, int stop) {
        return concatArray(args, start, stop, " ");
    }

    protected static String concatArray(String[] args, int start, String splitter) {
        return concatArray(args, start, args.length, splitter);
    }


    protected static String concatArray(String[] args, int start, int stop, String splitter) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = start; i < stop; i++) {
            if (i != start) resultBuilder.append(splitter);
            resultBuilder.append(args[i]);
        }
        return resultBuilder.toString();
    }

    protected static String concatCollection(Collection<String> args) {
        return concatList(args.stream().toList());
    }

    protected static String concatStream(Stream<String> args) {
        return concatList(args.toList());
    }

    protected static String concatCollection(Collection<String> args, String splitter) {
        return concatList(args.stream().toList(), splitter);
    }

    protected static String concatStream(Stream<String> args, String splitter) {
        return concatList(args.toList(), splitter);
    }

    protected static String concatList(List<String> args) {
        return concatList(args, 0, args.size(), " ");
    }

    protected static String concatList(List<String> args, String splitter) {
        return concatList(args, 0, args.size(), splitter);
    }

    protected static String concatList(List<String> args, int start) {
        return concatList(args, start, args.size(), " ");
    }

    protected static String concatList(List<String> args, int start, int stop) {
        return concatList(args, start, stop, " ");
    }

    protected static String concatList(List<String> args, int start, String splitter) {
        return concatList(args, start, args.size(), splitter);
    }

    protected static String concatList(List<String> args, int start, int stop, String splitter) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = start; i < stop; i++) {
            if (i != start) resultBuilder.append(splitter);
            resultBuilder.append(args.get(i));
        }
        return resultBuilder.toString();
    }

    protected static Material getMaterial(String inputString) throws InvalidArgumentException {
        if (Material.getMaterial(inputString.toUpperCase()) == null) throw new InvalidArgumentException();
        return Material.getMaterial(inputString.toUpperCase());
    }

    protected boolean isInt(String s) {
        if (s == null)
            return false;
        try {
            int d = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    protected static Integer parseInt(String numStr) throws InvalidNumberException {
        try {
            return Integer.valueOf(numStr);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException(numStr);
        }
    }

    protected static Long parseLong(String numStr) throws InvalidNumberException {
        try {
            return Long.valueOf(numStr);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException(numStr);
        }
    }

    protected static Double parseDouble(String numStr) throws InvalidNumberException {
        try {
            return Double.valueOf(numStr);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException(numStr);
        }
    }

    protected static Float parseFloat(String numStr) throws InvalidNumberException {
        try {
            return Float.valueOf(numStr);
        } catch (NumberFormatException e) {
            throw new InvalidNumberException(numStr);
        }
    }

    protected static EntityType parseEntityType(String mobName) throws InvalidEntityException {
        try {
            EntityType entityType = EntityType.valueOf(mobName.toUpperCase());
            if (entityType == EntityType.UNKNOWN) throw new InvalidEntityException(mobName);
            return entityType;
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityException(mobName);
        }
    }

    protected <T> T[] trimArray(T[] original, int trimFirst) {
        return Arrays.copyOfRange(original, trimFirst, original.length);
    }

    protected <T> T[] trimArray(T[] original, int trimFirst, int trimLast) {
        return Arrays.copyOfRange(original, trimFirst, original.length - trimFirst);
    }

    protected <T> List<T> toList(T[] original) {
        return Arrays.stream(original).toList();
    }
    protected <T> List<T> toList(Collection<T> original) {
        return original.stream().toList();
    }

    protected static boolean isValidCommand(String input) {
        return Bukkit.getCommandMap().getKnownCommands().keySet().stream().anyMatch(cmd -> remove(input, "/").equals(cmd));
    }

    protected boolean isTargetSelf(CommandSender sender, CommandSender target) throws SelfTargetException {
        return isTargetSelf(sender, target.getName());
    }

    protected boolean isTargetSelf(CommandSender sender, String target) throws SelfTargetException {
        return isTargetSelf(sender, target, false);
    }

    protected boolean isTargetSelf(CommandSender sender, CommandSender target, String extendedPermission) throws SelfTargetException {
        return isTargetSelf(sender, target.getName(), extendedPermission);
    }

    protected boolean isTargetSelf(CommandSender sender, String target, String extendedPermission) throws SelfTargetException {
        return isTargetSelf(sender, target, extendedPermission, false);
    }

    protected boolean isTargetSelf(CommandSender sender, CommandSender target, boolean ignorePermission) throws SelfTargetException {
        return isTargetSelf(sender, target.getName(), ignorePermission);
    }

    protected boolean isTargetSelf(CommandSender sender, String target, boolean ignorePermission) throws SelfTargetException {
        return isTargetSelf(sender, target, null, ignorePermission);
    }

    protected boolean isTargetSelf(CommandSender sender, CommandSender target, String extendedPermission, boolean ignorePermission) throws SelfTargetException {
        return isTargetSelf(sender, target.getName(), extendedPermission, ignorePermission);
    }

    protected boolean isTargetSelf(CommandSender sender, String target, String extendedPermission, boolean ignorePermission) throws SelfTargetException {
        if (sender.getName().equalsIgnoreCase(target)) {
            String permString = (plugin.permissionGroup != null ? plugin.permissionGroup + "." : "")
                    + permission + ".self" + (extendedPermission != null ? "." + extendedPermission : "");
            if (!ignorePermission && !sender.hasPermission(permString))
                throw new SelfTargetException(sender, "null");
            return true;
        }
        return false;
    }

    protected static void checkIsPlayer(CommandSender sender) throws NoPlayerException {
        if (!(sender instanceof Player)) throw new NoPlayerException(sender.getName());
    }

    protected static void checkIsOfflinePlayer(CommandSender sender) throws NoPlayerException {
        if (!(sender instanceof OfflinePlayer)) throw new NoPlayerException(sender.getName());
    }

    protected boolean checkRawPermission(Permissible permissible, String perm) throws NoPermissionException {
        if (!permissible.hasPermission(perm))
            throw new NoPermissionException(permissible, perm);
        return true;
    }

    protected boolean checkPermission(Permissible permissible) throws NoPermissionException {
        return checkPermission(permissible, null);
    }

    protected boolean checkPermission(Permissible permissible, String extendedPermission) throws NoPermissionException {
        return checkPermission(permissible, extendedPermission, plugin.permissionGroup);
    }

    protected boolean checkPermission(Permissible permissible, String permissionExtension, String permissionPrefix) throws NoPermissionException {
        String permString = (permissionPrefix != null ? permissionPrefix + "." : "")
                + permission
                + (permissionExtension != null ? "." + permissionExtension : "");
        if (!permissible.hasPermission(permString))
            throw new NoPermissionException(permissible, permissionExtension);
        return true;
    }

    protected static boolean checkHoldingItem(Player player, NotHoldingItemException.ExpectedItem expectedItem, Material... exceptedMaterial) throws NotHoldingItemException {
        if (expectedItem == NotHoldingItemException.ExpectedItem.ANY && player.getInventory().getItemInMainHand().getType() != Material.AIR)
            return true;
        if (!(Arrays.stream(exceptedMaterial).toList().contains(player.getInventory().getItemInMainHand().getType())))
            throw new NotHoldingItemException(player, expectedItem);
        return true;
    }

    protected static boolean checkHoldingItem(Player player, NotHoldingItemException.ExpectedItem expectedItem, Material exceptedMaterial) throws NotHoldingItemException {
        if (expectedItem == NotHoldingItemException.ExpectedItem.ANY && player.getInventory().getItemInMainHand().getType() != Material.AIR)
            return true;
        if (player.getInventory().getItemInMainHand().getType() != exceptedMaterial) {
            throw new NotHoldingItemException(player, expectedItem);
        }
        return true;
    }

    protected static boolean checkArgsLength(String[] args, ComparisonType compType, int i2) throws InvalidArgumentException {
        if (!IntegerUtils.compareInt(args.length, compType, i2)) throw new InvalidArgumentException();
        return true;
    }
    protected static Color getColor(String input) throws InvalidColorException {
        if (!input.startsWith("#")) {
            if (ColorUtils.bukkitColorFromString(input) == null) throw new InvalidColorException();
            return ColorUtils.bukkitColorFromString(input);
        } else {
            if (input.length() != 7) throw new InvalidColorException();
            if (!input.replace("#", "").toLowerCase().matches("(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])"))
                throw new InvalidColorException();
            int[] rgbArray = ColorUtils.getRGB(input);
            return Color.fromRGB(rgbArray[0], rgbArray[1], rgbArray[2]);
        }
    }

    public String cmdMsg(String input) {
        return cmdMsg(input, plugin, messageFormatter);
    }

    public String cmdMsg(String input, ActiveCraftPlugin plugin) {
        return cmdMsg(input, plugin, messageFormatter);
    }

    public String cmdMsg(String input, ChatColor color) {
        return cmdMsg(input, plugin, messageFormatter, color);
    }

    public String cmdMsg(String input, ActiveCraftPlugin plugin, ChatColor color) {
        return cmdMsg(input, plugin, messageFormatter, color);
    }

    public String cmdMsg(String input, MessageFormatter formatter) {
        return cmdMsg(input, plugin, formatter);
    }

    public String cmdMsg(String input, ActiveCraftPlugin plugin, MessageFormatter formatter) {
        return ActiveCraftMessage.getFormatted("command." + messagePrefix + "." + input, plugin, formatter);
    }

    public String cmdMsg(String input, MessageFormatter formatter, ChatColor color) {
        return cmdMsg(input, plugin, formatter, color);
    }

    public String cmdMsg(String input, ActiveCraftPlugin plugin, MessageFormatter formatter, ChatColor color) {
        return ActiveCraftMessage.getFormatted("command." + messagePrefix + "." + input, plugin, formatter, color);
    }

    public String rawCmdMsg(String input) {
        return rawCmdMsg(input, plugin);
    }

    public String rawCmdMsg(String input, ActiveCraftPlugin plugin) {
        return ActiveCraftMessage.getRawMessage("command." + messagePrefix + "." + input, plugin);
    }

    protected void sendMessage(CommandSender receiver, String message) {
        sendMessage(receiver, message, false);
    }

    protected void sendMessage(CommandSender receiver, String message, boolean isWarning) {
        receiver.sendMessage((isWarning ? Errors.WARNING() + ChatColor.GRAY : "") + message);
    }

    protected void sendMessage(CommandSender receiver, BaseComponent... message) {
        receiver.sendMessage(message);
    }

    protected void sendSilentMessage(CommandSender receiver, String message) {
        if (!ActiveCraftCore.getInstance().getMainConfig().isInSilentMode()) sendMessage(receiver, message);
    }

    protected void sendSilentMessage(CommandSender receiver, String message, boolean isWarning) {
        if (!ActiveCraftCore.getInstance().getMainConfig().isInSilentMode()) sendMessage(receiver, message, isWarning);
    }

    protected void sendSilentMessage(CommandSender receiver, BaseComponent... message) {
        if (!ActiveCraftCore.getInstance().getMainConfig().isInSilentMode()) sendMessage(receiver, message);
    }

    protected void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    protected void broadcast(String message, String permission) {
        Bukkit.broadcast(message, permission);
    }

    protected static ChatColor getChatColor(String name) throws InvalidColorException {
        if (ColorUtils.getColorByName(name) == null) throw new InvalidColorException();
        return ColorUtils.getColorByName(name);
    }

    protected static String remove(String target, String... replacements) {
        return StringUtils.remove(target, replacements);
    }

    protected abstract void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            messageFormatter.clearReplacements();
            messageFormatter.setSender(commandSender);
            Profilev2 senderProfilev2 = Profilev2.of(commandSender);
            //if (senderProfilev2 != null)
                //preferredLanguage.put(commandSender, senderProfilev2.getPreferredLanguage(plugin.getActiveCraftMessagev2()));
            runCommand(commandSender, command, s, strings);
        } catch (ActiveCraftException e) {
            exceptionList.keySet().stream()
                    .filter(exceptionclass -> exceptionclass.isInstance(e))
                    .findFirst()
                    .ifPresent(exceptionClass -> {
                        exceptionList.get(exceptionClass).accept(e, commandSender);
                    });
        }
        return false;
    }

    protected abstract List<String> onTab(CommandSender sender, Command command, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = onTab(sender, command, alias, args);
        return list != null ? list.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList()) : new ArrayList<>();
    }

    protected List<String> getBukkitPlayernames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    protected List<String> getProfileNames() {
        return ActiveCraftCore.getInstance().profiles.values().stream().map(Profilev2::getName).collect(Collectors.toList());
    }

    public static void registerErrorEvent(Class<? extends ActiveCraftException> exception, BiConsumer<ActiveCraftException, CommandSender> consumer) {
        exceptionList.put(exception, consumer);
    }

    private static void registerErrorMessage(Class<? extends ActiveCraftException> exceptionClass, String msg) {
        registerErrorEvent(exceptionClass, (e, sender) -> sender.sendMessage(msg));
    }
}

package org.activecraft.activecraftcore;

import org.activecraft.activecraftcore.commands.*;
import org.activecraft.activecraftcore.commands.*;
import org.activecraft.activecraftcore.guicreator.Gui;
import org.activecraft.activecraftcore.guicreator.GuiListener;
import org.activecraft.activecraftcore.guicreator.GuiManager;
import org.activecraft.activecraftcore.guis.offinvsee.OffInvSeeListener;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.guis.profilemenu.listener.ProfileMenuListeners;
import org.activecraft.activecraftcore.listener.*;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.messages.ActiveCraftMessageImpl;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.modules.Module;
import org.activecraft.activecraftcore.modules.ModuleManager;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.tables.Profiles;
import org.activecraft.activecraftcore.sql.SQLManager;
import org.activecraft.activecraftcore.utils.StringUtils;
import org.activecraft.activecraftcore.utils.config.*;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.playermanagement.OfflinePlayerActionScheduler;
import org.activecraft.activecraftcore.playermanagement.Playerlist;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.tables.Profiles;
import org.activecraft.activecraftcore.utils.config.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ToString
@Getter
public final class ActiveCraftCore extends ActiveCraftPlugin {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static ActiveCraftCore instance;
    private final HashMap<Player, ProfileMenu> profileMenuList = new HashMap<>();
    private final HashMap<UUID, Profilev2> profiles = new HashMap<>();
    private final GuiManager guiManager = new GuiManager();
    private MainConfig mainConfig;
    private LocationsConfig locationsConfig;
    private WarpsConfig warpsConfig;
    private PortalsConfig portalsConfig;
    private Playerlist playerlist;
    private final SQLManager sqlManager = new SQLManager();
    private final HashMap<Feature, Boolean> features = new HashMap<>();

    public ActiveCraftCore() {
        super(95488, 12627);
        instance = this;
    }

    public interface MessageImpl extends ActiveCraftMessageImpl {
        ActiveCraftMessage acm = instance.activeCraftMessage;

        default String msg(String key) {
            return msg(key, instance);
        }

        default String msg(String key, ChatColor color) {
            return msg(key, instance, color);
        }

        default String rawMsg(String key) {
            return rawMsg(key, instance);
        }

        default String formatMsg(String key, MessageFormatter formatter) {
            return formatMsg(key, instance, formatter);
        }

        default String formatMsg(String key, MessageFormatter formatter, ChatColor color) {
            return formatMsg(key, instance, formatter, color);
        }
    }

    @Override
    public void onPluginEnabled() {
        // connect to database
        sqlManager.init();

        // load warp perms
        loadWarpPermissions();

        // Playermanagement
        playerlist = new Playerlist();
        createProfiles();
        OfflinePlayerActionScheduler.initialize();

        // start playtime timer
        startTimer();

        // check for new plugin version
        checkForUpdate();

        bukkitLog(ChatColor.DARK_AQUA + "    ___   ______ ");
        bukkitLog(ChatColor.DARK_AQUA + "   /   | / ____/ ");
        bukkitLog(ChatColor.DARK_AQUA + "  / /| |/ /      " + ChatColor.GOLD + "ActiveCraft" + ChatColor.AQUA + " v" + getDescription().getVersion());
        bukkitLog(ChatColor.DARK_AQUA + " / ___ / /___    " + ChatColor.DARK_GRAY + "by CPlaiz and Silencio");
        bukkitLog(ChatColor.DARK_AQUA + "/_/  |_\\____/   ");
        List<Integer> moduleIds = ModuleManager.getModules().stream().map(Module::id).toList();
        String acmodulesString = StringUtils.combineList(
                getInstalledPlugins().stream()
                        .filter(plugin -> moduleIds.contains(plugin.spigotId))
                        .map(ActiveCraftPlugin::getName)
                        .toList(),
                ChatColor.DARK_AQUA + ", " + ChatColor.GOLD
        );
        if (!acmodulesString.isEmpty())
            bukkitLog(ChatColor.DARK_AQUA + "Installed Modules: " + ChatColor.GOLD + acmodulesString);
    }

    private void loadWarpPermissions() {
        for (String s : warpsConfig.getWarps().keySet()) {
            Map<String, Boolean> childMap = new HashMap<>();
            childMap.put("activecraft.warp.self", true);
            childMap.put("activecraft.warp", true);
            if (Bukkit.getPluginManager().getPermission("activecraft.warp.self." + s) != null)
                Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.self." + s, "Permission to warp yourself to a specific warp.", PermissionDefault.OP, childMap));
            childMap.clear();
            childMap.put("activecraft.warp.others", true);

            if (Bukkit.getPluginManager().getPermission("activecraft.warp.others." + s) != null)
                childMap.put("activecraft.warp", true);
            Bukkit.getPluginManager().addPermission(new Permission("activecraft.warp.others." + s, "Permission to warp another player to a specific warp.", PermissionDefault.OP, childMap));
        }
    }

    @Override
    public void onPluginDisabled() {
        Bukkit.getOnlinePlayers().forEach(player ->
                Profilev2.of(player).getLocationManager().setLastLocation(player.getWorld(), player.getLocation(), true)
        );
        log("Saving Data...");
        profiles.values().forEach(Profiles.INSTANCE::writeToDatabase);
        log("Data saved!");
        log("Plugin unloaded.");
    }

    public void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Profilev2 profile = Profilev2.of(player);

                assert profile != null;
                int playtime = profile.getPlaytime();
                playtime++;

                if (!profile.isAfk()) {
                    profile.setPlaytime(playtime);
                }

                if (mainConfig.getDefaultMuteDuration() >= 0 && playtime >= mainConfig.getDefaultMuteDuration()) {
                    if (profile.isDefaultmuted()) {
                        player.sendMessage(activeCraftMessage.getMessage("misc.default-mute-remove")); // TODO: 26.08.2022 zu msg supplier von profie ändern
                        profile.setDefaultmuted(false);
                    }
                }
            }
        }, 20 * 60, 20 * 60);
    }

    public void enableFeature(Feature feature) {
        features.put(feature, true);
        mainConfig.set("features." + feature.identifier(), true);
    }

    public void disableFeature(Feature feature) {
        features.put(feature, false);
        mainConfig.set("features." + feature.identifier(), false);
    }

    public boolean isFeatureEnabled(Feature feature) {
        return features.getOrDefault(feature, false);
    }

    public void createProfiles() {
        playerlist.load();
        profiles.clear();
        Profiles.INSTANCE.loadAllProfiles().forEach(profile -> profiles.put(profile.getUuid(), profile));
    }

    protected void registerConfigs() {
        saveDefaultConfig();
        ConfigManager.registerConfigs(this,
                mainConfig = new MainConfig(), portalsConfig = new PortalsConfig(),
                warpsConfig = new WarpsConfig(), locationsConfig = new LocationsConfig());
        File playerdataDir = new File(getDataFolder() + File.separator + "playerdata" + File.separator);
        if (!playerdataDir.exists())
            playerdataDir.mkdir();
    }

    protected void register() {
        // listeners
        pluginManager.addListeners(
                new JoinQuitListener(), new MessageListener(), new CommandListener(),
                new LockdownListener(), new SignListener(), new SignInteractListener(),
                new VanillaCommandListener(), new ServerPingListener(), new RespawnListener(),
                new DeathListener(), new LoginListener(), new BowCommand(this), new GamemodeChangeListener(),
                new TabCompleteListener(), new CommandStickCommand(this), new TeleportListener()
        );

        // gui creator
        pluginManager.addListeners(new GuiListener());

        // guis
        pluginManager.addListeners(new OffInvSeeListener(), new ProfileMenuListeners());

        Bukkit.getPluginCommand("atestcommand").setExecutor(new ATestCommand()); // TODO: 30.08.2022 remove test

        pluginManager.addCommands(
                new ACLanguageCommand(this),
                //new ACModulesCommand(this),
                //new ACReloadCommand(this),
                //new ACVersionCommand(this),
                //new BackCommand(this),
                new AfkCommand(this),
                //new BookCommand(this),
                //new BowCommand(this),
                //new BreakCommand(this),
                //new BroadCastCommand(this),
                //new ButcherCommand(this),
                //new ClearInvCommand(this),
                //new ColorNickCommand(this),
                //new CommandStickCommand(this),
                //new DrainCommand(this),
                //new EditSignCommand(this),
                //new EffectsCommand(this),
                //new EnchantCommand(this),
                //new EnderchestCommand(this),
                //new ExplodeCommand(this),
                //new FeedCommand(this),
                //new FireBallCommand(this),
                //new FireWorkCommand(this),
                //new FlyCommand(this),
                //new FlyspeedCommand(this),
                //new GodCommand(this),
                //new HatCommand(this),
                //new HealCommand(this),
                //new InvseeCommand(this),
                //new ItemCommand(this),
                //new KickAllCommand(this),
                //new KickCommand(this),
                //new KnockbackStickCommand(this),
                //new LanguageCommand(this),
                //new LastCoordsCommand(this),
                //new LastOnlineCommand(this),
                //new LeatherColorCommand(this),
                //new LockChatCommand(this),
                //new LogCommand(this),
                //new MoreCommand(this),
                //new MsgCommand(this),
                //new NickCommand(this),
                //new OffInvSeeCommand(this),
                //new OpItemsCommand(this),
                //new PingCommand(this),
                new PlayerlistCommand(this)
                //new PlayTimeCommand(this),
                //new PortalCommand(this),
                //new ProfileCommand(this),
                //new RamCommand(this),
                //new RandomTPCommand(this),
                //new RealNameCommand(this),
                //new RepairCommand(this),
                //new ReplyCommand(this),
                //new RestartCommand(this),
                //new SetspawnCommand(this),
                //new SkullCommand(this),
                //new SpawnCommand(this),
                //new SpawnerCommand(this),
                //new StaffChatCommand(this),
                //new StrikeCommand(this),
                //new SudoCommand(this),
                //new SuicideCommand(this),
                //new SummonCommand(this),
                //new TableMenuCommand(this),
                //new ToggleSocialSpyCommand(this),
                //new TopCommand(this),
                //new TpAllCommand(this),
                //new TpCommand(this),
                //new TphereCommand(this),
                //new VanishCommand(this),
                //new VerifyCommand(this),
                //new WalkspeedCommand(this),
                //new WarnCommand(this),
                //new WeatherCommand(this),
                //new WhereAmICommand(this),
                //new WhoIsCommand(this),
                //new XpCommand(this)
        );

        // commands
        pluginManager.addCommands(
                //new ACLanguageCommand(this),
                new ACModulesCommand(this),
                new ACReloadCommand(this),
                new ACVersionCommand(this),
                new BackCommand(this),
                //new AfkCommand(this),
                new BookCommand(this),
                new BowCommand(this),
                new BreakCommand(this),
                new BroadCastCommand(this),
                new ButcherCommand(this),
                new ClearInvCommand(this),
                new ColorNickCommand(this),
                new CommandStickCommand(this),
                new DrainCommand(this),
                new EditSignCommand(this),
                new EffectsCommand(this),
                new EnchantCommand(this),
                new EnderchestCommand(this),
                new ExplodeCommand(this),
                new FeedCommand(this),
                new FireBallCommand(this),
                new FireWorkCommand(this),
                new FlyCommand(this),
                new FlyspeedCommand(this),
                new GodCommand(this),
                new HatCommand(this),
                new HealCommand(this),
                new InvseeCommand(this),
                new ItemCommand(this),
                new KickAllCommand(this),
                new KickCommand(this),
                new KnockbackStickCommand(this),
                new LanguageCommand(this),
                new LastCoordsCommand(this),
                new LastOnlineCommand(this),
                new LeatherColorCommand(this),
                new LockChatCommand(this),
                new LogCommand(this),
                new MoreCommand(this),
                new MsgCommand(this),
                new NickCommand(this),
                new OffInvSeeCommand(this),
                new OpItemsCommand(this),
                new PingCommand(this),
                //new PlayerlistCommand(this),
                new PlayTimeCommand(this),
                new PortalCommand(this),
                new ProfileCommand(this),
                new RamCommand(this),
                new RandomTPCommand(this),
                new RealNameCommand(this),
                new RepairCommand(this),
                new ReplyCommand(this),
                new RestartCommand(this),
                new SetspawnCommand(this),
                new SkullCommand(this),
                new SpawnCommand(this),
                new SpawnerCommand(this),
                new StaffChatCommand(this),
                new StrikeCommand(this),
                new SudoCommand(this),
                new SuicideCommand(this),
                new SummonCommand(this),
                new TableMenuCommand(this),
                new ToggleSocialSpyCommand(this),
                new TopCommand(this),
                new TpAllCommand(this),
                new TpCommand(this),
                new TphereCommand(this),
                new VanishCommand(this),
                new VerifyCommand(this),
                new WalkspeedCommand(this),
                new WarnCommand(this),
                new WeatherCommand(this),
                new WhereAmICommand(this),
                new WhoIsCommand(this),
                new XpCommand(this)
        );

        pluginManager.addCommandCollections(
                new BanCommandCollection(this),
                new GamemodeCommandCollection(this),
                new HomeCommandCollection(this),
                new LockdownCommandCollection(this),
                new MuteCommandCollection(this),
                new OfflineTpCommandCollection(this),
                new TableCommandCollection(this),
                new TpaCommandCollection(this),
                new WarpCommandCollection(this)
        );
    }

    public static ActiveCraftCore getInstance() {
        return instance;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public Playerlist getPlayerlist() {
        return playerlist;
    }

    public HashMap<UUID, Profilev2> getProfiles() {
        return profiles;
    }
}

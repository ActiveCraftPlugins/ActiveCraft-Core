package de.silencio.activecraftcore.playermanagement;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.manager.HomeManager;
import de.silencio.activecraftcore.manager.WarnManager;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.StringUtils;
import de.silencio.activecraftcore.utils.config.Effect;
import de.silencio.activecraftcore.utils.config.FileConfig;
import de.silencio.activecraftcore.utils.config.Warn;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

@Getter
public final class Profile {

    public enum Value {
        NAME,
        NICKNAME,
        LAST_ONLINE,
        UUID,
        COLOR_NICK,
        FLYSPEED,
        TIMES_JOINED,
        WARNS,
        MUTES,
        BANS,
        IP_BANS,
        PLAYTIME_MINUTES,
        PLAYTIME_HOURS,
        AFK,
        OP,
        WHITELISTED,
        GODMODE,
        FLY,
        MUTED,
        DEFAULTMUTED,
        VANISHED,
        LOG_ENABLED,
        BYPASS_LOCKDOWN,
        EDIT_SIGN,
        HOME_LIST,
        WARN_LIST,
        RECEIVE_SOCIALSPY,
        LAST_LOCATION,
        EFFECTS,
        FORCE_MUTED;

        Value() {
        }
    }


    private FileConfig playerdataConfig;

    private String name;
    private String nickname;
    private String prefix;
    private String lastOnline;
    private UUID uuid;
    private ChatColor colorNick;
    private float flyspeed;
    private int times_joined;
    private int warns;
    private int mutes;
    private int bans;
    private int ipBans;
    private int playtimeMinutes;
    private int playtimeHours;
    private boolean afk;
    private boolean op;
    private boolean whitelisted;
    private boolean godmode;
    private boolean fly;
    private boolean muted;
    private boolean forcemuted;
    private boolean defaultmuted;
    private boolean vanished;
    private boolean logEnabled;
    private @Getter(AccessLevel.NONE)
    boolean bypassLockdown;
    private @Getter(AccessLevel.NONE)
    boolean receiveSocialspy;
    private boolean editSign;
    private List<String> tags;

    private HashMap<String, Location> homeList;
    private HashMap<String, Warn> warnList;
    private HashMap<String, Location> lastLocations;
    private HashMap<PotionEffectType, Effect> effects;

    private final WarnManager warnManager;
    private final HomeManager homeManager;

    public Profile(Player player) {
        this(player.getName());
    }

    public Profile(String playername) {
        name = playername;
        warnManager = new WarnManager(this);
        homeManager = new HomeManager(this);
        refresh();
    }

    public void refresh() {
        this.playerdataConfig = new FileConfig("playerdata" + File.separator + ActiveCraftCore.getUUIDByPlayername(name) + ".yml");
        loadFromConfig(playerdataConfig);
    }

    public static Profile of(String profileName) {
        Profile profile = ActiveCraftCore.getProfiles().get(profileName.toLowerCase());
        if (profile != null) profile.refresh();
        return profile;
    }

    public static Profile of(UUID uuid) {
        return of(ActiveCraftCore.getPlayernameByUUID(uuid));
    }

    public static Profile of(Player player) {
        return of(player.getName());
    }

    public static Profile of(CommandSender sender) {
        return of(sender.getName());
    }

    private void loadFromConfig(FileConfig fileConfig) {
        name = fileConfig.getString("name", "%INVALID_NAME%");
        nickname = fileConfig.getString("nickname", "%INVALID_NAME%");
        lastOnline = fileConfig.getString("last-online", "1/1/1970, 00:00:00");
        uuid = UUID.fromString(fileConfig.getString("uuid", ""));
        colorNick = ChatColor.valueOf(fileConfig.getString("colornick", "WHITE"));
        flyspeed = (float) fileConfig.getDouble("flyspeed");
        warns = fileConfig.getInt("violations.warns");
        mutes = fileConfig.getInt("violations.mutes");
        bans = fileConfig.getInt("violations.bans");
        ipBans = fileConfig.getInt("violations.ip-bans");
        times_joined = fileConfig.getInt("times-joined");
        afk = fileConfig.getBoolean("afk");
        op = fileConfig.getBoolean("op");
        whitelisted = fileConfig.getBoolean("whitelisted");
        godmode = fileConfig.getBoolean("godmode");
        fly = fileConfig.getBoolean("fly");
        muted = fileConfig.getBoolean("muted");
        forcemuted = fileConfig.getBoolean("forcemuted");
        defaultmuted = fileConfig.getBoolean("default-mute");
        vanished = fileConfig.getBoolean("vanished");
        logEnabled = fileConfig.getBoolean("log-enabled");
        bypassLockdown = fileConfig.getBoolean("lockdown-bypass");
        editSign = fileConfig.getBoolean("edit-sign");
        tags = fileConfig.getStringList("tags");
        prefix = fileConfig.getString("prefix", "");
        receiveSocialspy = fileConfig.getBoolean("receive-socialspy");
        playtimeMinutes = fileConfig.getInt("playtime.minutes");
        playtimeHours = fileConfig.getInt("playtime.hours");

        lastLocations = new HashMap<>();
        Bukkit.getWorlds().forEach(world -> lastLocations.put(world.getName(), fileConfig.getLocation("last-location." + world.getName())));

        homeList = new HashMap<>();
        ConfigurationSection homesSection = playerdataConfig.getConfigurationSection("homes");
        if (homesSection != null)
            homesSection.getKeys(false).forEach(homeName -> homeList.put(homeName, homesSection.getLocation(homeName)));

        warnList = new HashMap<>();
        ConfigurationSection warnsSection = playerdataConfig.getConfigurationSection("warns");
        if (warnsSection != null)
            warnsSection.getKeys(false).forEach(warnId -> warnList.put(warnId, new Warn(
                    warnsSection.getString(warnId + ".reason"),
                    warnsSection.getString(warnId + ".created"),
                    warnsSection.getString(warnId + ".source"),
                    warnId
            )));

        effects = new HashMap<>();
        Arrays.stream(PotionEffectType.values()).forEach(effect -> effects.put(effect, new Effect(
                effect,
                playerdataConfig.getInt("effects." + effect.getName().toLowerCase() + ".amp"),
                playerdataConfig.getBoolean("effects." + effect.getName().toLowerCase() + ".active")
        )));
    }

    public void set(Value value, Object object) {
        switch (value) {
            case NAME -> playerdataConfig.set("name", object);
            case NICKNAME -> playerdataConfig.set("nickname", object);
            case LAST_ONLINE -> playerdataConfig.set("last-online", object);
            case UUID -> playerdataConfig.set("uuid", object);
            case COLOR_NICK -> playerdataConfig.set("colornick", object);
            case FLYSPEED -> playerdataConfig.set("flyspeed", object);
            case TIMES_JOINED -> playerdataConfig.set("times_joined", object);
            case WARNS -> playerdataConfig.set("violations.warns", object);
            case MUTES -> playerdataConfig.set("violations.mutes", object);
            case BANS -> playerdataConfig.set("violations.bans", object);
            case IP_BANS -> playerdataConfig.set("violations.ip-bans", object);
            case PLAYTIME_MINUTES -> playerdataConfig.set("playtime.minutes", object);
            case PLAYTIME_HOURS -> playerdataConfig.set("playtime.hours", object);
            case AFK -> playerdataConfig.set("afk", object);
            case OP -> playerdataConfig.set("op", object);
            case WHITELISTED -> playerdataConfig.set("whitelisted", object);
            case GODMODE -> playerdataConfig.set("godmode", object);
            case FLY -> playerdataConfig.set("fly", object);
            case MUTED -> playerdataConfig.set("muted", object);
            case DEFAULTMUTED -> playerdataConfig.set("default-mute", object);
            case VANISHED -> playerdataConfig.set("vanished", object);
            case LOG_ENABLED -> playerdataConfig.set("log-enabled", object);
            case BYPASS_LOCKDOWN -> playerdataConfig.set("lockdown-bypass", object);
            case EDIT_SIGN -> playerdataConfig.set("edit-sign", object);
            case FORCE_MUTED -> playerdataConfig.set("forcemuted", object);
            case RECEIVE_SOCIALSPY -> playerdataConfig.set("receive-socialspy", object);
            case HOME_LIST -> playerdataConfig.set("homes", object);
        }
        playerdataConfig.saveConfig();
        refresh();
    }

    public void set(Value value, String deepPath, Object object) {
        switch (value) {
            case EFFECTS -> playerdataConfig.set("effects." + deepPath, object);
            case LAST_LOCATION -> playerdataConfig.set("last-location." + deepPath, object);
            case WARN_LIST -> playerdataConfig.set("warns." + deepPath, object);
        }
        playerdataConfig.saveConfig();
        refresh();
    }

    public void refreshEffects() {
        Player player;
        if ((player = getPlayer()) == null) return;
        for (Effect effect : effects.values()) {
            if (effect != null) {
                if (effect.active()) {
                    player.addPotionEffect(new PotionEffect(effect.effectType(), 2147483647, effect.amplifier()));
                }
            }
        }
    }

    public void toggleEffect(PotionEffectType effectType) {
        Effect effect = effects.get(effectType);
        if (effect == null) {
            set(Profile.Value.EFFECTS, effectType.getName().toLowerCase(), Map.of("amp", 1, "active", true));
        } else {
            set(Profile.Value.EFFECTS, effectType.getName().toLowerCase(), Map.of("amp", effect.amplifier(), "active", !effect.active()));
        }
        Player player;
        if ((player = getPlayer()) == null) return;
        effect = effects.get(effectType);
        if (effect.active()) {
            player.addPotionEffect(new PotionEffect(effectType, 2147483647, effect.amplifier()));
        } else {
            player.removePotionEffect(effectType);
        }
    }

    public void changeEffectLevel(PotionEffectType effectType, int change) {
        Effect effect = effects.get(effectType);
        if (effect == null) {
            set(Profile.Value.EFFECTS, effectType.getName().toLowerCase(), Map.of("amp", change < 0 ? 1 : change, "active", true));
        } else {
            int amp = effect.amplifier() + change;
            amp = 0 < amp && amp < 256 ? amp : (amp >= 256 ? 255 : 0);
            set(Profile.Value.EFFECTS, effectType.getName().toLowerCase(), Map.of("amp", amp, "active", effect.active()));
        }
        Player player;
        effect = effects.get(effectType);
        if ((player = getPlayer()) == null || !effect.active()) return;
        player.removePotionEffect(effectType);
        player.addPotionEffect(new PotionEffect(effectType, 2147483647, effect.amplifier()));
    }

    public void clearTags() {
        tags.clear();
        playerdataConfig.set("tags", this.tags);
        playerdataConfig.saveConfig();
        updateDisplayname();
    }

    public void addTag(String... tags) {
        for (String tag : tags)
            if (!this.tags.contains(tag)) this.tags.add(tag);
        playerdataConfig.set("tags", this.tags);
        playerdataConfig.saveConfig();
        updateDisplayname();
    }

    public void removeTag(String... tags) {
        for (String tag : tags)
            this.tags.remove(tag);
        playerdataConfig.set("tags", this.tags);
        playerdataConfig.saveConfig();
        updateDisplayname();
    }

    public void setTags(String... tags) {
        this.tags.clear();
        addTag(tags);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        playerdataConfig.set("prefix", prefix);
        playerdataConfig.saveConfig();
        updateDisplayname();
    }

    public void clearPrefix() {
        setPrefix("");
    }

    public void updateDisplayname() {
        Player player = Bukkit.getPlayer(name);
        if (player == null) return;
        List<String> appliedTags = tags;
        Collections.sort(appliedTags);
        Collections.reverse(appliedTags);
        if ((player = getPlayer()) == null) return;
        if (!Arrays.stream(ColorUtils.getColorsOnly()).toList().contains(colorNick)) return;
        String displayedPrefix = prefix == null ? "" : prefix.strip() + (prefix.strip().equals("") ? "" : " ");
        String displayname = nickname + (tags.size() > 0 ? " " : "") + StringUtils.combineList(appliedTags);
        player.setDisplayName(displayedPrefix + colorNick + displayname);
        player.setPlayerListName(displayedPrefix + colorNick + displayname);
    }

    public String getRawNickname() {
        return nickname;
    }

    public boolean canBypassLockdown() {
        return bypassLockdown;
    }

    public boolean canReceiveSocialspy() {
        return receiveSocialspy;
    }

    public Location getLastLocation(String world) {
        return playerdataConfig.getLocation("last-location." + world);
    }

    public Location getLastLocationBeforeQuit() {
        return playerdataConfig.getLocation("last-location.BEFORE_QUIT");
    }

    public String getNickname() {
        if (prefix == null) prefix = "";
        prefix = prefix.strip() + (prefix.strip().equals("") ? "" : " ");
        return prefix + colorNick + nickname;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }
}

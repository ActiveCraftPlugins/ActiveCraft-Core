package de.silencio.activecraftcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Profile {

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
        ON_DUTY,
        LOG_ENABLED,
        BYPASS_LOCKDOWN,
        EDIT_SIGN,
        KNOWN_IPS;

        Value() {

        }
    }

    private Player owner;
    private FileConfig playerdataConfig;
    private FileConfig playtimeConfig;
    private String name;
    private String nickname;
    private String last_online;
    private UUID uuid;
    private ChatColor color_nick;
    private float flyspeed;
    private int times_joined;
    private int warns;
    private int mutes;
    private int bans;
    private int ip_bans;
    private int playtime_minutes;
    private int playtime_hours;
    private boolean afk;
    private boolean op;
    private boolean whitelisted;
    private boolean godmode;
    private boolean fly;
    private boolean muted;
    private boolean defaultmuted;
    private boolean vanished;
    private boolean on_duty;
    private boolean log_enabled;
    private boolean bypass_lockdown;
    private boolean edit_sign;
    private List<String> known_ips;

    public Profile(Player player) {
        this.owner = player;
        this.playerdataConfig = new FileConfig("playerdata" + File.separator + player.getName().toLowerCase() + ".yml");
        this.playtimeConfig = new FileConfig("playtime.yml");
        loadFromConfig(playerdataConfig);

    }

    public void reload() {
        this.playerdataConfig = new FileConfig("playerdata" + File.separator + owner.getName().toLowerCase() + ".yml");
        this.playtimeConfig = new FileConfig("playtime.yml");
        loadFromConfig(playerdataConfig);
    }

    private void loadFromConfig(FileConfig fileConfig) {
        name = fileConfig.getString("name");
        nickname = fileConfig.getString("nickname");
        last_online = fileConfig.getString("last-online");
        uuid = UUID.fromString(fileConfig.getString("uuid"));
        color_nick = ChatColor.valueOf(fileConfig.getString("colornick"));
        flyspeed = (float) fileConfig.getDouble("flyspeed");
        warns = fileConfig.getInt("violations.warns");
        mutes = fileConfig.getInt("violations.mutes");
        bans = fileConfig.getInt("violations.bans");
        ip_bans = fileConfig.getInt("violations.ip-bans");
        times_joined = fileConfig.getInt("times-joined");
        afk = fileConfig.getBoolean("afk");
        op = fileConfig.getBoolean("op");
        whitelisted = fileConfig.getBoolean("whitelisted");
        godmode = fileConfig.getBoolean("godmode");
        fly = fileConfig.getBoolean("fly");
        muted = fileConfig.getBoolean("muted");
        defaultmuted = fileConfig.getBoolean("default-mute");
        vanished = fileConfig.getBoolean("vanished");
        on_duty = fileConfig.getBoolean("on-duty");
        log_enabled = fileConfig.getBoolean("log-enabled");
        bypass_lockdown = fileConfig.getBoolean("lockdown-bypass");
        edit_sign = fileConfig.getBoolean("edit-sign");
        known_ips = fileConfig.getStringList("known-ips");

        playtime_minutes = playtimeConfig.getInt(owner.getName() + ".minutes");
        playtime_hours = playtimeConfig.getInt(owner.getName() + ".hours");

    }

    public void set(Value value, Object object) {
        switch (value) {
            case NAME:
                playerdataConfig.set("name", object);
                break;

            case NICKNAME:
                playerdataConfig.set("nickname", object);
                break;

            case LAST_ONLINE:
                playerdataConfig.set("last-online", object);
                break;

            case UUID:
                playerdataConfig.set("uuid", object);
                break;

            case COLOR_NICK:
                playerdataConfig.set("colornick", object);
                break;

            case FLYSPEED:
                playerdataConfig.set("flyspeed", object);
                break;

            case TIMES_JOINED:
                playerdataConfig.set("times_joined", object);
                break;

            case WARNS:
                playerdataConfig.set("violations.warns", object);
                break;

            case MUTES:
                playerdataConfig.set("violations.mutes", object);
                break;

            case BANS:
                playerdataConfig.set("violations.bans", object);
                break;

            case IP_BANS:
                playerdataConfig.set("violations.ip-bans", object);
                break;

            case PLAYTIME_MINUTES:
                playtimeConfig.set(owner.getName() + ".minutes", object);
                break;

            case PLAYTIME_HOURS:
                playtimeConfig.set(owner.getName() + ".hours", object);
                break;

            case AFK:
                playerdataConfig.set("afk", object);
                break;

            case OP:
                playerdataConfig.set("op", object);
                break;

            case WHITELISTED:
                playerdataConfig.set("whitelisted", object);
                break;

            case GODMODE:
                playerdataConfig.set("godmode", object);
                break;

            case FLY:
                playerdataConfig.set("fly", object);
                break;

            case MUTED:
                playerdataConfig.set("muted", object);
                break;

            case DEFAULTMUTED:
                playerdataConfig.set("default-mute", object);
                break;

            case VANISHED:
                playerdataConfig.set("vanished", object);
                break;

            case ON_DUTY:
                playerdataConfig.set("on-duty", object);
                break;

            case LOG_ENABLED:
                playerdataConfig.set("log-enabled", object);
                break;

            case BYPASS_LOCKDOWN:
                playerdataConfig.set("lockdown-bypass", object);
                break;

            case EDIT_SIGN:
                playerdataConfig.set("edit-sign", object);
                break;

            case KNOWN_IPS:
                playerdataConfig.set("known-ips", object);
                break;
        }
        playerdataConfig.saveConfig();
        playtimeConfig.saveConfig();
    }

    public Player getProfileOwner() {
        return owner;
    }

    public FileConfig getPlayerdataConfig() {
        return playerdataConfig;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLastOnline() {
        return last_online;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ChatColor getColorNick() {
        return color_nick;
    }

    public float getFlyspeed() {
        return flyspeed;
    }

    public int getWarns() {
        return warns;
    }

    public int getMutes() {
        return mutes;
    }

    public int getBans() {
        return bans;
    }

    public int getIpBans() {
        return ip_bans;
    }

    public int getPlaytimeHours() {
        return playtime_hours;
    }

    public int getPlaytimeMinutes() {
        return playtime_minutes;
    }

    public boolean isAfk() {
        return afk;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isWhitelisted() {
        return whitelisted;
    }

    public boolean isGodmode() {
        return godmode;
    }

    public boolean isFly() {
        return fly;
    }

    public boolean isMuted() {
        return muted;
    }

    public boolean isDefaultmuted() {
        return defaultmuted;
    }

    public boolean isVanished() {
        return vanished;
    }

    public boolean isOnDuty() {
        return on_duty;
    }

    public boolean hasLogEnabled() {
        return log_enabled;
    }

    public boolean canBypassLockdown() {
        return bypass_lockdown;
    }

    public boolean canEditSign() {
        return edit_sign;
    }

    public List<String> getKnownIps() {
        return known_ips;
    }
}

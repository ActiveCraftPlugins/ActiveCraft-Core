package de.silencio.activecraftcore;

import de.silencio.activecraftcore.commands.*;
import de.silencio.activecraftcore.guicreator.GuiListener;
import de.silencio.activecraftcore.guis.offinvsee.OffInvSeeListener;
import de.silencio.activecraftcore.guis.profilemenu.Listener.MainProfileListener;
import de.silencio.activecraftcore.listener.*;
import org.bukkit.plugin.Plugin;

public class CorePluginManager extends ActiveCraftPluginManager {

    public CorePluginManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        // general listener
        addListeners(
                new JoinQuitListener(), new MessageListener(), new CommandListener(),
                new LockdownListener(), new SignListener(), new SignInteractListener(),
                new VanillaCommandListener(), new ServerPingListener(), new RespawnListener(),
                new DeathListener(), new LoginListener(), new BowCommand(), new GamemodeChangeListener(),
                new ClearTabCompleteListener(), new CommandStickCommand(), new TeleportListener()
                );

        // gui creator
        addListeners(new GuiListener());

        //guis
        addListeners(new OffInvSeeListener(), new MainProfileListener());


        //register ac commands
        addCommands(
                new ACVersionCommand(),
                new AfkCommand(),
                new BackCommand(),
                new BanCommand(),
                new BookCommand(),
                new BowCommand(),
                new BreakCommand(),
                new BroadCastCommand(),
                new ButcherCommand(),
                new ClearInvCommand(),
                new ColorNickCommand(),
                new CommandStickCommand(),
                new ConfigReloadCommand(),
                new DrainCommand(),
                new EditSignCommand(),
                new EffectsCommand(),
                new EnchantCommand(),
                new EnderchestCommand(),
                new ExplodeCommand(),
                new FeedCommand(),
                new FireBallCommand(),
                new FireWorkCommand(),
                new FlyCommand(),
                new GamemodeCommand(),
                new GodCommand(),
                new HatCommand(),
                new HealCommand(),
                new HomeCommand(),
                new InvseeCommand(),
                new ItemCommand(),
                new KickAllCommand(),
                new KickCommand(),
                new KnockbackStickCommand(),
                new LanguageCommand(),
                new LastCoordsCommand(),
                new LastOnlineCommand(),
                new LeatherColorCommand(),
                new LockdownCommand(),
                new LogCommand(),
                new MoreCommand(),
                new MsgCommand(),
                new MuteCommand(),
                new NickCommand(),
                new OfflineTpCommand(),
                new OpItemsCommand(),
                new OffInvSeeCommand(),
                new PingCommand(),
                new PlayerlistCommand(),
                new PlayTimeCommand(),
                new PortalCommand(),
                new ProfileCommand(),
                new RamCommand(),
                new RandomTPCommand(),
                new RealNameCommand(),
                new RepairCommand(),
                new ReplyCommand(),
                new RestartCommand(),
                new SkullCommand(),
                new SocialSpyToggleCommand(),
                new SpawnCommand(),
                new SpawnerCommand(),
                new SummonCommand(),
                new StaffChatCommand(),
                new StrikeCommand(),
                new SudoCommand(),
                new SuicideCommand(),
                new TableCommands(),
                new TableMenuCommand(),
                new TopCommand(),
                new TpaCommand(),
                new TpAllCommand(),
                new TpCommand(),
                new TphereCommand(),
                new VanishCommand(),
                new VerifyCommand(),
                new WalkspeedCommand(),
                new WarnCommand(),
                new WarpCommand(),
                new WeatherCommand(),
                new WhereAmICommand(),
                new WhoIsCommand(),
                new XpCommand()
        );
        register();
    }
}

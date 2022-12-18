package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.commands.util.CommandUtils
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NoPermissionException
import org.activecraft.activecraftcore.exceptions.SelfTargetException
import org.activecraft.activecraftcore.messagesv2.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.*
import org.bukkit.permissions.Permissible
import java.util.*

abstract class ActiveCraftCommandv2 @JvmOverloads constructor(
    val commandName: String,
    val plugin: ActiveCraftPlugin,
    val permission: String = commandName,
    val commandMessagePrefix: String = commandName
) :
    CommandExecutor, TabCompleter, CommandUtils {
    private lateinit var activeSender: CommandSender

    @JvmField
    protected val activeCraftMessage: ActiveCraftMessage = plugin.activeCraftMessagev2

    @JvmField
    protected var messageFormatter = PlayerMessageFormatter(activeCraftMessage)

    @JvmField
    protected val defaultColorScheme = activeCraftMessage.colorScheme

    val aliases: List<String>
        get() {
            val pluginCommand = bukkitCommand
            return pluginCommand?.aliases ?: ArrayList()
        }
    val bukkitCommand: PluginCommand?
        get() = Bukkit.getPluginCommand(commandName)

    @Throws(SelfTargetException::class)
    @JvmOverloads
    protected fun isTargetSelf(
        sender: CommandSender,
        target: CommandSender,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ) = isTargetSelf(sender, target.name, extendedPermission, ignorePermission)

    @Throws(SelfTargetException::class)
    @JvmOverloads
    protected fun isTargetSelf(
        sender: CommandSender,
        target: String,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ): Boolean {
        if (sender.name.equals(target, ignoreCase = true)) {
            val permString = ((if (plugin.permissionGroup != null) plugin.permissionGroup + "." else "")
                    + permission + ".self" + if (extendedPermission != null) ".$extendedPermission" else "")
            if (!ignorePermission && !sender.hasPermission(permString)) throw SelfTargetException(
                sender,
                "null"
            )
            return true
        }
        return false
    }

    @Throws(NoPermissionException::class)
    @JvmOverloads
    protected fun assertPermission(
        permissible: Permissible,
        permissionExtension: String? = null,
        permissionPrefix: String? = plugin.permissionGroup
    ): Boolean {
        val permString = ((if (permissionPrefix != null) "$permissionPrefix." else "")
                + permission
                + if (permissionExtension != null) ".$permissionExtension" else "")
        if (!permissible.hasPermission(permString)) throw NoPermissionException(
            permissible,
            permissionExtension
        )
        return true
    }

    @JvmOverloads
    fun newMessageFormatter(activeCraftMessage: ActiveCraftMessage = this.activeCraftMessage) =
        MessageFormatter(activeCraftMessage)

    @JvmOverloads
    fun msg(
        key: String,
        color: ChatColor? = activeCraftMessage.colorScheme.primary,
        messageSupplier: MessageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter: MessageFormatter = this.messageFormatter
    ) = messageSupplier.getFormatted(key = key, formatter = messageFormatter, color = color)

    @JvmOverloads
    fun msg(
        key: String,
        messageSupplier: MessageSupplier,
        formatter: MessageFormatter = this.messageFormatter
    ) = msg(
        key = key,
        messageSupplier = messageSupplier,
        messageFormatter = formatter,
        color = activeCraftMessage.colorScheme.primary
    )

    fun msg(
        key: String,
        messageFormatter: MessageFormatter,
    ) = msg(
        key = key,
        messageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter = messageFormatter,
        color = activeCraftMessage.colorScheme.primary
    )

    fun msg(
        key: String,
        color: ChatColor?,
        formatter: MessageFormatter
    ) = msg(
        key = key,
        messageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter = formatter,
        color = color
    )

    @JvmOverloads
    fun cmdMsg(
        key: String,
        color: ChatColor? = activeCraftMessage.colorScheme.primary,
        messageSupplier: MessageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter: MessageFormatter = this.messageFormatter
    ) = msg("command.$commandMessagePrefix.$key", color, messageSupplier, messageFormatter)

    @JvmOverloads
    fun cmdMsg(
        key: String,
        messageSupplier: MessageSupplier,
        formatter: MessageFormatter = this.messageFormatter
    ) = cmdMsg(
        key = key,
        messageSupplier = messageSupplier,
        messageFormatter = formatter,
        color = activeCraftMessage.colorScheme.primary
    )

    fun cmdMsg(
        key: String,
        messageFormatter: MessageFormatter,
    ) = cmdMsg(
        key = key,
        messageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter = messageFormatter,
        color = activeCraftMessage.colorScheme.primary
    )

    fun cmdMsg(
        key: String,
        color: ChatColor?,
        formatter: MessageFormatter
    ) = cmdMsg(
        key = key,
        messageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter = formatter,
        color = color
    )

    fun rawCmdMsg(key: String, messageSupplier: MessageSupplier = activeSender.getMessageSupplier(activeCraftMessage)) =
        cmdMsg(key = key, messageSupplier = messageSupplier, color = null)

    @Throws(ActiveCraftException::class)
    protected abstract fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>)
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        try {
            messageFormatter.clearFormatterPatterns()
            activeSender = commandSender
            messageFormatter.setSender(commandSender)
            runCommand(commandSender, command, s, strings)
        } catch (e: ActiveCraftException) {
            for (acp in ActiveCraftPlugin.getInstalledPlugins()) {
                val exceptionList = acp.commandExceptionProcessor.exceptionList
                exceptionList.keys.first { it.isInstance(e) }.let {
                    exceptionList[it]?.accept(e, commandSender)
                }
            }
        }
        return false
    }

    protected abstract fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>?

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val list = onTab(sender, command, alias, args) ?: return emptyList()
        return list.filter { s: String ->
            s.lowercase(
                Locale.getDefault()
            ).startsWith(args[args.size - 1].lowercase(Locale.getDefault()))
        }.toList()
    }

    companion object {
        init {
            registerCommandExceptions()
        }
    }
}
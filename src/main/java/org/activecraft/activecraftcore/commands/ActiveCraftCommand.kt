package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.commands.util.CommandUtils
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NoPermissionException
import org.activecraft.activecraftcore.exceptions.SelfTargetException
import org.activecraft.activecraftcore.messages.*
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.*
import org.bukkit.permissions.Permissible
import java.util.*

abstract class ActiveCraftCommand constructor(
    val commandName: String,
    val plugin: ActiveCraftPlugin,
    val permission: String = commandName,
    val commandMessagePrefix: String = commandName
) :
    CommandExecutor, TabCompleter, CommandUtils {
    private lateinit var activeSender: CommandSender

    protected val activeCraftMessage
        get() = plugin.activeCraftMessage
            ?: throw IllegalStateException("No instance of class ActiveCraftMessage found for ActiveCraftPlugin ${plugin.name}")

    protected var messageFormatter = PlayerMessageFormatter(activeCraftMessage)

    protected val defaultColorScheme = activeCraftMessage.colorScheme

    val aliases: List<String>
        get() {
            val pluginCommand = bukkitCommand
            return pluginCommand?.aliases ?: ArrayList()
        }
    val bukkitCommand: PluginCommand?
        get() = Bukkit.getPluginCommand(commandName)

    fun getMessageSupplier(
        sender: CommandSender = activeSender,
        activeCraftMessage: ActiveCraftMessage = this.activeCraftMessage
    ): MessageSupplier {
        val profile = Profile.of(sender)
        return profile?.getMessageSupplier(activeCraftMessage) ?: activeCraftMessage.getDefaultMessageSupplier()!!
    }

    @Throws(SelfTargetException::class)
    protected fun isTargetSelf(
        sender: CommandSender,
        target: CommandSender,
        ignorePermission: Boolean
    ) = isTargetSelf(sender, target.name, null, ignorePermission)

    @Throws(SelfTargetException::class)
    protected fun isTargetSelf(
        sender: CommandSender,
        target: CommandSender,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ) = isTargetSelf(sender, target.name, extendedPermission, ignorePermission)

    @Throws(SelfTargetException::class)
    protected fun isTargetSelf(
        sender: CommandSender,
        target: String,
        ignorePermission: Boolean = false
    ) = isTargetSelf(sender, target, this, null, ignorePermission)

    @Throws(SelfTargetException::class)
    protected fun isTargetSelf(
        sender: CommandSender,
        target: String,
        extendedPermission: String? = null,
        ignorePermission: Boolean = false
    ) = isTargetSelf(sender, target, this, extendedPermission, ignorePermission)

    @Throws(NoPermissionException::class)
    protected fun assertCommandPermission(
        permissible: Permissible,
        permissionExtension: String? = null,
        permissionPrefix: String? = plugin.permissionGroup
    ) = assertPermission(this, permissible, permissionExtension, permissionPrefix)

    fun newMessageFormatter(activeCraftMessage: ActiveCraftMessage = this.activeCraftMessage) =
        MessageFormatter(activeCraftMessage)

    fun newPlayerMessageFormatter(activeCraftMessage: ActiveCraftMessage = this.activeCraftMessage) =
        PlayerMessageFormatter(activeCraftMessage)

    fun msg(
        key: String,
        color: ChatColor? = activeCraftMessage.colorScheme.primary,
        messageSupplier: MessageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter: MessageFormatter = this.messageFormatter
    ) = messageSupplier.getFormatted(key = key, formatter = messageFormatter, color = color)

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

    fun cmdMsg(
        key: String,
        color: ChatColor? = activeCraftMessage.colorScheme.primary,
        messageSupplier: MessageSupplier = activeSender.getMessageSupplier(activeCraftMessage),
        messageFormatter: MessageFormatter = this.messageFormatter
    ) = msg(
        key = "command.$commandMessagePrefix.$key",
        color = color,
        messageSupplier = messageSupplier,
        messageFormatter = messageFormatter
    )

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

    fun sendWarningMessage(receiver: CommandSender, message: String) =
        sendWarningMessage(receiver, message, activeCraftMessage)

    fun sendSilentWarningMessage(receiver: CommandSender, message: String) =
        sendSilentWarningMessage(receiver, message, activeCraftMessage)

    @Throws(ActiveCraftException::class)
    protected abstract fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>)
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        try {
            messageFormatter.clearFormatterPatterns()
            activeSender = commandSender
            messageFormatter.setSender(commandSender)
            runCommand(commandSender, command, s, strings)
        } catch (e: ActiveCraftException) {
            for (acp in ActiveCraftPlugin.installedPlugins) {
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
            ).startsWith(args[args.size - 1].lowercase())
        }.toList()
    }

    companion object {
        init {
            registerCommandExceptions()
        }
    }
}
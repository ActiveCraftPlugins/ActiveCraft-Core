package org.activecraft.activecraftcore.guis.profilemenu

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.guicreator.GuiBackItem
import org.activecraft.activecraftcore.guicreator.GuiCloseItem
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.backItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.closeItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead
import org.activecraft.activecraftcore.guis.profilemenu.inventory.*
import org.activecraft.activecraftcore.messages.ColorScheme
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.getMessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.entity.Player

class ProfileMenu(val player: Player, val target: Player) {
    val profile: Profile = of(target)
    val playerHead: GuiPlayerHead = GuiPlayerHead(target)
    val mainProfile: MainProfile
    val actionProfile: ActionProfile
    val violationsProfile: ViolationsProfile
    val gamemodeSwitcherProfile: GamemodeSwitcherProfile
    val homeListPageLayout: HomeListPageLayout
    val storageProfile: StorageProfile
    val messageSupplier: MessageSupplier = player.getMessageSupplier(ActiveCraftCore.INSTANCE.activeCraftMessage!!)
    val colorScheme: ColorScheme = messageSupplier.colorScheme
    val defaultGuiCloseItem: GuiCloseItem = GuiCloseItem(closeItemDisplayname(messageSupplier))
    val defaultGuiBackItem: GuiBackItem = GuiBackItem(backItemDisplayname(messageSupplier))

    init {
        //playerhead
        playerHead.setLore(
            colorScheme.primary.toString() + "aka " + profile.nickname,
            colorScheme.primaryAccent.toString() + profile.uuid.toString()
        )
        playerHead.setDisplayName(colorScheme.primary.toString() + target.name)

        // menus
        mainProfile = MainProfile(this)
        actionProfile = ActionProfile(this)
        violationsProfile = ViolationsProfile(this)
        gamemodeSwitcherProfile = GamemodeSwitcherProfile(this)
        homeListPageLayout = HomeListPageLayout(this)
        storageProfile = StorageProfile(this)
    }
}
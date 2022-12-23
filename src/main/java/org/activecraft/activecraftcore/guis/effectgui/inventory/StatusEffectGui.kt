package org.activecraft.activecraftcore.guis.effectgui.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.activecraft.activecraftcore.guis.effectgui.item.EffectItem
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.potion.PotionEffectType

class StatusEffectGui(val effectGui: EffectGui) :
    GuiCreator(
        identifier = "status_effect_gui",
        rows = 6,
        title = effectGui.messageSupplier.getMessage("effectgui.header.statuseffects")
    ) {
    val player: Player = effectGui.player
    private val target: Player = effectGui.target
    private val profile: Profile = effectGui.profile
    private var prevPage: GuiItem? = null
    private val headerItem: GuiItem? = null
    private var absorption: EffectItem? = null
    private var badLuck: EffectItem? = null
    private var badOmen: EffectItem? = null
    private var blindness: EffectItem? = null
    private var conduitPower: EffectItem? = null
    private var dolphinsGrace: EffectItem? = null
    private var glowing: EffectItem? = null
    private var haste: EffectItem? = null
    private var healthBoost: EffectItem? = null
    private var villageHero: EffectItem? = null
    private var hunger: EffectItem? = null
    private var levitation: EffectItem? = null
    private var miningFatigue: EffectItem? = null
    private var nausea: EffectItem? = null
    private var resistance: EffectItem? = null
    private var saturation: EffectItem? = null
    private var wither: EffectItem? = null

    override fun refresh() {
        fillBackground(true)
        setItem(effectGui.defaultGuiCloseItem, 49)
        setItem(GuiPlayerHead(target), 4)
        setItem(GuiItem(Material.ARROW)
            .setDisplayName(ChatColor.GRAY.toString() + effectGui.messageSupplier.getMessage("effectgui.header.potioneffects"))
            .addClickListener {
                pushReplacement(
                    player,
                    effectGui.potionEffectGui.build()
                )
            }
            .also { prevPage = it },
            45
        )
        badOmen = EffectItem(Material.WHITE_BANNER, PotionEffectType.BAD_OMEN, effectGui)
        val bannerMeta = badOmen!!.itemMeta as BannerMeta
        bannerMeta.addPattern(Pattern(DyeColor.CYAN, PatternType.RHOMBUS_MIDDLE))
        bannerMeta.addPattern(Pattern(DyeColor.LIGHT_GRAY, PatternType.STRIPE_BOTTOM))
        bannerMeta.addPattern(Pattern(DyeColor.GRAY, PatternType.STRIPE_CENTER))
        bannerMeta.addPattern(Pattern(DyeColor.LIGHT_GRAY, PatternType.BORDER))
        bannerMeta.addPattern(Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE))
        bannerMeta.addPattern(Pattern(DyeColor.LIGHT_GRAY, PatternType.HALF_HORIZONTAL))
        bannerMeta.addPattern(Pattern(DyeColor.LIGHT_GRAY, PatternType.CIRCLE_MIDDLE))
        bannerMeta.addPattern(Pattern(DyeColor.BLACK, PatternType.BORDER))
        badOmen!!.itemMeta = bannerMeta
        setItem(EffectItem(Material.SHIELD, PotionEffectType.ABSORPTION, effectGui).also { absorption = it }, 10)
        setItem(EffectItem(Material.DEAD_BUSH, PotionEffectType.UNLUCK, effectGui).also { badLuck = it }, 11)
        setItem(badOmen, 12)
        setItem(EffectItem(Material.BLACK_CONCRETE, PotionEffectType.BLINDNESS, effectGui).also { blindness = it }, 13)
        setItem(EffectItem(Material.CONDUIT, PotionEffectType.CONDUIT_POWER, effectGui).also { conduitPower = it }, 14)
        setItem(
            EffectItem(
                Material.DOLPHIN_SPAWN_EGG,
                PotionEffectType.DOLPHINS_GRACE,
                effectGui
            ).also { dolphinsGrace = it }, 15
        )
        setItem(EffectItem(Material.SPECTRAL_ARROW, PotionEffectType.GLOWING, effectGui).also { glowing = it }, 16)
        setItem(EffectItem(Material.GOLDEN_PICKAXE, PotionEffectType.FAST_DIGGING, effectGui).also { haste = it }, 19)
        setItem(
            EffectItem(Material.LINGERING_POTION, PotionEffectType.HEALTH_BOOST, effectGui, true).also {
                healthBoost = it
            }, 20
        )
        setItem(
            EffectItem(Material.EMERALD, PotionEffectType.HERO_OF_THE_VILLAGE, effectGui).also { villageHero = it },
            21
        )
        setItem(EffectItem(Material.ROTTEN_FLESH, PotionEffectType.HUNGER, effectGui).also { hunger = it }, 22)
        setItem(EffectItem(Material.SHULKER_SHELL, PotionEffectType.LEVITATION, effectGui).also { levitation = it }, 23)
        setItem(EffectItem(Material.DARK_PRISMARINE, PotionEffectType.SLOW_DIGGING, effectGui).also {
            miningFatigue = it
        }, 24)
        setItem(EffectItem(Material.PUFFERFISH, PotionEffectType.CONFUSION, effectGui).also { nausea = it }, 25)
        setItem(
            EffectItem(
                Material.NETHERITE_CHESTPLATE,
                PotionEffectType.DAMAGE_RESISTANCE,
                effectGui
            ).also { resistance = it }, 28
        )
        setItem(EffectItem(Material.BREAD, PotionEffectType.SATURATION, effectGui).also { saturation = it }, 29)
        setItem(EffectItem(Material.WITHER_SKELETON_SKULL, PotionEffectType.WITHER, effectGui).also { wither = it }, 30)
    }
}
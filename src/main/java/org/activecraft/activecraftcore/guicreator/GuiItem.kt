package org.activecraft.activecraftcore.guicreator

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

open class GuiItem : ItemStack {
    var clickSound: Sound? = Sound.UI_BUTTON_CLICK
        protected set
    var movable = false
        protected set
    val clickListenerList: MutableList<(GuiClickEvent) -> Unit> = mutableListOf()

    @JvmOverloads
    constructor(material: Material, stackSize: Int = 1) : super(material, stackSize)
    constructor(itemStack: ItemStack) : super(itemStack.type) {
        this.itemMeta = itemStack.itemMeta
        amount = itemStack.amount
        for (itemFlag in itemStack.itemFlags) {
            addItemFlags(itemFlag!!)
        }
    }

    fun setDisplayName(displayName: String): GuiItem {
        val itemMeta = this.itemMeta
        itemMeta.setDisplayName(displayName)
        this.itemMeta = itemMeta
        return this
    }

    fun setLore(vararg lore: String): GuiItem {
        val stringList: List<String> = listOf(*lore)
        val itemMeta = this.itemMeta
        itemMeta.lore = stringList
        this.itemMeta = itemMeta
        return this
    }

    fun setLore(index: Int, lore: String): GuiItem {
        val itemMeta = this.itemMeta
        val loreList = if (itemMeta.lore != null) itemMeta.lore else ArrayList()
        loreList!![loreList.size.coerceAtMost(index)] = lore
        setLore(loreList)
        return this
    }

    fun addLore(vararg lore: String): GuiItem {
        val itemMeta = this.itemMeta
        val loreList = if (itemMeta.lore != null) itemMeta.lore else ArrayList()
        loreList!!.addAll(listOf(*lore))
        setLore(loreList)
        return this
    }

    fun removeLore(i: Int): GuiItem {
        val itemMeta = this.itemMeta
        val loreList: MutableList<String> = ArrayList()
        if (itemMeta.lore != null) loreList.addAll(itemMeta.lore!!)
        loreList.removeAt(i)
        lore = loreList
        return this
    }

    fun removeLore(start: Int, end: Int): GuiItem {
        for (i in start until end) removeLore(i)
        return this
    }

    val displayName: String
        get() = this.itemMeta.displayName

    override fun getLore() = itemMeta.lore

    fun setClickSound(clickSound: Sound?): GuiItem {
        this.clickSound = clickSound
        return this
    }

    fun setMovable(movable: Boolean): GuiItem {
        this.movable = movable
        return this
    }

    fun toItemStack() = this as ItemStack

    fun setGlint(glint: Boolean): GuiItem {
        if (glint) {
            addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        } else if (getEnchantmentLevel(Enchantment.DURABILITY) == 1) {
            removeEnchantment(Enchantment.DURABILITY)
        }
        return this
    }

    fun addClickListener(clickListener: (GuiClickEvent) -> Unit): GuiItem {
        clickListenerList.add(clickListener)
        return this
    }
}
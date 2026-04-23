package com.marcpg.cotton.boll

import com.marcpg.cotton.style.Outline
import com.marcpg.cotton.style.Style
import com.marcpg.cotton.style.StyleDecoration
import com.marcpg.cotton.util.Color

abstract class BollImpl<T>(
    open var content: T,
    open var style: Style = Style(),
    open val followingBolls: MutableList<Boll> = mutableListOf(),
) : Boll {
    override fun append(boll: Boll): Boll {
        followingBolls += boll
        return this
    }

    override fun fullStyle(): Style = style
    override fun fullStyle(style: Style): Boll {
        this.style = style
        return this
    }

    override fun color(): Color? = style.color
    override fun color(color: Color?): Boll {
        style.color = color
        return this
    }

    override fun decorationState(decoration: StyleDecoration): Boolean? = style.decorations[decoration]
    override fun decoration(decoration: StyleDecoration, state: Boolean?): Boll {
        style.decorations[decoration] = state
        return this
    }
    override fun decorationIfAbsent(decoration: StyleDecoration, state: Boolean?): Boll {
        if (decoration !in style.decorations)
            style.decorations[decoration] = state
        return this
    }

    override fun font(): Any? = style.font
    override fun font(font: Any?): Boll {
        style.font = font
        return this
    }

    override fun backgroundColor(): Color? = style.backgroundColor
    override fun backgroundColor(color: Color?): Boll {
        style.backgroundColor = color
        return this
    }

    override fun outline(): Outline? = style.outline
    override fun outline(outline: Outline?): Boll {
        style.outline = outline
        return this
    }
}

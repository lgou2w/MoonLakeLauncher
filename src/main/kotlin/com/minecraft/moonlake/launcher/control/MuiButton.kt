/*
 * Copyright (C) 2017 The MoonLake Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minecraft.moonlake.launcher.control

import com.minecraft.moonlake.launcher.skin.MuiButtonSkin
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Skin
import javafx.scene.paint.Paint

open class MuiButton: Button {

    /**************************************************************************
     *
     * Enum Class
     *
     **************************************************************************/

    enum class Type {
        FLAT, RAISED
    }

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): super()
    constructor(text: String, graphic: Node): super(text, graphic)

    init {
        isFocusTraversable = false
        styleClass.add("mui-button")
    }

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun createDefaultSkin(): Skin<*>
            = MuiButtonSkin(this)

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var type: ObjectProperty<Type>
            = SimpleObjectProperty(Type.FLAT)

    fun typeProperty(): ObjectProperty<Type>
            = type

    fun getType(): Type
            = if(type is ObjectProperty) type.get() else Type.FLAT

    fun setType(type: Type)
            = this.type.set(type)

    private var ripplerFill: ObjectProperty<Paint>
            = SimpleObjectProperty(null)

    fun ripplerFillProperty(): ObjectProperty<Paint>
            = ripplerFill

    fun getRipplerFill(): Paint?
            =  ripplerFill.get()

    fun setRipplerFill(ripplerFill: Paint)
            = this.ripplerFill.set(ripplerFill)
}

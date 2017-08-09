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

package com.minecraft.moonlake.launcher.svg

import com.minecraft.moonlake.launcher.layout.MuiPane
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.StackPane
import javafx.scene.paint.Paint
import javafx.scene.shape.SVGPath

class MuiSVGGlyph : MuiPane {

    /**************************************************************************
     *
     * Static
     *
     **************************************************************************/

    companion object {
        val DEFAULT_PREF_SIZE = 64.0
    }

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private val name: String
    private var widthHeightRatio = 1.0
    private var fill: ObjectProperty<Paint> = SimpleObjectProperty()

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(name: String, path: String, fill: Paint): super() {
        this.name = name
        this.fill.addListener { _, _, newValue -> background = Background(BackgroundFill(newValue, null, null)) }
        this.fill.set(fill)

        val svgPath = SVGPath()
        svgPath.content = path
        shape = svgPath
        widthHeightRatio = svgPath.prefWidth(-1.0) / svgPath.prefHeight(-1.0)
        setPrefSize(DEFAULT_PREF_SIZE, DEFAULT_PREF_SIZE)
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun getName(): String
            = name

    fun fillProperty(): ObjectProperty<Paint>
            = fill

    fun getFill(): Paint
            = fill.get()

    fun setFill(fill: Paint)
            = this.fill.set(fill)

    fun setSize(width: Double, height: Double) {
        setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE)
        setPrefSize(width, height)
        setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE)
    }

    fun setSizeRatio(size: Double) {
        val width = widthHeightRatio * size
        val height = size / widthHeightRatio
        if(width <= size)
            setSize(width, size)
        else if(height <= size)
            setSize(size, height)
        else
            setSize(size, size)
    }
}

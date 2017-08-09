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

import com.minecraft.moonlake.launcher.layout.MuiStackPane
import javafx.beans.property.*
import javafx.geometry.Insets
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

class MuiResourceSVGGlyph: MuiStackPane {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var svgGlyph: MuiSVGGlyph? = null

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): super() {
        this.url.addListener { _, _, newValue -> run {
            if(newValue != null) {
                this.svgGlyph = MuiSVGGlyphLoader.loadMuiSVG(newValue)
                this.addEventHandler(MouseEvent.MOUSE_ENTERED, { _ -> run {
                    if(this.svgGlyph != null && enterFill.isNotNull.get())
                        this.svgGlyph!!.setFill(enterFill.get())
                }})
                this.addEventHandler(MouseEvent.MOUSE_EXITED, { _ -> run {
                    if(this.svgGlyph != null)
                        this.svgGlyph!!.setFill(fill.get())
                }})
                if(this.svgGlyph != null)
                    this.svgGlyph!!.styleClass.add("mui-res-svg")
                this.children.add(0, svgGlyph)
            } else if(this.svgGlyph != null) {
                this.children.remove(svgGlyph)
                this.svgGlyph = null
            }
        }}
        this.sizeRatio.addListener { _, _, newValue -> run {
            if(this.svgGlyph != null)
                this.svgGlyph!!.setSizeRatio(newValue.toDouble())
        }}
        this.fill.addListener { _, _, newValue -> run {
            if(this.svgGlyph != null)
                this.svgGlyph!!.setFill(newValue)
        }}
        this.padding = Insets(5.0)
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var url: StringProperty = SimpleStringProperty()

    fun urlProperty(): StringProperty
            = url

    fun getUrl(): String
            = url.get()

    fun setUrl(url: String)
            = this.url.set(url)

    private var sizeRatio: DoubleProperty = SimpleDoubleProperty()

    fun sizeRatioProperty(): DoubleProperty
            = sizeRatio

    fun getSizeRatio(): Double
            = sizeRatio.get()

    fun setSizeRatio(sizeRatio: Double)
            = this.sizeRatio.set(sizeRatio)

    private var fill: ObjectProperty<Paint> = SimpleObjectProperty(Color.BLACK)

    fun fillProperty(): ObjectProperty<Paint>
            = fill

    fun getFill(): Paint
            = fill.get()

    fun setFill(fill: Paint)
            = this.fill.set(fill)

    private var enterFill: ObjectProperty<Paint> = SimpleObjectProperty(null)

    fun enterFillProperty(): ObjectProperty<Paint>
            = enterFill

    fun getEnterFill(): Paint
            = enterFill.get()

    fun setEnterFill(enterFill: Paint)
            = this.enterFill.set(enterFill)
}

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

import com.minecraft.moonlake.launcher.layout.MuiBorderPane
import com.minecraft.moonlake.launcher.layout.MuiStackPane
import com.minecraft.moonlake.launcher.util.MuiDepthUtils
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.paint.Color

class MuiNavbar: MuiStackPane {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var pane = MuiBorderPane()

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): super() {
        this.pane.leftProperty().bind(left)
        this.pane.rightProperty().bind(right)
        this.pane.backgroundProperty().bind(backgroundProperty())
        this.pane.prefHeightProperty().bind(prefHeightProperty())
        this.pane.prefWidthProperty().bind(prefWidthProperty())
        this.background = Background(BackgroundFill(Color.valueOf("#00acc1"), null, null))
        this.children.add(0, pane)
        this.styleClass.add("mui-navbar")
        MuiDepthUtils.setNodeDepth(pane, 2)
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var left: ObjectProperty<Node> = SimpleObjectProperty(null)

    fun leftProperty(): ObjectProperty<Node>
            = left

    fun getLeft(): Node
            = left.get()

    fun setLeft(left: Node)
            = this.left.set(left)

    private var right: ObjectProperty<Node> = SimpleObjectProperty(null)

    fun rightProperty(): ObjectProperty<Node>
            = right

    fun getRight(): Node
            = right.get()

    fun setRight(right: Node)
            = this.right.set(right)
}

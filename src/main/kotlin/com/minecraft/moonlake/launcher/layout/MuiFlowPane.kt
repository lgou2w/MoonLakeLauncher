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

package com.minecraft.moonlake.launcher.layout

import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.layout.FlowPane

open class MuiFlowPane: FlowPane {

    constructor(): super()
    constructor(orientation: Orientation?) : super(orientation)
    constructor(hgap: Double, vgap: Double) : super(hgap, vgap)
    constructor(orientation: Orientation?, hgap: Double, vgap: Double) : super(orientation, hgap, vgap)
    constructor(vararg children: Node?) : super(*children)
    constructor(orientation: Orientation?, vararg children: Node?) : super(orientation, *children)
    constructor(hgap: Double, vgap: Double, vararg children: Node?) : super(hgap, vgap, *children)
    constructor(orientation: Orientation?, hgap: Double, vgap: Double, vararg children: Node?) : super(orientation, hgap, vgap, *children)

    init {
    }
}

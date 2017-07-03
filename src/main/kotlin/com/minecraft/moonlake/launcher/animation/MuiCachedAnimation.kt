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

package com.minecraft.moonlake.launcher.animation

import javafx.animation.Animation
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node

open class MuiCachedAnimation(animation: Animation, vararg muiCachedNodes: Node) {

    protected var animation: ObjectProperty<Animation> = SimpleObjectProperty()
    private var moments: Array<MuiCachedMoment>? = null

    init {
        if(muiCachedNodes.isNotEmpty())
            moments = Array(muiCachedNodes.size, { i -> MuiCachedMoment(muiCachedNodes[i]) })
        this.animation.set(animation)
        this.animation.get().statusProperty().addListener { _, _, newValue -> run {
            when(newValue) {
                Animation.Status.RUNNING -> starting()
                else -> stopping()
            }
        }}
    }

    protected fun starting() {
        if(moments != null)
            moments!!.forEach { moment -> moment.cached() }
    }

    protected fun stopping() {
        if(moments != null)
            moments!!.forEach { moment -> moment.restore() }
    }

    fun getAnimation(): Animation
            = animation.get()
}

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

import javafx.animation.Timeline
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.util.Duration

open class MuiCachedTransition(node: Node, timeline: Timeline, vararg muiCachedMoments: MuiCachedMoment): MuiTransition() {

    protected var node: Node = node
    protected var timeline: ObjectProperty<Timeline> = SimpleObjectProperty()
    private var moments: Array<out MuiCachedMoment> = arrayOf()
    private var nodeCachedMoment: MuiCachedMoment? = null

    init {
        this.timeline.set(timeline)
        this.moments = muiCachedMoments
        this.statusProperty().addListener { _, _, newStatus -> run {
            when(newStatus) {
                Status.RUNNING -> starting()
                else -> stopping()
            }
        }}
    }

    protected fun starting() {
        nodeCachedMoment = MuiCachedMoment(node)
        nodeCachedMoment!!.cached()
        moments.iterator().forEach { moment -> moment.cached() }
    }

    protected fun stopping() {
        nodeCachedMoment!!.restore()
        moments.iterator().forEach { moment -> moment.restore() }
    }

    override fun interpolate(frac: Double) {
        timeline.get().playFrom(Duration.seconds(frac))
        timeline.get().stop()
    }
}

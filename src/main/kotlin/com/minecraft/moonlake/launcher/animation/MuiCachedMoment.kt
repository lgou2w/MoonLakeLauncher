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

import javafx.scene.CacheHint
import javafx.scene.Node
import javafx.scene.layout.Region

import java.util.HashMap

open class MuiCachedMoment(val node: Node) {

    private var cache: Boolean = false
    private var cacheShape: Boolean = false
    private var snapToPixel: Boolean = false
    private var cacheHint = CacheHint.DEFAULT

    companion object {
        private val history: MutableMap<Node, MuiCachedMoment> = HashMap()
    }

    fun cached() {
        if (!history.containsKey(node)) {
            this.cache = node.isCache
            this.cacheHint = node.cacheHint
            node.isCache = true
            node.cacheHint = CacheHint.SPEED
            if (node is Region) {
                this.cacheShape = node.isCacheShape
                this.snapToPixel = node.isSnapToPixel
                node.isCacheShape = true
                node.isSnapToPixel = true
            }
            history.put(node, this)
        } else {
            val cached = MuiCachedMoment(node)
            this.cache = cached.cache
            this.cacheHint = cached.cacheHint
            this.cacheShape = cached.cacheShape
            this.snapToPixel = cached.snapToPixel
        }
    }

    fun restore() {
        node.isCache = cache
        node.cacheHint = cacheHint
        if (node is Region) {
            node.isCacheShape = cacheShape
            node.isSnapToPixel = snapToPixel
        }
        history.remove(node)
    }
}

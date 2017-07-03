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

package com.minecraft.moonlake.launcher.animation.direction

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.util.Duration

class MuiLeftTransition(contentHolder: Node, pane: Pane, offsetX: Double, cycleDuration: Duration = Duration.seconds(.4), delay: Duration = Duration.seconds(.0)): MuiDirectionTransition(Direction.Type.LEFT, contentHolder, Timeline(
        KeyFrame(Duration.ZERO,
                KeyValue(contentHolder.translateXProperty(), -offsetX, Interpolator.EASE_BOTH),
                KeyValue(pane.visibleProperty(), false, Interpolator.EASE_BOTH)
        ),
        KeyFrame(Duration.millis(10.0),
                KeyValue(pane.visibleProperty(), true, Interpolator.EASE_BOTH),
                KeyValue(pane.opacityProperty(), 0, Interpolator.EASE_BOTH)
        ),
        KeyFrame(Duration.millis(1000.0),
                KeyValue(contentHolder.translateXProperty(), 0, Interpolator.EASE_BOTH),
                KeyValue(pane.opacityProperty(), 1, Interpolator.EASE_BOTH)))) {
    init {
        setCycleDuration(cycleDuration)
        setDelay(delay)
    }
}

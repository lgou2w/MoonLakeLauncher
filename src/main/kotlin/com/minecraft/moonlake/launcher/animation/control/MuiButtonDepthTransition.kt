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

package com.minecraft.moonlake.launcher.animation.control

import com.minecraft.moonlake.launcher.animation.MuiCachedTransition
import com.minecraft.moonlake.launcher.util.MuiDepthUtils
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.util.Duration

class MuiButtonDepthTransition(muiButtonContainer: Node, from: Int = 1, to: Int = 2, cycleDuration: Duration = Duration.seconds(.2), delay: Duration = Duration.seconds(.0)): MuiCachedTransition(muiButtonContainer, Timeline(
        KeyFrame(Duration.ZERO,
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).radiusProperty(),
                        MuiDepthUtils.getShadowAt(from).radiusProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).spreadProperty(),
                        MuiDepthUtils.getShadowAt(from).spreadProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).offsetXProperty(),
                        MuiDepthUtils.getShadowAt(from).offsetXProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).offsetYProperty(),
                        MuiDepthUtils.getShadowAt(from).offsetYProperty().get(),
                        Interpolator.EASE_BOTH)
        ),
        KeyFrame(Duration(500.0),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).radiusProperty(),
                        MuiDepthUtils.getShadowAt(to).radiusProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).spreadProperty(),
                        MuiDepthUtils.getShadowAt(to).spreadProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).offsetXProperty(),
                        MuiDepthUtils.getShadowAt(to).offsetXProperty().get(),
                        Interpolator.EASE_BOTH),
                KeyValue(
                        (muiButtonContainer.effect as DropShadow).offsetYProperty(),
                        MuiDepthUtils.getShadowAt(to).offsetYProperty().get(),
                        Interpolator.EASE_BOTH)))) {

    init {
        setCycleDuration(cycleDuration)
        setDelay(delay)
    }
}

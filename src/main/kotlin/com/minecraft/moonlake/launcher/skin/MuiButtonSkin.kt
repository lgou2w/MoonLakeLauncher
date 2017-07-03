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

package com.minecraft.moonlake.launcher.skin

import com.minecraft.moonlake.launcher.animation.MuiTransition
import com.minecraft.moonlake.launcher.animation.control.MuiButtonDepthTransition
import com.minecraft.moonlake.launcher.control.MuiButton
import com.minecraft.moonlake.launcher.control.MuiRippler
import com.minecraft.moonlake.launcher.layout.MuiStackPane
import com.minecraft.moonlake.launcher.util.MuiDepthUtils
import com.minecraft.moonlake.launcher.util.MuiUtils
import com.sun.javafx.scene.control.skin.ButtonSkin
import com.sun.javafx.scene.control.skin.LabeledText
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.util.Duration
import java.util.concurrent.Callable

open class MuiButtonSkin : ButtonSkin {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private val defaultRadii = CornerRadii(3.0)
    private val muiButtonContainer = MuiStackPane()
    private var releaseManualRippler: Runnable? = null
    private var depthAnimation: MuiTransition? = null
    private var muiButtonRippler: MuiRippler
    private var invalidLayout = true

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(muiButton: MuiButton): super(muiButton) {
        muiButtonRippler = object: MuiRippler(MuiStackPane()) {
            override fun getMask(): Node {
                val mask = MuiStackPane()
                mask.shapeProperty().bind(muiButtonContainer.shapeProperty())
                mask.backgroundProperty().bind(Bindings.createObjectBinding(Callable {
                    Background(BackgroundFill(Color.WHITE,
                            if(muiButtonContainer.backgroundProperty().get() != null && muiButtonContainer.background.fills.size > 0)
                                muiButtonContainer.background.fills[0].radii
                            else
                                defaultRadii,
                            if(muiButtonContainer.backgroundProperty().get() != null && muiButtonContainer.background.fills.size > 0)
                                muiButtonContainer.background.fills[0].insets
                            else
                                Insets.EMPTY
                            ))
                }, muiButtonContainer.backgroundProperty()))

                mask.resize(muiButtonContainer.width - muiButtonContainer.snappedRightInset() - muiButtonContainer.snappedLeftInset(),
                        muiButtonContainer.height - muiButtonContainer.snappedBottomInset() - muiButtonContainer.snappedTopInset())
                return mask
            }
            override fun initListeners() {
                ripplerPane.setOnMousePressed { event -> run {
                    if(releaseManualRippler != null)
                        releaseManualRippler!!.run()
                    releaseManualRippler = null
                    createRipple(event.x, event.y)
                }}
            }
        }
        skinnable.armedProperty().addListener { _, _, newValue -> run {
            if(newValue) {
                releaseManualRippler = muiButtonRippler.createManualRipple()
            } else {
                if(releaseManualRippler != null)
                    releaseManualRippler!!.run()
            }
        }}
        muiButtonContainer.children.add(muiButtonRippler)
        muiButton.typeProperty().addListener { _, _, newValue -> updateType(newValue) }
        muiButton.setOnMouseEntered {
            if(depthAnimation != null) {
                depthAnimation!!.rate = 1.0
                depthAnimation!!.play()
            }
        }
        muiButton.setOnMouseExited {
            if(depthAnimation != null) {
                depthAnimation!!.rate = -1.0
                depthAnimation!!.play()
            }
        }
        muiButton.focusedProperty().addListener { _, _, newValue -> run {
            if(newValue) {
                if (!skinnable.isPressed)
                    muiButtonRippler.showOverlay()
            } else {
                muiButtonRippler.hideOverlay()
            }
        }}
        muiButton.ripplerFillProperty().addListener { _, _, newValue -> muiButtonRippler.setRipplerFill(newValue) }
        muiButton.pressedProperty().addListener { _, _, _ -> muiButtonRippler.hideOverlay() }
        muiButton.isPickOnBounds = false
        muiButtonContainer.isPickOnBounds = false
        muiButtonContainer.shapeProperty().bind(skinnable.shapeProperty())
        muiButtonContainer.borderProperty().bind(skinnable.borderProperty())
        muiButtonContainer.backgroundProperty().bind(Bindings.createObjectBinding(Callable {
            if(muiButton.background == null || MuiUtils.isDefaultBackground(muiButton.background) || MuiUtils.isDefaultClickedBackground(muiButton.background))
                updateBackground(muiButton.getType())
            try {
                val background = skinnable.background
                if(background != null && background.fills[0].insets == Insets(-.2, -.2, -.2, -.2)) {
                    Background(BackgroundFill(
                            if(skinnable.background != null)
                                skinnable.background.fills[0].fill
                            else
                                Color.TRANSPARENT,
                            if(skinnable.background != null)
                                skinnable.background.fills[0].radii
                            else
                                defaultRadii,
                            Insets.EMPTY))
                } else {
                    Background(BackgroundFill(
                            if(skinnable.background != null)
                                skinnable.background.fills[0].fill
                            else
                                Color.TRANSPARENT,
                            if(skinnable.background != null)
                                skinnable.background.fills[0].radii
                            else
                                defaultRadii,
                            Insets.EMPTY))
                }
            } catch (e: Exception) {
                skinnable.background
            }
        }, skinnable.backgroundProperty()))

        if(muiButton.background == null || MuiUtils.isDefaultBackground(muiButton.background))
            updateBackground(muiButton.getType())

        updateType(muiButton.getType())
        updateChildren()
    }

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    final override fun updateChildren() {
        super.updateChildren()
        if(muiButtonContainer is MuiStackPane)
            children.add(0, muiButtonContainer)
        for(i in 1..children.size - 1)
            children[i].isMouseTransparent = true
    }

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        if(invalidLayout) {
            if(skinnable().getRipplerFill() == null) {
                (children.size - 1 downTo 1).map { children[it] }.forEach {
                    if(it is LabeledText) {
                        muiButtonRippler.setRipplerFill(it.fill)
                        it.fillProperty().addListener { _, _, newValue -> muiButtonRippler.setRipplerFill(newValue) }
                    } else if(it is Label) {
                        muiButtonRippler.setRipplerFill(it.textFill)
                        it.textFillProperty().addListener { _, _, newValue -> muiButtonRippler.setRipplerFill(newValue) }
                    }
                }
            } else {
                muiButtonRippler.setRipplerFill(skinnable().getRipplerFill()!!)
            }
            invalidLayout = false
        }
        muiButtonContainer.resizeRelocate(skinnable.layoutBounds.minX - 1, skinnable.layoutBounds.minY - 1, skinnable.width + 2, skinnable.height + 2)
        layoutLabelInArea(x, y, w, h)
    }

    /**************************************************************************
     *
     * Private Implements
     *
     **************************************************************************/

    private fun skinnable(): MuiButton
            = skinnable as MuiButton

    private fun updateBackground(type: MuiButton.Type) {
        var background: Background? = null
        when(type) {
            MuiButton.Type.RAISED -> background = Background(BackgroundFill(Color.WHITE, defaultRadii, null))
            MuiButton.Type.FLAT -> background = Background(BackgroundFill(Color.TRANSPARENT, defaultRadii, null))
        }
        skinnable.background = background
    }

    private fun updateType(type: MuiButton.Type) {
        when(type) {
            MuiButton.Type.RAISED -> {
                MuiDepthUtils.setNodeDepth(muiButtonContainer, 1)
                depthAnimation = MuiButtonDepthTransition(muiButtonContainer, 1, 3, Duration.seconds(.1))
            }
            MuiButton.Type.FLAT -> {
                muiButtonContainer.effect = null
            }
        }
    }
}

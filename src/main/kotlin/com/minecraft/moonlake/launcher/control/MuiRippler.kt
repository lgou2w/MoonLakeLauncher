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

import com.minecraft.moonlake.launcher.animation.MuiCachedAnimation
import com.minecraft.moonlake.launcher.animation.MuiCachedTransition
import com.minecraft.moonlake.launcher.layout.MuiStackPane
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.DefaultProperty
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.Event
import javafx.geometry.Insets
import javafx.scene.CacheHint
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.util.Duration
import java.util.concurrent.atomic.AtomicBoolean

@DefaultProperty(value = "node")
open class MuiRippler: MuiStackPane {

    /**************************************************************************
     *
     * Enum Class
     *
     **************************************************************************/

    enum class RipplerMask {
        CIRCLE, RECT
    }

    enum class RipplerPosition {
        FRONT, BACK
    }

    /**************************************************************************
     *
     * Protected Member
     *
     **************************************************************************/

    protected var ripplerPane: MuiStackPane
    protected var node: Node? = null

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var enable: Boolean = true
    private var rippler: RippleGenerator
    private var rippleInterpolator: Interpolator = Interpolator.SPLINE(.0825, .3025, .0875, .9975)

    /**************************************************************************
     *
     * Static Block
     *
     **************************************************************************/

    companion object {
        /**
         * Rippler Max Radius = 300
         */
        private val MAX_RADIUS = 300.0
    }

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): this(null)
    constructor(node: Node? = null, maskType: RipplerMask = MuiRippler.RipplerMask.RECT, position: RipplerPosition = MuiRippler.RipplerPosition.FRONT): super() {
        styleClass.add("mui-rippler")
        this.maskType.set(maskType)
        this.position.set(position)
        rippler = RippleGenerator()
        ripplerPane = MuiStackPane()
        ripplerPane.children.add(rippler)
        setControl(node)
        isCache = true
        cacheHint = CacheHint.SPEED
        isCacheShape = true
        isSnapToPixel = false
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun setControl(node: Node?) {
        if(node != null) {
            this.node = node
            if(this.position.get() == RipplerPosition.BACK)
                ripplerPane.children.add(this.node)
            else
                children.add(this.node)
            this.position.addListener { _, _, _ -> run {
                if(this.position.get() == RipplerPosition.BACK)
                    ripplerPane.children.add(this.node)
                else
                    children.add(this.node)
            }}
            children.add(ripplerPane)
            initListeners()
            this.node!!.layoutBoundsProperty().addListener { _, _, _ -> run {
                resetOverlay()
                resetClip()
            }}
            this.node!!.boundsInParentProperty().addListener { _, _, _ -> run {
                resetOverlay()
                resetClip()
            }}
        }
    }

    fun getControl(): Node?
            = node

    fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    fun createManualRipple(): Runnable {
        rippler.setGeneratorCenterX(node!!.layoutBounds.width / 2)
        rippler.setGeneratorCenterY(node!!.layoutBounds.height / 2)
        return rippler.createManualRipple()
    }

    open protected fun getMask(): Node {
        val bounds = node!!.boundsInParent
        val width = node!!.layoutBounds.width
        val height = node!!.layoutBounds.height
        val borderWidth = if(ripplerPane.border != null) ripplerPane.border.insets.top else .0
        val diffMinX = Math.abs(node!!.boundsInLocal.minX - node!!.layoutBounds.minX)
        val diffMinY = Math.abs(node!!.boundsInLocal.minY - node!!.layoutBounds.minY)
        val diffMaxX = Math.abs(node!!.boundsInLocal.maxX - node!!.layoutBounds.maxX)
        val diffMaxY = Math.abs(node!!.boundsInLocal.maxY - node!!.layoutBounds.maxY)
        var mask: Node
        when(getMaskType()) {
            RipplerMask.RECT -> {
                mask = Rectangle(bounds.minX + diffMinX, bounds.minY + diffMinY, width - .1 - 2 * borderWidth, height - .1 - 2 * borderWidth)
            }
            RipplerMask.CIRCLE -> {
                val radius = Math.min(width / 2 - .1 - 2 * borderWidth, height / 2 - .1 - 2 * borderWidth)
                mask = Circle((bounds.minX + diffMinX + bounds.maxX - diffMaxX) / 2, (bounds.minY + diffMinY + bounds.maxY - diffMaxY) / 2, radius, Color.BLUE)
            }
        }
        if(node is Shape || (node is Region && (node as Region).shape != null)) {
            mask = MuiStackPane()
            mask.shape = if(node is Shape) node as Shape else (node as Region).shape
            mask.background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
            mask.resize(width, height)
            mask.relocate(bounds.minX + diffMinX, bounds.minY + diffMinY)
        }
        return mask
    }

    open protected fun initListeners() {
        ripplerPane.setOnMousePressed { event -> run {
            createRipple(event.x, event.y)
            if(position.get() == RipplerPosition.FRONT)
                node!!.fireEvent(event)
        }}
        ripplerPane.setOnMouseReleased { event -> run {
            if(position.get() == RipplerPosition.FRONT)
                node!!.fireEvent(event)
        }}
        ripplerPane.setOnMouseClicked { event -> run {
            if(position.get() == RipplerPosition.FRONT)
                node!!.fireEvent(event)
        }}
    }

    open protected fun createRipple(x: Double, y: Double) {
        rippler.setGeneratorCenterX(x)
        rippler.setGeneratorCenterY(y)
        rippler.createMouseRipple(x, y)
    }

    open protected fun computeRippleRadius(): Double {
        val width = node!!.layoutBounds.width * node!!.layoutBounds.width
        val height = node!!.layoutBounds.height * node!!.layoutBounds.height
        return Math.min(Math.sqrt(width + height), MAX_RADIUS) * 1.1 + 5
    }

    fun fireEventProgrammatically(event: Event) {
        if(!event.isConsumed)
            ripplerPane.fireEvent(event)
    }

    fun showOverlay() {
        rippler.showOverlay()
    }

    fun hideOverlay() {
        rippler.hideOverlay()
    }

    fun resetOverlay() {
        rippler.resetOverlay()
    }

    fun resetClip() {
        rippler.resetClip()
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var ripplerRecenter: BooleanProperty
            = SimpleBooleanProperty(false)

    fun ripplerRecenterProperty(): BooleanProperty
            = ripplerRecenter

    fun isRipplerRecenter(): Boolean
            = if(ripplerRecenter is BooleanProperty) ripplerRecenter.get() else false

    fun setripplerRecenter(ripplerRecenter: Boolean)
            = this.ripplerRecenter.set(ripplerRecenter)

    private var ripplerFill: ObjectProperty<Paint>
            = SimpleObjectProperty(Color.rgb(0, 0, 0, .1))

    fun ripplerFillProperty(): ObjectProperty<Paint>
            = ripplerFill

    fun getRipplerFill(): Paint
            = if(ripplerFill is ObjectProperty) ripplerFill.get() else Color.rgb(0, 0, 0, .1)

    fun setRipplerFill(ripplerFill: Paint)
            = this.ripplerFill.set(ripplerFill)

    private var ripplerRadius: ObjectProperty<Number>
            = SimpleObjectProperty(Region.USE_COMPUTED_SIZE)

    fun ripplerRadiusProperty(): ObjectProperty<Number>
            = ripplerRadius

    fun getRipplerRadius(): Number
            = if(ripplerRadius is ObjectProperty) ripplerRadius.get() else Region.USE_COMPUTED_SIZE

    fun setRipplerRadius(ripplerRadius: Number)
            = this.ripplerRadius.set(ripplerRadius)

    private var maskType: ObjectProperty<RipplerMask>
            = SimpleObjectProperty(RipplerMask.RECT)

    fun maskTypeProperty(): ObjectProperty<RipplerMask>
            = maskType

    fun getMaskType(): RipplerMask
            = if(maskType is ObjectProperty) maskType.get() else RipplerMask.RECT

    fun setMaskType(maskType: RipplerMask)
            = this.maskType.set(maskType)

    private var position: ObjectProperty<RipplerPosition>
            = SimpleObjectProperty(RipplerPosition.FRONT)

    fun positionProperty(): ObjectProperty<RipplerPosition>
            = position

    fun getPosition(): RipplerPosition
            = if(position is ObjectProperty) position.get() else RipplerPosition.FRONT

    fun setPosition(position: RipplerPosition)
            = this.position.set(position)

    /**************************************************************************
     *
     * Inner Class
     *
     **************************************************************************/

    internal inner class RippleGenerator internal constructor(): Group() {

        private var generatorCenterX = .0
        private var generatorCenterY = .0
        private var overlayRect: OverlayRipple? = null
        private var generating = AtomicBoolean(false)
        private var cacheRipplerClip = false
        private var resetClip = false

        init {
            isManaged = false
        }

        fun createMouseRipple(mouseX: Double, mouseY: Double) {
            if(enable && !generating.getAndSet(true)) {
                createOverlay()
                if(clip == null || (children.size == 1 && !cacheRipplerClip) || resetClip)
                    clip = getMask()
                resetClip = false
                val ripple = Ripple(generatorCenterX, generatorCenterY)
                children.add(ripple)
                overlayRect!!.outAnimation.stop()
                overlayRect!!.inAnimation.play()
                ripple.inAnimation!!.getAnimation().play()
                ripplerPane.setOnMouseReleased { _ -> run {
                    if(generating.getAndSet(false)) {
                        if(overlayRect != null)
                            overlayRect!!.inAnimation.stop()
                        ripple.inAnimation!!.getAnimation().stop()
                        ripple.outAnimation = MuiCachedAnimation(Timeline(KeyFrame(duration(scaleX, mouseX, mouseY), *ripple.outKeyValues)), this)
                        ripple.outAnimation!!.getAnimation().setOnFinished { _ -> children.remove(ripple) }
                        ripple.outAnimation!!.getAnimation().play()
                        if(overlayRect != null)
                            overlayRect!!.outAnimation.play()
                    }
                }}
            }
        }

        fun createManualRipple(): Runnable {
            if(enable && !generating.getAndSet(true)) {
                createOverlay()
                if(clip == null || (children.size == 1 && !cacheRipplerClip) || resetClip)
                    clip = getMask()
                resetClip = false
                val ripple = Ripple(generatorCenterX, generatorCenterY)
                children.add(ripple)
                overlayRect!!.outAnimation.stop()
                overlayRect!!.inAnimation.play()
                ripple.inAnimation!!.getAnimation().play()
                return Runnable {
                    if(generating.getAndSet(false)) {
                        if(overlayRect != null)
                            overlayRect!!.inAnimation.stop()
                        ripple.inAnimation!!.getAnimation().stop()
                        ripple.outAnimation = MuiCachedAnimation(Timeline(KeyFrame(duration(scaleX), *ripple.outKeyValues)), this)
                        ripple.outAnimation!!.getAnimation().setOnFinished { _ -> children.remove(ripple) }
                        ripple.outAnimation!!.getAnimation().play()
                        if(overlayRect != null)
                            overlayRect!!.outAnimation.play()
                    }
                }
            }
            return Runnable { }
        }

        private fun duration(scaleX: Double, mouseX: Double = -1.0, mouseY: Double = -1.0): Duration {
            if(mouseX < .0 || mouseY < .0) // not is mouse clicked
                return Duration(Math.min(1000.0, .9 * 1000 / scaleX))
            return Duration(Math.min(1000.0, .9 * 1000 / scaleX)) // mouse Clicked
        }

        fun showOverlay() {
            if(rippler.overlayRect != null)
                rippler.overlayRect!!.outAnimation.stop()
            rippler.createOverlay()
            rippler.overlayRect!!.inAnimation.play()
        }

        fun hideOverlay() {
            if(rippler.overlayRect != null)
                rippler.overlayRect!!.inAnimation.stop()
            if(rippler.overlayRect != null)
                rippler.overlayRect!!.outAnimation.play()
        }

        fun resetOverlay() {
            if(rippler.overlayRect != null) {
                val oldOverlay = rippler.overlayRect!!
                rippler.overlayRect!!.inAnimation.stop()
                rippler.overlayRect!!.outAnimation.setOnFinished { _ -> rippler.children.remove(oldOverlay) }
                rippler.overlayRect!!.outAnimation.play()
                rippler.overlayRect = null
            }
        }

        fun resetClip() {
            rippler.resetClip = true
        }

        fun cacheRippleClip(cached: Boolean) {
            cacheRipplerClip = cached
        }

        fun createOverlay() {
            if(overlayRect == null) {
                val fill = ripplerFill.get() as Color
                overlayRect = OverlayRipple()
                overlayRect!!.clip = getMask()
                overlayRect!!.fill = Color(fill.red, fill.green, fill.blue, .2)
                children.add(0, overlayRect)
            }
        }

        fun setGeneratorCenterX(generatorCenterX: Double) {
            this.generatorCenterX = generatorCenterX
        }

        fun setGeneratorCenterY(generatorCenterY: Double) {
            this.generatorCenterY = generatorCenterY
        }

        fun clear() {
            children.clear()
            generating.set(false)
        }

        /**************************************************************************
         *
         * Private Inner Class
         *
         **************************************************************************/

        private inner class OverlayRipple internal constructor(): Rectangle(node!!.layoutBounds.width - .1, node!!.layoutBounds.height - .1) {

            internal var inAnimation: MuiCachedTransition = object: MuiCachedTransition(this, Timeline(KeyFrame(Duration(1300.0), KeyValue(opacityProperty(), 1, Interpolator.EASE_IN)))) {
                init {
                    cycleDuration = Duration(3000.0)
                    delay = Duration(.0)
                }
            }
            internal var outAnimation: MuiCachedTransition = object: MuiCachedTransition(this, Timeline(KeyFrame(Duration(1300.0), KeyValue(opacityProperty(), 0, Interpolator.EASE_OUT)))) {
                init {
                    cycleDuration = Duration(0300.0)
                    delay = Duration(.0)
                }
            }

            init {
                val diffMinX = Math.abs(node!!.boundsInLocal.minX - node!!.layoutBounds.minX)
                val diffMinY = Math.abs(node!!.boundsInLocal.minY - node!!.layoutBounds.minY)
                val bounds = node!!.boundsInParent
                x = bounds.minX + diffMinX
                y = bounds.minY + diffMinY
                opacity = .0
                isCache = true
                cacheHint = CacheHint.SPEED
                isCacheShape = true
                isSnapToPixel = false
                outAnimation.setOnFinished { _ -> resetOverlay() }
            }
        }

        private fun radiusRippler(): Double {
            if(ripplerRadius.get().toDouble() == Region.USE_COMPUTED_SIZE)
                return computeRippleRadius()
            else
                return ripplerRadius.get().toDouble()
        }

        private inner class Ripple internal constructor(centerX: Double, centerY: Double) : Circle(centerX, centerY, radiusRippler()) {

            internal var outKeyValues: Array<KeyValue?>
            internal var outAnimation: MuiCachedAnimation? = null
            internal var inAnimation: MuiCachedAnimation? = null

            init {
                val isRipplerInterpolator = isRipplerRecenter()
                val inKeyValues = arrayOfNulls<KeyValue>(if(isRipplerInterpolator) 4 else 2)
                outKeyValues = arrayOfNulls<KeyValue>(if(isRipplerInterpolator) 5 else 3)
                inKeyValues[0] = KeyValue(scaleXProperty(), .9, rippleInterpolator)
                inKeyValues[1] = KeyValue(scaleYProperty(), .9, rippleInterpolator)
                outKeyValues[0] = KeyValue(scaleXProperty(), 1.0, rippleInterpolator)
                outKeyValues[1] = KeyValue(scaleYProperty(), 1.0, rippleInterpolator)
                outKeyValues[2] = KeyValue(opacityProperty(), .1, rippleInterpolator)

                if(isRipplerInterpolator) {
                    val dx = (node!!.layoutBounds.width / 2 - centerX) / 1.55
                    val dy = (node!!.layoutBounds.height / 2 - centerY) / 1.55
                    outKeyValues[3] = KeyValue(translateXProperty(), Math.signum(dx) * Math.min(Math.abs(dx), radius / 2), rippleInterpolator)
                    outKeyValues[4] = KeyValue(translateYProperty(), Math.signum(dy) * Math.min(Math.abs(dy), radius / 2), rippleInterpolator)
                    inKeyValues[2] = outKeyValues[3]
                    inKeyValues[3] = outKeyValues[4]
                }
                inAnimation = MuiCachedAnimation(Timeline(
                        KeyFrame(Duration.ZERO,
                                KeyValue(scaleXProperty(), .0, rippleInterpolator),
                                KeyValue(scaleYProperty(), .0, rippleInterpolator),
                                KeyValue(translateXProperty(), .0, rippleInterpolator),
                                KeyValue(translateYProperty(), .0, rippleInterpolator),
                                KeyValue(opacityProperty(), 1, rippleInterpolator)),
                        KeyFrame(Duration(900.0), *inKeyValues)), this)

                isCache = true
                cacheHint = CacheHint.SPEED
                isCacheShape = true
                isSnapToPixel = false
                scaleX = .0
                scaleY = .0

                if(ripplerFill.get() is Color) {
                    val ripplerFill = ripplerFill.get() as Color
                    val circleColor = Color(ripplerFill.red, ripplerFill.green, ripplerFill.blue, .2)
                    stroke = circleColor
                    fill = circleColor
                } else {
                    stroke = ripplerFill.get()
                    fill = ripplerFill.get()
                }
            }
        }
    }
}

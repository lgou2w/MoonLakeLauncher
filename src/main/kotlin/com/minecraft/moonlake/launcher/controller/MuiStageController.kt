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

package com.minecraft.moonlake.launcher.controller

import javafx.fxml.FXML
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.net.URL
import java.util.*

abstract class MuiStageController<S: Stage, out T: Pane>: MuiController<T>() {

    private var stage: S? = null

    protected fun stage(): S
            = stage!!

    /**
     * Set this controller stage instance
     */
    internal fun setStage(stage: S) { this.stage = stage }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        super.initialize(location, resources)
    }

    /**************************************************************************
     *
     * Navbar Mouse Event Handler
     *
     **************************************************************************/

    protected var navbarMouseX = .0
    protected var navbarMouseY = .0

    @FXML
    open fun onNavbarPressed(event: MouseEvent) {
        if(event.button == MouseButton.PRIMARY) {
            navbarMouseX = event.x
            navbarMouseY = event.y
            event.consume()
        }
    }

    @FXML
    open fun onNavbarDragged(event: MouseEvent) {
        if(event.button == MouseButton.PRIMARY) {
            stage().x = event.screenX - navbarMouseX
            stage().y = event.screenY - navbarMouseY
            event.consume()
        }
    }

    /**************************************************************************
     *
     * Window Control Button Event Handler
     *
     **************************************************************************/

    @FXML
    open fun onMinimizeClicked(event: MouseEvent) {
        stage().isIconified = true
        event.consume()
    }

    @FXML
    open fun onMaximizeClicked(event: MouseEvent) {
        stage().isMaximized = !stage().isMaximized
        event.consume()
    }

    @FXML
    open fun onCloseClicked(event: MouseEvent) {
        stage().close()
        event.consume()
    }
}

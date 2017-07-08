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

package com.minecraft.moonlake.launcher.util

import javafx.application.Platform
import javafx.scene.layout.Background
import javafx.scene.text.Font
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.util.concurrent.CountDownLatch

object MuiUtils {

    /**************************************************************************
     *
     * JavaFx Utils
     *
     **************************************************************************/

    @JvmStatic
    fun getRobotoFont(size: Double = 12.0): Font
            = Font("Roboto", size)

    @JvmStatic
    fun isDefaultBackground(background: Background): Boolean {
        try {
            val firstFill = background.fills[0].fill.toString()
            return "0xffffffba" == firstFill || "0xffffffbf" == firstFill || "0xffffffbd" == firstFill
        } catch (e: Exception) {
            return false
        }
    }

    @JvmStatic
    fun isDefaultClickedBackground(background: Background): Boolean {
        try {
            return "0x039ed3ff" == background.fills[0].fill.toString()
        } catch (e: Exception) {
            return false
        }
    }

    /**************************************************************************
     *
     * Thread Runnable Util
     *
     **************************************************************************/

    @JvmStatic
    fun runInFx(runnable: Runnable) {
        if(Platform.isFxApplicationThread()) {
            runnable.run()
            return
        }
        Platform.runLater(runnable)
    }

    @JvmStatic
    fun runInFxWait(runnable: Runnable) {
        if(Platform.isFxApplicationThread()) {
            runnable.run()
            return
        }
        val doneLatch = CountDownLatch(1)
        Platform.runLater({
            try {
                runnable.run()
            } finally {
                doneLatch.countDown()
            }
        })
        try {
            doneLatch.await()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    @JvmStatic
    fun runInFxLater(runnable: Runnable) {
        Platform.runLater(runnable)
    }

    /**************************************************************************
     *
     * Io Util
     *
     **************************************************************************/

    @JvmStatic
    fun makeDirectory(parent: File): Boolean
            = parent.isDirectory || parent.mkdirs()

    @JvmStatic
    fun closeable(vararg closeables: Closeable?) {
        try {
            closeables.forEach { it?.close() }
        } catch (e: IOException) {
        }
    }

    /**************************************************************************
     *
     * String Util
     *
     **************************************************************************/

    @JvmStatic
    internal val ROUNDING = DecimalFormat("#.00")

    @JvmStatic
    fun rounding(value: Double): String
            = ROUNDING.format(value)
}

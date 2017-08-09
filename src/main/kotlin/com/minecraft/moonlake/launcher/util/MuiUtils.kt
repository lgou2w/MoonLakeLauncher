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

import com.minecraft.moonlake.launcher.controller.MuiStageController
import com.minecraft.moonlake.launcher.controller.MuiController
import com.minecraft.moonlake.launcher.controller.MuiControllerUtils
import com.minecraft.moonlake.launcher.system.MuiJavaProcess
import javafx.application.Platform
import javafx.scene.layout.Background
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.util.concurrent.CountDownLatch
import java.util.function.Function
import java.nio.charset.Charset
import java.io.InputStreamReader
import java.io.BufferedReader

object MuiUtils {

    /**************************************************************************
     *
     * Static Member
     *
     **************************************************************************/

    val GBK = Charset.forName("gbk")
    val UTF_8 = Charset.forName("utf-8")

    /**************************************************************************
     *
     * JavaFx Utils
     *
     **************************************************************************/

    @JvmStatic
    fun <P: Pane, T: MuiController<P>> createMuiStage(controller: Class<T>, title: String? = null, stage: Stage? = null, stageStyle: StageStyle? = null): Stage {
        val window = stage ?: Stage()
        val scene = MuiControllerUtils.loadControllerScene(controller)
        window.initStyle(stageStyle ?: StageStyle.DECORATED)
        window.minHeight = scene.height
        window.minWidth = scene.width
        window.scene = scene
        window.title = title
        window.centerOnScreen()
        return window
    }

    @JvmStatic
    fun <S: Stage, P: Pane, T: MuiStageController<S, P>> createMuiStage(stage: S, controller: Class<T>, title: String? = null, stageStyle: StageStyle? = null): Stage {
        val scene = MuiControllerUtils.loadStageControllerScene(stage, controller)
        stage.initStyle(stageStyle ?: StageStyle.DECORATED)
        stage.minHeight = scene.height
        stage.minWidth = scene.width
        stage.scene = scene
        stage.title = title
        stage.centerOnScreen()
        return stage
    }

    @JvmStatic
    fun isDefaultBackground(background: Background): Boolean {
        try {
            val firstFill = background.fills[0].fill.toString()
            return "0xffffffba" == firstFill || "0xffffffbf" == firstFill || "0xffffffbd" == firstFill || "0xffffff12" == firstFill
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

    @JvmStatic
    fun readProcessInput(vararg cmd: String = arrayOf()): List<String>
            = readProcess0(cmd, null, Function { process -> process.inputStream })

    @JvmStatic
    fun readProcessError(vararg cmd: String = arrayOf()): List<String>
            = readProcess0(cmd, null, Function { process -> process.errorStream })

    @JvmStatic
    fun readProcessInput(charset: Charset? = null, vararg cmd: String = arrayOf()): List<String>
            = readProcess0(cmd, charset, Function { process -> process.inputStream })

    @JvmStatic
    fun readProcessError(charset: Charset? = null, vararg cmd: String = arrayOf()): List<String>
            = readProcess0(cmd, charset, Function { process -> process.errorStream })

    @JvmStatic
    private fun readProcess0(cmd: Array<out String>, charset: Charset? = null, callback: Function<Process, InputStream>): List<String> {
        val process = MuiJavaProcess(cmd, ProcessBuilder(*cmd).start())
        val lines: MutableList<String> = ArrayList()
        return BufferedReader(InputStreamReader(callback.apply(process.raw()), charset ?: Charset.defaultCharset())).use { br ->
            var line: String? = null
            while (br.readLine().apply { line = this } != null)
                lines.add(line!!)
            process.waitFor()
            lines
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

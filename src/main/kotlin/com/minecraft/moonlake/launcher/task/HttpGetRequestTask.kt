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

package com.minecraft.moonlake.launcher.task

import com.minecraft.moonlake.launcher.MoonLakeLauncher
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

open class HttpGetRequestTask: MuiTask<String> {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var url: String

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(url: String) : super() {
        this.url = url
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun getUrl(): String
            = url

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun onSucceeded(result: String) {
        updateProgress(1.0, 1.0)
        updateMessage("请求成功")
    }

    override fun failed() {
        updateProgress(.0, 1.0)
        updateMessage("请求失败")
    }

    override fun call(): String {
        MoonLakeLauncher.logger.info("[RequestTask] 开始请求: $url")
        val result: String?
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.useCaches = false
            connection.addRequestProperty("User-Agent", "MoonLake Launcher by lgou2w")
            connection.connect()
            val input = connection.inputStream
            val contentLength = connection.contentLength.toLong()
            val byteArrayOutput = ByteArrayOutputStream()
            val buffer: ByteArray = ByteArray(4096)
            var lastTime = System.currentTimeMillis()
            var read = 0L; var length = 0
            while(input.read(buffer).apply { length = this } != -1) {
                byteArrayOutput.write(buffer, 0, length)
                read += length
                val now = System.currentTimeMillis()
                if(now - lastTime >= 1000) {
                    updateProgress(read, contentLength)
                    lastTime = now
                }
            }
            result = byteArrayOutput.toString("utf-8")
        } catch (e: Exception) {
            throw e
        }
        return result ?: ""
    }
}

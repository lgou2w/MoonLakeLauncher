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
import com.minecraft.moonlake.launcher.util.MuiUtils
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

open class HttpFileDownloadTask : MuiTask<File> {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var url: String
    private var file: File
    private var downloaded: Long = 0L
    private var input: InputStream? = null
    private var access: RandomAccessFile? = null

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(url: String, file: File): super() {
        this.url = url
        this.file = file
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

    override fun onSucceeded(result: File) {
        updateProgress(1.0, 1.0)
        updateMessage("下载成功")
    }

    override fun failed() {
        updateProgress(.0, 1.0)
        updateMessage("下载失败")
    }

    override fun call(): File {
        MoonLakeLauncher.logger.info("[DownloadTask] 开始下载: $url -> $file")
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.addRequestProperty("User-Agent", "MoonLake Launcher by lgou2w")
            connection.connect()
            if(connection.responseCode / 100 != 2)
                throw IOException("请求的目标响应码不为 200, 当前: ${connection.responseCode}.")
            val contentLength = connection.contentLength.toLong()
            if(contentLength < 1)
                throw IOException("请求的目标内容长度无效.")
            if(!MuiUtils.makeDirectory(file.parentFile))
                throw IOException("无法创建目录文件.")
            val tmp = File(file.absolutePath + ".mldltmp")
            if(!tmp.exists())
                tmp.createNewFile()
            else if(!tmp.renameTo(tmp))
                throw IllegalStateException("临时文件处于锁状态, 请检测是否被其他进程占用.")
            downloaded = 0L
            input = connection.inputStream
            access = RandomAccessFile(tmp, "rw")
            var lastDownloaded = 0L
            var lastTime = System.currentTimeMillis()
            while (true) {
                val buffer: ByteArray = ByteArray(4096)
                val read = input!!.read(buffer)
                if(read == -1)
                    break
                access!!.write(buffer, 0, read)
                downloaded += read
                val now = System.currentTimeMillis()
                if(now - lastTime >= 1000) {
                    updateProgress(downloaded, contentLength)
                    updateMessage(formatDownloadSpeed(lastDownloaded))
                    lastDownloaded = downloaded
                    lastTime = now
                }
            }
            closeStream()
            if(file.exists())
                file.delete()
            tmp.renameTo(file)
            if(downloaded != contentLength)
                throw IllegalStateException("未完整的文件大小: $downloaded, 预期: $contentLength")
        } catch (e: Exception) {
            file.delete()
            throw e
        } finally {
            closeStream()
        }
        return file
    }

    /**************************************************************************
     *
     * Private Implements
     *
     **************************************************************************/

    private fun closeStream() {
        MuiUtils.closeable(access, input)
        access = null
        input = null
    }

    private fun formatDownloadSpeed(lastDownloaded: Long): String {
        val kilobyte = (downloaded - lastDownloaded) / 1024.0
        if(kilobyte <= 1024.0)
            return "下载速度: ${MuiUtils.rounding(kilobyte)}KB/s"
        return "下载速度: ${MuiUtils.rounding(kilobyte / 1024.0)}MB/s"
    }
}

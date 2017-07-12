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

package com.minecraft.moonlake.launcher

import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter

class MoonLakeLauncherLogger {

    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    private fun log(type: String, message: String)
            = println("[${dateFormat.format(Date())}] [${Thread.currentThread().name}/$type] [MoonLakeLauncher]: $message")

    fun debug(message: String)
            = log("DEBUG", message)
    fun info(message: String)
            = log("INFO", message)
    fun warn(message: String)
            = log("WARN", message)
    fun severe(message: String)
            = log("SEVERE", message)
}

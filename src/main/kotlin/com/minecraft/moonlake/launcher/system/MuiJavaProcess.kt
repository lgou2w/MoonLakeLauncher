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

package com.minecraft.moonlake.launcher.system

import com.minecraft.moonlake.launcher.api.MuiProcess

class MuiJavaProcess: MuiProcess {

    private val raw: Process
    private val commands: List<String>

    constructor(commands: Array<out String>, process: Process): this(commands.toList(), process)
    constructor(commands: List<String>, process: Process) {
        this.raw = process
        this.commands = commands
    }

    override fun startupCommands(): List<String>
            = commands

    override fun exitCode(): Int = try {
        raw.exitValue()
    } catch (e: IllegalThreadStateException) {
        throw e.fillInStackTrace()
    }

    override fun raw(): Process
            = raw
    override fun isRunning(): Boolean
            = raw.isAlive
    override fun stop()
            = raw.destroy()
    override fun waitFor(): Int
            = raw.waitFor()
    override fun toString(): String
            = "MuiJavaProcess{commands=$commands, isRunning=${isRunning()}}"
}

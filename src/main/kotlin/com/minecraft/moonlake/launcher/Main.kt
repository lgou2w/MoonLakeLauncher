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

@file:JvmName("Main")

package com.minecraft.moonlake.launcher

import com.google.gson.Gson
import com.minecraft.moonlake.launcher.mc.version.MinecraftVersion
import java.io.FileReader

/**************************************************************************
 *
 * Main Method
 *
 **************************************************************************/

fun main(args: Array<String>) {
    //MoonLakeLauncher.launch(args)

    val path = "C:\\Users\\MoonLake\\AppData\\Roaming\\.minecraft\\versions\\1.11.2\\1.11.2.json"
    val mcVer = Gson().fromJson(FileReader(path), MinecraftVersion::class.java)
    println(mcVer)
}

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

package com.minecraft.moonlake.launcher.platform

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory
import java.util.*

/**************************************************************************
 *
 * Platform Info Class
 *
 **************************************************************************/

data class PlatformInfo(val platform: Platform, val version: String, val bit: Platform.Bit, val totalMemory: Long)

/**************************************************************************
 *
 * Platform Enum
 *
 **************************************************************************/

enum class Platform(private val separator: Char, private val osName: String) {

    WINDOWS('\\', "Windows"),
    LINUX('/', "Linux"),
    OSX('/', "Mac OS X"),
    UNKNOWN('/', "Unknown")
    ;

    /**************************************************************************
     *
     * Internal Enum
     *
     **************************************************************************/

    enum class Bit {

        BIT32 {
            override fun toString(): String
                    = "32"
        },
        BIT64 {
            override fun toString(): String
                    = "64"
        },
        ;
    }

    /**************************************************************************
     *
     * Static
     *
     **************************************************************************/

    companion object {
        fun get(): Platform {
            val name = System.getProperty("os.name").toLowerCase(Locale.US)
            if(name.contains("windows"))
                return WINDOWS
            if(name.contains("linux"))
                return LINUX
            if(name.contains("unix"))
                return LINUX
            if(name.contains("solaris"))
                return LINUX
            if(name.contains("sunos"))
                return LINUX
            if(name.contains("mac"))
                return OSX
            return UNKNOWN
        }
        fun getBit(): Platform.Bit {
            return if(System.getProperty("os.arch").contains("64")) Bit.BIT64 else Bit.BIT32
        }
        fun getVersion(): String {
            return System.getProperty("os.version")
        }
        fun getTotalPhysicalMemorySize(): Long {
            val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
            return osBean.totalPhysicalMemorySize
        }
        fun getTotalPhysicalMemorySizeToMegabyte(): Int {
            val total = getTotalPhysicalMemorySize().toDouble()
            return Math.round((total / 1024 / 1024)).toInt()
        }
        fun getTotalPhysicalMemorySizeToGigabyte(): Int {
            val total = getTotalPhysicalMemorySize().toDouble()
            return Math.round((total / 1024 / 1024 / 1024)).toInt()
        }
        fun getInfo(): PlatformInfo
                = PlatformInfo(get(), getVersion(), getBit(), getTotalPhysicalMemorySize())
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun getSeparator(): Char
            = separator
    fun getOSName(): String
            = osName

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun toString(): String
            = osName
}

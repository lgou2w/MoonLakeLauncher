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

package com.minecraft.moonlake.launcher.mc.assets

import java.util.*

/**
 * Minecraft 版本资源 Json 对象
 */
data class AssetsIndex(var objects: MutableMap<String, AssetsObject>) {

    /**
     * objects : {
     *      "minecraft/sounds/mob/stray/death2.ogg": {
     *          "hash": "d48940aeab2d4068bd157e6810406c882503a813",
     *          "size": 18817
     *      },
     *      "minecraft/sounds/mob/husk/step4.ogg": {
     *          "hash": "70a1c99c314a134027988106a3b61b15389d5f2f",
     *          "size": 9398
     *      }
     * }
     */

    fun getObjects(): Set<AssetsObject>
            = if(objects is MutableMap) HashSet(objects.values) else Collections.emptySet()

    data class AssetsObject(
            var hash: String,
            var size: Int) {

        /**
         * hash : d48940aeab2d4068bd157e6810406c882503a813
         * size : 18817
         */

        @Throws(NullPointerException::class)
        fun getLocation(): String
                = if(hash is String) "${hash.substring(0, 2)}/$hash" else null!!
    }
}

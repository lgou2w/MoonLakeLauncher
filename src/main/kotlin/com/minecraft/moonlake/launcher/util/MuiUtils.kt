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

import javafx.scene.layout.Background
import javafx.scene.text.Font

class MuiUtils private constructor() {

    companion object {

        fun getRobotoFont(size: Double = 12.0): Font
                = Font("Roboto", size)

        fun isDefaultBackground(background: Background): Boolean {
            try {
                val firstFill = background.fills[0].fill.toString()
                return "0xffffffba" == firstFill || "0xffffffbf" == firstFill || "0xffffffbd" == firstFill
            } catch (e: Exception) {
                return false
            }
        }

        fun isDefaultClickedBackground(background: Background): Boolean {
            try {
                return "0x039ed3ff" == background.fills[0].fill.toString()
            } catch (e: Exception) {
                return false
            }
        }
    }
}

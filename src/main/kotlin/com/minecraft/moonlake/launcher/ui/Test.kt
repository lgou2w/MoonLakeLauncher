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

package com.minecraft.moonlake.launcher.ui

import com.google.gson.Gson
import com.minecraft.moonlake.launcher.annotation.MuiControllerFxml
import com.minecraft.moonlake.launcher.control.MuiButton
import com.minecraft.moonlake.launcher.controller.MuiController
import com.minecraft.moonlake.launcher.layout.MuiStackPane
import com.minecraft.moonlake.launcher.mc.download.MojangDownloadSource
import com.minecraft.moonlake.launcher.mc.version.MinecraftVersionList
import com.minecraft.moonlake.launcher.task.HttpGetRequestTask
import javafx.fxml.FXML
import java.net.URL
import java.util.ResourceBundle

@MuiControllerFxml(value = "fxml/Test.fxml", width = 600.0, height = 400.0)
class Test: MuiController<MuiStackPane>() {

    @FXML var test: MuiButton? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        super.initialize(location, resources)

        test!!.setOnMouseClicked {
            val provider = MojangDownloadSource()
            val task = object: HttpGetRequestTask(provider.getVersionListDownloadURL()) {
                override fun onSucceeded(result: String) {
                    super.onSucceeded(result)
                    println()
                    val mcVerList = Gson().fromJson(result, MinecraftVersionList::class.java)
                    mcVerList.versions.filter { it.isRelease() }.forEach { println(it) }
                }
            }
            Thread(task).start()

        }
    }
}

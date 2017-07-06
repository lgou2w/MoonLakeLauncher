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

package com.minecraft.moonlake.launcher.download

class MojangDownloadProvider: DownloadProvider {

    override fun getLibrariesDownloadURL(): String
            = "https://libraries.minecraft.net"

    override fun getVersionsDownloadURL(): String
            = "http://s3.amazonaws.com/Minecraft.Download/versions/"

    override fun getIndexesDownloadURL(): String
            = "http://s3.amazonaws.com/Minecraft.Download/indexes/"

    override fun getVersionListDownloadURL(): String
            = "https://launchermeta.mojang.com/mc/game/version_manifest.json"

    override fun getAssetsDownloadURL(): String
            = "http://resources.download.minecraft.net/"
}

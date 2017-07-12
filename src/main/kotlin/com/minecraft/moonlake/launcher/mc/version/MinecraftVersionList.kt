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

package com.minecraft.moonlake.launcher.mc.version

/**
 * 向 Minecraft 下载提供商请求得到的版本列表对象
 *
 * @see com.minecraft.moonlake.launcher.mc.download.DownloadSource
 */
data class MinecraftVersionList(
        var latest: Latest,
        var versions: MutableList<Version>) {

    /**
     * latest : {"snapshot":"1.12","release":"1.12"}
     * versions : [
     *      {
     *          "id":"1.12",
     *          "type":"release",
     *          "time":"2017-06-13T06:57:00+00:00",
     *          "releaseTime":"2017-06-02T13:50:27+00:00",
     *          "url":"https://launchermeta.mojang.com/mc/game/71579a8bd04a542218613d8015aaf386b84c6ba1/1.12.json"
     *      },
     *      {
     *          "id":"1.12-pre7",
     *          "type":"snapshot",
     *          "time":"2017-06-13T06:57:00+00:00",
     *          "releaseTime":"2017-05-31T10:56:41+00:00",
     *          "url":"https://launchermeta.mojang.com/mc/game/870bdfd6ea61ee9cccedab53b28650c68c9cb410/1.12-pre7.json"
     *      }
     * ]
     */

    data class Latest(
            var snapshot: String,
            var release: String) {

        /**
         * snapshot : 1.12
         * release : 1.12
         */
    }

    data class Version(
            var id: String,
            var type: String,
            var time: String,
            var releaseTime: String,
            var url: String) {

        /**
         * id : 1.12
         * type : release
         * time : 2017-06-13T06:57:00+00:00
         * releaseTime : 2017-06-02T13:50:27+00:00
         * url : https://launchermeta.mojang.com/mc/game/71579a8bd04a542218613d8015aaf386b84c6ba1/1.12.json
         */

        fun isRelease()
                = if(type is String) type.equals("release", true) else false
    }
}

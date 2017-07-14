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
 * Minecraft 版本 Json 对象
 */
data class MinecraftVersion(
        var assetIndex: AssetIndex,
        var assets: String,
        var downloads: Download,
        var id: String,
        var logging: Logging,
        var mainClass: String,
        var minecraftArguments: String,
        var minimumLauncherVersion: Int,
        var releaseTime: String,
        var time: String,
        var type: String,
        var libraries: MutableList<MinecraftLibrary>) {

    /**
     * assetIndex : {
     *      "id":"1.12",
     *      "sha1":"67e29e024e664064c1f04c728604f83c24cbc218",
     *      "size":169014,
     *      "url":"https://launchermeta.mojang.com/mc/assets/1.12/67e29e024e664064c1f04c728604f83c24cbc218/1.12.json",
     *      "totalSize":127037169
     * }
     * assets : 1.12
     * downloads : {
     *      "client":{
     *          "sha1":"909823f9c467f9934687f136bc95a667a0d19d7f",
     *          "size":10177098,
     *          "url":"https://launcher.mojang.com/mc/game/1.12/client/909823f9c467f9934687f136bc95a667a0d19d7f/client.jar"
     *      },
     *      "server":{
     *          "sha1":"8494e844e911ea0d63878f64da9dcc21f53a3463",
     *          "size":30202458,
     *          "url":"https://launcher.mojang.com/mc/game/1.12/server/8494e844e911ea0d63878f64da9dcc21f53a3463/server.jar"
     *      }
     * }
     * id : 1.12
     * libraries : is too long...
     * logging : {
     *      "client":{
     *          "file":{
     *              "id":"client-1.12.xml",
     *              "sha1":"ef4f57b922df243d0cef096efe808c72db042149",
     *              "size":877,
     *              "url":"https://launchermeta.mojang.com/mc/log_configs/client-1.12.xml/ef4f57b922df243d0cef096efe808c72db042149/client-1.12.xml"
     *          },
     *          "argument":"-Dlog4j.configurationFile=${path}",
     *          "type":"log4j2-xml"
     *      }
     * }
     * mainClass : net.minecraft.client.main.Main
     * minecraftArguments :
     *      --username ${auth_player_name}
     *      --version ${version_name}
     *      --gameDir ${game_directory}
     *      --assetsDir ${assets_root}
     *      --assetIndex ${assets_index_name}
     *      --uuid ${auth_uuid}
     *      --accessToken ${auth_access_token}
     *      --userType ${user_type}
     *      --versionType ${version_type}
     * minimumLauncherVersion : 18
     * releaseTime : 2017-06-02T13:50:27+00:00
     * time : 2017-06-13T06:57:00+00:00
     * type : release
     */

    data class AssetIndex(
            var id: String,
            var sha1: String,
            var size: Int,
            var url: String,
            var totalSize: Int) {

        /**
         * id : 1.12
         * sha1 : 67e29e024e664064c1f04c728604f83c24cbc218
         * size : 169014
         * url : https://launchermeta.mojang.com/mc/assets/1.12/67e29e024e664064c1f04c728604f83c24cbc218/1.12.json
         * totalSize : 127037169
         */
    }

    data class Download(
            var client: Client,
            var server: Server) {

        /**
         * client : {
         *      "sha1":"909823f9c467f9934687f136bc95a667a0d19d7f",
         *      "size":10177098,
         *      "url":"https://launcher.mojang.com/mc/game/.12/client/909823f9c467f9934687f136bc95a667a0d19d7f/client.jar"
         * }
         * server : {
         *      "sha1":"8494e844e911ea0d63878f64da9dcc21f53a3463",
         *      "size":30202458,
         *      "url":"https://launcher.mojang.com/mc/game/1.12/server/8494e844e911ea0d63878f64da9dcc21f53a3463/server.jar"
         * }
         */

        data class Client(
                var sha1: String,
                var size: Int,
                var url: String) {

            /**
             * sha1 : 909823f9c467f9934687f136bc95a667a0d19d7f
             * size : 10177098
             * url : https://launcher.mojang.com/mc/game/1.12/client/909823f9c467f9934687f136bc95a667a0d19d7f/client.jar
             */
        }

        data class Server(
                var sha1: String,
                var size: Int,
                var url: String) {

            /**
             * sha1 : 8494e844e911ea0d63878f64da9dcc21f53a3463
             * size : 30202458
             * url : https://launcher.mojang.com/mc/game/1.12/server/8494e844e911ea0d63878f64da9dcc21f53a3463/server.jar
             */
        }
    }

    data class Logging(var client: Client) {

        /**
         * client : {
         *      "file":{
         *      "id":"client-1.12.xml",
         *      "sha1":"ef4f57b922df243d0cef096efe808c72db042149",
         *      "size":877,
         *      "url":"https://launchermeta.mojang.com/mc/log_configs/client-1.12.xml/ef4f57b922df243d0cef096efe808c72db042149/client-1.12.xml"
         *  },
         *  "argument":"-Dlog4j.configurationFile=${path}",
         *  "type":"log4j2-xml"
         * }
         */

        data class Client(
                var file: File,
                var argument: String,
                var type: String) {

            /**
             * file : {
             *      "id":"client-1.12.xml",
             *      "sha1":"ef4f57b922df243d0cef096efe808c72db042149",
             *      "size":877,
             *      "url":"https://launchermeta.mojang.com/mc/log_configs/client-1.12.xml/ef4f57b922df243d0cef096efe808c72db042149/client-1.12.xml"
             * }
             * argument : -Dlog4j.configurationFile=${path}
             * type : log4j2-xml
             */

            data class File(
                    var id: String,
                    var sha1: String,
                    var size: Int,
                    var url: String) {

                /**
                 * id : client-1.12.xml
                 * sha1 : ef4f57b922df243d0cef096efe808c72db042149
                 * size : 877
                 * url : https://launchermeta.mojang.com/mc/log_configs/client-1.12.xml/ef4f57b922df243d0cef096efe808c72db042149/client-1.12.xml
                 */
            }
        }
    }
}

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

data class MinecraftLibrary(
        var name: String,
        var downloads: Download,
        var extract: Extract,
        var natives: Native,
        var rules: List<Rule>) {

    /**
     * name : com.mojang:patchy:1.1
     * downloads : {
     *      "artifact":{
     *          "size":15817,
     *          "sha1":"aef610b34a1be37fa851825f12372b78424d8903",
     *          "path":"com/mojang/patchy/1.1/patchy-1.1.jar",
     *          "url":"https://libraries.minecraft.net/com/mojang/patchy/1.1/patchy-1.1.jar"
     *      }
     * }
     * rules : [{
     *      "action":"allow"
     * },
     * {
     *      "action":"disallow",
     * "os":{
     *      "name":"osx"
     * }
     * }]
     * extract : {
     *      "exclude":[
     *          "META-INF/"
     *      ]
     * }
     * natives : {
     *      "linux":"natives-linux",
     *      "osx":"natives-osx",
     *      "windows":"natives-windows"
     * }
     */

    data class Download(var artifact: Artifact) {

        /**
         * artifact : {
         *      "size":15817,
         *      "sha1":"aef610b34a1be37fa851825f12372b78424d8903",
         *      "path":"com/mojang/patchy/1.1/patchy-1.1.jar",
         *      "url":"https://libraries.minecraft.net/com/mojang/patchy/1.1/patchy-1.1.jar"
         * }
         */

        data class Artifact(
                var size: Int,
                var sha1: String,
                var path: String,
                var url: String) {

            /**
             * size : 15817
             * sha1 : aef610b34a1be37fa851825f12372b78424d8903
             * path : com/mojang/patchy/1.1/patchy-1.1.jar
             * url : https://libraries.minecraft.net/com/mojang/patchy/1.1/patchy-1.1.jar
             */
        }
    }

    data class Extract(var exclude: List<String>)

    data class Native(
            var linux: String,
            var osx: String,
            var windows: String) {

        /**
         * linux : natives-linux
         * osx : natives-osx
         * windows : natives-windows
         */
    }

    data class Rule(
            var action: String,
            var os: Os) {

        /**
         * action : allow
         * os : {
         *      "name":"osx"
         * }
         */

        data class Os(var name: String) {

            /**
             * name : osx
             */
        }
    }
}
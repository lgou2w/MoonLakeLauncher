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

package com.minecraft.moonlake.launcher.svg

import com.minecraft.moonlake.launcher.MoonLakeLauncher
import com.minecraft.moonlake.launcher.util.ClassUtils
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

object MuiSVGGlyphLoader {

    @JvmStatic
    fun loadMuiSVG(resource: String, initSize: Double = -1.0, initFill: Paint? = null): MuiSVGGlyph? {
        val svg = loadMuiSVG(ClassUtils.loadResource(this::class.java, resource))
        if(initSize != -1.0)
            svg?.setSizeRatio(initSize)
        if(initFill != null)
            svg?.setFill(initFill)
        return svg
    }

    @JvmStatic
    fun loadMuiSVG(url: URL): MuiSVGGlyph? {
        var svg: MuiSVGGlyph? = null
        try {
            val svgFile = File(url.toURI())
            val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            builder.setEntityResolver { _, _ -> InputSource(StringReader("")) }
            val doc = builder.parse(svgFile)
            doc.documentElement.normalize()
            val svgPath = doc.getElementsByTagName("path").item(0).attributes
            val path = svgPath.getNamedItem("d").nodeValue
            val fillNode: Node? = svgPath.getNamedItem("fill")
            svg = MuiSVGGlyph(svgFile.nameWithoutExtension, path, if(fillNode != null) Color.valueOf(fillNode.nodeValue) else Color.BLACK)
        } catch (e: Exception) {
            MoonLakeLauncher.logger.error("加载 SVG 文件 '${url.path}' 时异常", e)
        } finally {
            return svg
        }
    }
}

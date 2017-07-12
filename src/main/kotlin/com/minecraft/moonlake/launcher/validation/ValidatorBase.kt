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

package com.minecraft.moonlake.launcher.validation

import javafx.beans.property.*
import javafx.css.PseudoClass
import javafx.scene.Node
import javafx.scene.Parent

abstract class ValidatorBase: Parent {

    /**************************************************************************
     *
     * Static
     *
     **************************************************************************/

    companion object {
        val PSEUDO_CLASS_ERROR = PseudoClass.getPseudoClass("error")!!
    }

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): super()
    constructor(message: String): super() {
        setMessage(message)
    }

    init {
        parentProperty().addListener { _, _, _ -> parentChanged() }
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun validate() {
        eval()
        onEval()
    }

    protected abstract fun eval()

    open protected fun onEval() {
        val srcNode = getSrcNode()
        if(hasErrors.get()) {
            srcNode!!.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, true)
        } else {
            srcNode!!.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, false)
        }
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    protected var srcNode: SimpleObjectProperty<Node>
             = SimpleObjectProperty()

    fun srcNodeProperty(): ObjectProperty<Node>
            = srcNode

    fun getSrcNode(): Node?
            = srcNode.get()

    fun setSrcNode(srcNode: Node)
            = this.srcNode.set(srcNode)

    protected var src: SimpleStringProperty = object: SimpleStringProperty() {
        override fun invalidated() {
            updateSrcNode()
        }
    }

    fun srcProperty(): StringProperty
            = src

    fun getSrc(): String
            = src.get()

    fun setSrc(src: String)
            = this.src.set(src)

    protected var hasErrors: ReadOnlyBooleanWrapper
            = ReadOnlyBooleanWrapper(false)

    fun hasErrorsProperty(): ReadOnlyBooleanProperty
            = hasErrors.readOnlyProperty

    fun getHasErrors(): Boolean
            = hasErrors.get()

    protected var message: SimpleStringProperty = object: SimpleStringProperty() {
        override fun invalidated() {
            updateSrcNode()
        }
    }

    fun messageProperty(): StringProperty
            = message

    fun getMessage(): String
            = message.get()

    fun setMessage(message: String)
            = this.message.set(message)

    protected var icon: SimpleObjectProperty<Node> = object: SimpleObjectProperty<Node>() {
        override fun invalidated() {
            updateSrcNode()
        }
    }

    fun iconProperty(): SimpleObjectProperty<Node>
            = icon

    fun getIcon(): Node?
            = icon.get()

    fun setIcon(icon: Node) {
        icon.styleClass.add("error-icon")
        this.icon.set(icon)
    }

    /**************************************************************************
     *
     * Private Implements
     *
     **************************************************************************/

    private fun parentChanged()
            = updateSrcNode()

    private fun updateSrcNode() {
        val parent = parent
        if(parent != null) {
            val node = parent.lookup(getSrc())
            srcNode.set(node)
        }
    }
}

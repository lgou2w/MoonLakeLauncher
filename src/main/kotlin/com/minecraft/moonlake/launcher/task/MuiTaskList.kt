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

package com.minecraft.moonlake.launcher.task

import java.util.*

open class MuiTaskList: Thread {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private val queue = Collections.synchronizedList(ArrayList<MuiTask<*>>())

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(): super(MuiTaskList::class.java.simpleName)
    constructor(name: String): super(name)

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun addTask(task: MuiTask<*>): Boolean
            = queue.add(task)

    fun isEmpty(): Boolean
            = queue.isEmpty()

    /**************************************************************************
     *
     * Overridable API
     *
     **************************************************************************/

    protected open fun onFinished() {}

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun run() {
        while(!queue.isEmpty())
            queue.removeAt(0).run()
        onFinished()
    }
}

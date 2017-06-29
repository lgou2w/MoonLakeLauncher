package com.minecraft.moonlake.launcher

import com.sun.javafx.application.LauncherImpl

fun main(args: Array<String>?) {
    LauncherImpl.launchApplication(MoonLakeLauncher::class.java, args)
}

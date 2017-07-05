package com.minecraft.moonlake.launcher.util

import com.minecraft.moonlake.launcher.annotation.MuiControllerFxml
import com.minecraft.moonlake.launcher.controller.MuiController
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane

object MuiControllerUtils {

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <P: Pane, T: MuiController<P>> loadControllerPane(clazz: Class<T>): P {
        val muiControllerFxml = clazz.getAnnotation(MuiControllerFxml::class.java)
        if(muiControllerFxml != null)
            return FXMLLoader.load<P>(clazz.classLoader.getResource(muiControllerFxml.value))
        throw IllegalArgumentException("参数控制器类没有被 MuiControllerFxml 注解.")
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <P: Pane, T: MuiController<P>> loadControllerScene(clazz: Class<T>): Scene {
        val muiControllerFxml = clazz.getAnnotation(MuiControllerFxml::class.java)
        if(muiControllerFxml != null && (muiControllerFxml.width > .0 || muiControllerFxml.height > .0))
            return Scene(FXMLLoader.load<P>(clazz.classLoader.getResource(muiControllerFxml.value)), muiControllerFxml.width, muiControllerFxml.height)
        else if(muiControllerFxml != null)
            return Scene(FXMLLoader.load<P>(clazz.classLoader.getResource(muiControllerFxml.value)))
        throw IllegalArgumentException("参数控制器类没有被 MuiControllerFxml 注解.")
    }
}

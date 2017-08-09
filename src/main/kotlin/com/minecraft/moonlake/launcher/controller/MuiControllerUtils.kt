package com.minecraft.moonlake.launcher.controller

import com.minecraft.moonlake.launcher.annotation.MuiControllerFXML
import javafx.fxml.FXMLLoader
import javafx.fxml.JavaFXBuilderFactory
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

object MuiControllerUtils {

    @JvmStatic
    private fun <P: Pane> setPanePrefSize(pane: P, width: Double, height: Double): P {
        pane.setPrefSize(width, height)
        return pane
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <P: Pane, T: MuiController<P>> loadControllerPane(clazz: Class<T>): P {
        val muiControllerFxml = clazz.getAnnotation(MuiControllerFXML::class.java)
        if(muiControllerFxml != null && (muiControllerFxml.width > .0 || muiControllerFxml.height > .0))
            return setPanePrefSize(FXMLLoader.load<P>(clazz.classLoader.getResource(muiControllerFxml.value)), muiControllerFxml.width, muiControllerFxml.height)
        else if(muiControllerFxml != null)
            return FXMLLoader.load<P>(clazz.classLoader.getResource(muiControllerFxml.value))
        throw IllegalArgumentException("参数控制器类没有被 MuiControllerFXML 注解.")
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <P: Pane, T: MuiController<P>> loadControllerScene(clazz: Class<T>): Scene
            = Scene(loadControllerPane(clazz))

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <S: Stage, P: Pane, T: MuiStageController<S, P>> loadStageControllerPane(stage: S, clazz: Class<T>): P {
        val muiControllerFxml = clazz.getAnnotation(MuiControllerFXML::class.java)
        if(muiControllerFxml != null) {
            val loader = FXMLLoader()
            loader.builderFactory = JavaFXBuilderFactory()
            loader.location = clazz.classLoader.getResource(muiControllerFxml.value)
            val pane = loader.load<P>()
            loader.getController<T>().setStage(stage)
            if(muiControllerFxml.width > .0 || muiControllerFxml.height > .0)
                return setPanePrefSize(pane, muiControllerFxml.width, muiControllerFxml.height)
            return pane
        }
        throw IllegalArgumentException("参数控制器类没有被 MuiControllerFXML 注解.")
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <S: Stage, P: Pane, T: MuiStageController<S, P>> loadStageControllerScene(stage: S, clazz: Class<T>): Scene
            = Scene(loadStageControllerPane(stage, clazz))
}

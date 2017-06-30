package com.minecraft.moonlake.launcher

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.text.Font
import javafx.stage.Stage

class MoonLakeLauncher: Application() {

    companion object {
        fun launch(args: Array<String>?) {
            if(args == null)
                throw NullPointerException("Null is args.")
            launch(MoonLakeLauncher::class.java, *args)
        }
    }

    override fun start(stage: Stage?) {
        val rootPane = BorderPane()
        val flowPane = FlowPane(10.0, 10.0)
        val font = Font("微软雅黑", 14.0)
        val label = Label("Welcome to MoonLake Launcher")
        val button = Button("click me!")
        label.font = font
        button.font = font
        button.onAction = EventHandler { showMessage("Hello, Welcome to MoonLake Launcher!", "Message:") }
        flowPane.alignment = Pos.CENTER
        flowPane.children.addAll(label, button)
        rootPane.center = flowPane
        stage?.scene = Scene(rootPane, 600.0, 400.0)
        stage?.title = "MoonLake Launcher"
        stage?.centerOnScreen()
        stage?.show()
    }

    fun showMessage(message: String?, title: String?) {
        val alert = Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK)
        alert.title = title
        alert.headerText = ""
        alert.dialogPane.graphic = null
        val buttonType = alert.showAndWait().get()
        println(buttonType)
    }
}

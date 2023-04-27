package com.scroogio.geometry

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.event.EventHandler
import kotlin.system.exitProcess

class HelloApplication(private val height: Double = 1080.0, private val width: Double = 1920.0) : Application() {

    private fun setStage(stage: Stage) {
        stage.title = "So colorful triangle"

        stage.minHeight = height / 10
        stage.minWidth = width / 10

        stage.maxHeight = height
        stage.maxWidth = width

        stage.height = height / 2
        stage.width = width / 2

        stage.onCloseRequest = EventHandler {
            Platform.exit()
            exitProcess(0)
        }
    }

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        setStage(stage)
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
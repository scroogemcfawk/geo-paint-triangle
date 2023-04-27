package com.scroogio.geometry

import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import java.util.LinkedList
import java.util.Queue
import kotlin.math.roundToInt

class MainController {
    @FXML
    private lateinit var cv: javafx.scene.canvas.Canvas
    private lateinit var gc: GraphicsContext

    @FXML
    private lateinit var sp: StackPane


    val MASK_A = 0xFF000000L
    val MASK_R = 0x00FF0000L
    val MASK_G = 0x0000FF00L
    val MASK_B = 0x000000FFL

    fun getA(c: Long): Long {
        return (c and MASK_A) shr 24
    }

    fun getR(c: Long): Long {
        return (c and MASK_R) shr 16
    }

    fun getG(c: Long): Long {
        return (c and MASK_G) shr 8
    }

    fun getB(c: Long): Long {
        return (c and MASK_B)
    }

    val colors = listOf("ff0000", "00ff00", "0000ff")
    val intColors = listOf(0xFF_FF00D4, 0xFF_00DDFF, 0xFF_E100FF)

    val q: Queue<Point2D> = LinkedList()

    @FXML
    fun initialize() {
        sp.style = "-fx-background-color: black;"
        gc = cv.graphicsContext2D
        cv.cursor = Cursor.CROSSHAIR
    }

    fun pml(e: MouseEvent) {
        q.add(Point2D(e.x, e.y))
        if (q.size > 3) {
            q.poll()
        }
        println(q)
        //        if (q.size == 3) {
        //            draw()
        //        }
        draw()
    }

    fun isIn(l1: Double, l2: Double, l3: Double): Boolean {
        if (l1 < 0.0 || 1.0 < l1) return false
        if (l2 < 0.0 || 1.0 < l2) return false
        return !(l3 < 0.0 || 1.0 < l3)
    }

    fun getScaledColor(l1: Double, l2: Double, l3: Double): Int {
        return (l1 * getA(intColors[0]) + l2 * getA(intColors[1]) + l3 * getA(intColors[2])).roundToInt().shl(24) or
                (l1 * getR(intColors[0]) + l2 * getR(intColors[1]) + l3 * getR(intColors[2])).roundToInt().shl(16) or
                (l1 * getG(intColors[0]) + l2 * getG(intColors[1]) + l3 * getG(intColors[2])).roundToInt().shl(8) or
                (l1 * getB(intColors[0]) + l2 * getB(intColors[1]) + l3 * getB(intColors[2])).roundToInt()
    }


    fun draw() {
        gc.clearRect(0.0, 0.0, cv.width, cv.height)
        if (q.size == 3) {
            val p1 = q.poll()
            val p2 = q.poll()
            val p3 = q.poll()

            val pw = gc.pixelWriter
            var c = 0
            for (y in 0 until cv.height.toInt()) {
                for (x in 0 until cv.width.toInt()) {
                    val detT = ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y))
                    val l1 = ((p2.y - p3.y) * (x - p3.x) + (p3.x - p2.x) * (y - p3.y)) / detT
                    val l2 = ((p3.y - p1.y) * (x - p3.x) + (p1.x - p3.x) * (y - p3.y)) / detT
                    val l3 = 1 - l1 - l2

                    if (isIn(l1, l2, l3)) {
                        ++c
                        pw.setArgb(x, y, getScaledColor(l1, l2, l3))
                    }
                }
            }
            gc.fill = Color.BLUE
            println("$c pixels painted")
        } else {
            val rad = 15.0
            for ((i, p) in q.withIndex()) {
                gc.fill = Paint.valueOf(colors[i])
                gc.fillOval(p.x - rad / 2, p.y - rad / 2, rad, rad)
            }
        }
    }
}
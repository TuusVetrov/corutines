package org.example.executors

import java.util.concurrent.Executors

fun main() {
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    repeat(100_000) {
        executor.execute {
            processImage(Image(it))
        }
    }
}

private fun processImage(image: Image) {
    println("Image $image is proccesing")
    Thread.sleep(1000)
    println("Image $image proccesed")
}

data class Image(val id: Int)


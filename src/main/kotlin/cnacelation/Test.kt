package org.example.cnacelation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
private val scope = CoroutineScope(dispatcher)

fun main() {
    val job =  scope.launch {
        timer()
    }
    Thread.sleep(3000)
    job.cancel()
}

private suspend fun timer() {
    var timer = 0
    while (true) {
        try {
           // ensureActive() // отмена корутины
            if (!coroutineContext.isActive) { // эквивалент ensureActive
                throw CancellationException()
            }
            println(timer++)
            Thread.sleep(1000)
        } catch (e: CancellationException) {
            throw e
        }
        catch (e: Exception) {

        }
    }
}
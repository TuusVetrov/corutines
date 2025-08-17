package org.example.supervisorjob

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

private val job = SupervisorJob()
private val executor = Executors.newCachedThreadPool().asCoroutineDispatcher()
private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    println("Exception cought")
}

private val scope = CoroutineScope(executor + job + exceptionHandler)

fun main() {

    scope.launch {
        longOperation(3000, 1)
        error("")
    }
    scope.launch {
        longOperation(4000, 2)
    }
}

private suspend fun longOperation(timeMillis: Long, number: Int) {
    println("Coroutine $number started")
    delay(timeMillis)
    println("Coroutine $number finished")
}
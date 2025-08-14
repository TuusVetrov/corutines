
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        println("Exeption cought")
    }

    val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    val scope = CoroutineScope(dispatcher)

    val deferred = scope.async {
        method()
    }
    scope.launch {
        async {
            deferred.await()
        }
    }
    scope.launch {
        method2()
    }
}

suspend fun method() {
    delay(3000)
    error("")
}

suspend fun method2() {
    delay(5000)
    print("method2")
}
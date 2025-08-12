package concurency

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.entities.Author
import org.example.entities.Book
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

import kotlin.concurrent.thread

object Display {
    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val scope = CoroutineScope(CoroutineName("my coroutine scope") + dispatcher)

    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    private val loadButton = JButton("Load button").apply {
        addActionListener {
            isEnabled = false
            infoArea.text = "Loading book...\n"

            val jobs = mutableListOf<Deferred<Book>>()
            repeat(10) {
                scope.async {
                    val book = loadBook()
                    infoArea.append("Book $it: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n\n")
                    book
                }.also { jobs.add(it) }
            }
            scope.launch {
                val books = jobs.awaitAll()
                books.forEach(::println)
                isEnabled = true
            }

        }
    }
    private val timerLabel = JLabel("Time: 00:00")
    private val topPanel = JPanel(BorderLayout()).apply {
        add(timerLabel, BorderLayout.WEST)
        add(loadButton, BorderLayout.EAST)
    }

    private val mainFrame = JFrame("Book and Author info").apply {
        layout = BorderLayout()
        add(topPanel, BorderLayout.NORTH)
        add(JScrollPane(infoArea), BorderLayout.CENTER)
        size = Dimension(400, 300)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                scope.cancel()
            }
        })

    }

    fun show() {
        mainFrame.isVisible = true
        startTimer()
    }

    private suspend fun loadBook(): Book {
        return withContext(Dispatchers.Default) {
            delay(3000)
            Book("1984", 1949, "Distopia")
        }

    }

    private suspend fun loadAuthor(): Author {
        delay(3000)
        return Author("George Orwell", "British writer and journalist")
    }

    private fun startTimer() {
        scope.launch {
            var totalSeconds = 0
            while (true) {
                val minutes = totalSeconds / 60
                val second = totalSeconds % 60
                timerLabel.text = String.format("Time %02d:%02d", minutes, second)
                delay(1000)
                totalSeconds++
            }
        }
    }
}
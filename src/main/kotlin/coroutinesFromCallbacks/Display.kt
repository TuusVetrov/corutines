package org.example.coroutinesFromCallbacks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.entities.Author
import org.example.entities.Book
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.border.Border
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

object Display {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    private val loadButton = JButton("Load button").apply {
        scope.launch {
            isEnabled = false

            val book = loadBook()
            infoArea.text = "Loading book...\n"
            infoArea.append("Book: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n")
            infoArea.append("Loading author...\n")
            val author = loadAuthor();

            infoArea.append("Author: ${author.name}\nBio: ${author.bio}\n")

            isEnabled = true
        }
        addActionListener {

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

    }

    fun show() {
        mainFrame.isVisible = true
        startTimer()
    }


    private suspend fun loadAuthor(): Author {
        return suspendCoroutine { continuation ->
            loadAuthor { author ->
                continuation.resumeWith(Result.success(author))
            }
        }
    }

    private suspend fun loadBook(): Book {
        return suspendCoroutine<Book> { continuation ->
            loadBook { book ->
                continuation.resumeWith(Result.success(book))
            }
        }
    }

    private fun loadBook(callback: (Book) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Book("1984", 1949, "Distopia"))
        }
    }

    private fun loadAuthor(callback: (Author) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Author("George Orwell", "British writer and journalist") )
        }
    }

    private fun startTimer() {
        thread {
            var totalSeconds = 0
            while (true) {
                val minutes = totalSeconds / 60
                val second = totalSeconds % 60
                timerLabel.text = String.format("Time %02d:%02d", minutes, second)
                Thread.sleep(1000)
                totalSeconds++
            }
        }
    }
 }
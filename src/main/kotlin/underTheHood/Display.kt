package org.example.underTheHood

import org.example.coroutines.Display
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

object Display {
    private val infoArea = JTextArea().apply {
        isEditable = false
    }

    private val loadButton = JButton("Load button").apply {
        addActionListener {
            loadData()
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

    fun loadData(state: Int = 0, data: Any? = null) {
        when(state) {
            0 -> {
                loadButton.isEnabled = false
                infoArea.text = "Loading book...\n"
                loadBook { loadData(1, it) }
            }
            1 -> {
                val book = data as Book
                infoArea.append("Book: ${book.title}\nYear: ${book.year}\nGenre: ${book.genre}\n")
                infoArea.append("Loading author...\n")
                loadAuthor(book) {
                    loadData(2, it)
                }
            }
            2 -> {
                val author = data as Author
                infoArea.append("Author: ${author.name}\nBio: ${author.bio}\n")
                loadButton.isEnabled = true
            }
        }
    }

    fun show() {
        mainFrame.isVisible = true
        startTimer()
    }

    private fun loadBook(callback: (Book) -> Unit) {
        thread {
            Thread.sleep(3000)
            callback(Book("1984", 1949, "Distopia"))
        }
    }

    private fun loadAuthor(book: Book, callback: (Author) -> Unit) {
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
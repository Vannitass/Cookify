package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder

/**
 * Activity, предоставляющая функционал чата с ботом.
 *
 * Бот отправляет запросы в Google, парсит ответы и отображает их в интерфейсе.
 */
class Chat : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var messageAdapter: ArrayAdapter<String>
    private val messages = mutableListOf<String>()

    /**
     * Метод, вызываемый при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности (если доступно).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Инициализация кнопок навигации между экранами
        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)

        // Переход на главную страницу
        imageButton1.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
        }

        // Остаемся в чате, если он уже открыт
        imageButton2.setOnClickListener {
            if (this !is Chat) {
                startActivity(Intent(this, Chat::class.java))
            }
        }

        // Переход на страницу профиля
        imageButton3.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        // Инициализация компонентов чата
        messageListView = findViewById(R.id.messageListView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.button_up)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messageListView.adapter = messageAdapter

        // Приветственное сообщение
        addMessage("Привет! Введите запрос")

        // Обработка нажатия кнопки "Отправить"
        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage("Вы: $userMessage")
                messageEditText.text.clear()
                searchGoogle(userMessage)
            }
        }
    }

    /**
     * Метод для добавления сообщений в чат.
     *
     * @param message Текст сообщения.
     */
    private fun addMessage(message: String) {
        messages.add(message)
        messageAdapter.notifyDataSetChanged()
        messageListView.setSelection(messages.size - 1)
    }

    /**
     * Метод для выполнения поиска в Google и парсинга результатов.
     *
     * @param query Запрос, введенный пользователем.
     */
    private fun searchGoogle(query: String) {
        val url = "https://www.google.com/search?q=${URLEncoder.encode(query, "UTF-8")}"

        // Показать индикатор загрузки
        loadingIndicator.visibility = ProgressBar.VISIBLE
        sendButton.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val html = response.body?.string()
                        if (html != null) {
                            val doc: Document = Jsoup.parse(html)
                            val smartAnswer = doc.selectFirst("div.BNeawe")?.text()
                            Log.d("HTML_LOG", "HTML Content:\n${doc.html()}")

                            withContext(Dispatchers.Main) {
                                if (smartAnswer != null) {
                                    addMessage("Ответ: $smartAnswer")
                                } else {
                                    addMessage("Бот: Не удалось найти ответ.")
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            addMessage("Бот: Ошибка соединения с Google.")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Ошибка сети: ${e.message}")
                withContext(Dispatchers.Main) {
                    addMessage("Бот: Произошла ошибка при попытке получить ответ.")
                }
            } finally {
                // Скрыть индикатор загрузки
                withContext(Dispatchers.Main) {
                    loadingIndicator.visibility = ProgressBar.GONE
                    sendButton.isEnabled = true
                }
            }
        }
    }
}

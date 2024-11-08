package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
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

class Chat : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var messageAdapter: ArrayAdapter<String>
    private val messages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Инициализация кнопок навигации
        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)

        imageButton1.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
        }

        imageButton2.setOnClickListener {
            // Избегаем перезапуска Chat, если он уже открыт
            if (this !is Chat) {
                startActivity(Intent(this, Chat::class.java))
            }
        }

        imageButton3.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        // Инициализация компонентов чата
        messageListView = findViewById(R.id.messageListView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.button_up)
        loadingIndicator = findViewById(R.id.loadingIndicator) // Индикатор загрузки

        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messageListView.adapter = messageAdapter

        // Приветственное сообщение
        addMessage("Привет! Введите запрос")

        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage("Вы: $userMessage") // Добавляем сообщение пользователя
                messageEditText.text.clear()
                searchYandex(userMessage) // Запускаем поиск
            }
        }
    }

    // Метод для добавления сообщений в список
    private fun addMessage(message: String) {
        messages.add(message)
        messageAdapter.notifyDataSetChanged()
        messageListView.setSelection(messages.size - 1)
    }

    // Функция для выполнения запроса в Яндекс и парсинга ответа
    private fun searchYandex(query: String) {
        // Кодирование запроса для корректной передачи в URL
        val url = "https://yandex.ru/search/?text=${URLEncoder.encode(query, "UTF-8")}"

        // Показываем индикатор загрузки и отключаем кнопку отправки
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
                            val smartAnswer = doc.selectFirst("div.OrganicTextContent")?.text()

                            // Отображаем ответ или сообщение об ошибке
                            withContext(Dispatchers.Main) {
                                if (smartAnswer != null) {
                                    addMessage("Ответ: $smartAnswer")
                                } else {
                                    addMessage("Бот: Не удалось найти интеллектуальный ответ.")
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            addMessage("Бот: Ошибка соединения с Яндексом.")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    addMessage("Бот: Произошла ошибка при попытке получить ответ.")
                }
            } finally {
                // Скрываем индикатор загрузки и включаем кнопку отправки
                withContext(Dispatchers.Main) {
                    loadingIndicator.visibility = ProgressBar.GONE
                    sendButton.isEnabled = true
                }
            }
        }
    }
}

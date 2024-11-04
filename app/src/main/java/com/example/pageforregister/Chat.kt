package com.example.pageforregister
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class Chat : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: ArrayAdapter<String>
    private val messages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageListView = findViewById(R.id.messageListView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.button_up)

        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messageListView.adapter = messageAdapter

        // Приветственное сообщение
        addMessage("Привет! Введите запрос")

        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage("Вы: $userMessage") // Добавляем сообщение пользователя
                messageEditText.text.clear()
                fetchNews()

            }
        }
    }

    // Метод для добавления сообщений в список
    private fun addMessage(message: String) {
        messages.add(message)
        messageAdapter.notifyDataSetChanged()
        messageListView.setSelection(messages.size - 1)
    }

    // Функция для парсинга новостей
    private fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = "https://ya.ru/" // Укажите реальный URL
                val document = Jsoup.connect(url).get()
                val articles = document.select("article") // Настройте на реальную структуру сайта

                // Парсим новости
                for (article in articles.take(3)) { // Ограничиваем количество статей
                    val title = article.select("h2").text()
                    val link = article.select("a").attr("href")

                    // Добавляем результат на основной поток для UI
                    CoroutineScope(Dispatchers.Main).launch {
                        addMessage("Новость: $title\nСсылка: $link")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    addMessage("Бот: Не удалось загрузить новости.")
                }
            }
        }
    }
}

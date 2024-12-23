package com.example.pageforregister.chat

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.Profile.Profile
import com.example.pageforregister.R
import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder
import java.util.Locale
/**
 * Класс `Chat` реализует экран чата в приложении.
 *
 * Особенности:
 * - Отображение сообщений в списке RecyclerView.
 * - Обработка пользовательских запросов и отправка их в Google Search.
 * - Отображение результатов поиска, полученных через парсинг HTML.
 * - Приветственное сообщение и управление состоянием кнопки отправки.
 * - Навигация между основными экранами приложения (главная страница, чат, профиль).
 */
@Suppress("DEPRECATION")
class Chat : AppCompatActivity() {

    private lateinit var itemsList: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var loadingIndicator: ProgressBar
    private val messages = mutableListOf<Message>() // Список теперь содержит объекты Message
    private lateinit var chatAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Инициализация кнопок навигации между экранами
        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)

        val intentAnim = Intent(this, MainPageActivity::class.java)

        // Переход на главную страницу
        imageButton1.setOnClickListener {
            startActivity(intentAnim)
            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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

            startActivity(intentAnim)

            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            startActivity(Intent(this, Profile::class.java))



        }

        // Инициализация RecyclerView
        itemsList = findViewById(R.id.messageListView)
        chatAdapter = MessageAdapter(this, messages) // Передаем список Message
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = chatAdapter

        // Инициализация других компонентов
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.button_up)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        // Приветственное сообщение
        addMessage("Привет! Введите запрос", false)

        // Инициализация кнопки отправки, чтобы она была неактивна, если поле ввода пусто
        sendButton.isEnabled = false
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                sendButton.isEnabled = editable.toString().isNotEmpty()
            }
        })

        // Обработка нажатия кнопки "Отправить"
        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage("Вы: $userMessage", true)
                messageEditText.text.clear()
                searchGoogle(userMessage)
            }
        }
    }
    /**
     * Добавляет новое сообщение в чат.
     *
     * @param text Текст сообщения.
     * @param isSent Указывает, отправлено ли сообщение пользователем (true) или получено от бота (false).
     */
    private fun addMessage(text: String, isSent: Boolean) {
        val calendar = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = timeFormat.format(calendar.time) // Примерное время, можно добавить текущее время
        val message = Message(text, currentTime, isSent) // Создаем объект Message
        messages.add(message)
        chatAdapter.notifyItemInserted(messages.size - 1)
        itemsList.scrollToPosition(messages.size - 1)
    }
    /**
     * Выполняет поиск в Google с использованием введённого пользователем запроса.
     *
     * @param query Текст запроса.
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
                                    addMessage("Ответ: $smartAnswer", false)
                                } else {
                                    addMessage("Бот: Не удалось найти ответ.", false)
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            addMessage("Бот: Ошибка соединения с Google.", false)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Ошибка сети: ${e.message}")
                withContext(Dispatchers.Main) {
                    addMessage("Бот: Произошла ошибка при попытке получить ответ.", false)
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

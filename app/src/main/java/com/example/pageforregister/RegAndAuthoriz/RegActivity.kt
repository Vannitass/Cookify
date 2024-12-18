package com.example.pageforregister.RegAndAuthoriz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.networkapi.RetrofitInstance
import android.util.Log
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.R
import kotlinx.coroutines.*

/**
 * Activity для регистрации пользователей.
 *
 * Эта активность предоставляет интерфейс для ввода логина, email и пароля,
 * а также отправляет эти данные на сервер для регистрации.
 */
class RegActivity : AppCompatActivity() {

    /**
     * Метод, вызываемый при создании активности.
     * Регистрирует нового пользователя в системе.
     * @param savedInstanceState Сохраненное состояние активности (если доступно).
     * @param username имя пользователя.
     * @param email электронная почта.
     * @param password хэшированный пароль.
     * @return `true`, если регистрация прошла успешно, иначе `false`.
     * @throws SQLiteException если произошла ошибка работы с базой данных.
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPass: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        linkToAuth.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        //обработчик событий при нажатии на кнопку
        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                // Запуск корутины для выполнения сетевого запроса в фоновом потоке
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = RetrofitInstance.api.greet("Registr", login, email, pass).execute()

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val responseData = response.body()

                                if (responseData?.status == "success") {
                                Toast.makeText(this@RegActivity, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()

                                // Очистка полей ввода
                                userLogin.text.clear()
                                userEmail.text.clear()
                                userPass.text.clear()

                                startActivity(Intent(this@RegActivity, AuthActivity::class.java))
                                }

                            } else {
                                Toast.makeText(this@RegActivity, "Ошибка регистрации. Попробуйте позже.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка сети: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegActivity, "Ошибка сети. Проверьте подключение к интернету.", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                // Логирование данных для отладки
                Log.d("MyTag", "Login: $login, Email: $email, Password: $pass")
            }
        }

    }
}

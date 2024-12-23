package com.example.pageforregister.RegAndAuthoriz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.networkapi.RetrofitInstance
import kotlinx.coroutines.*
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.R

/**
 * Activity для авторизации пользователей.
 *
 * Эта активность предоставляет интерфейс для ввода логина и пароля,
 * а также отправки этих данных на сервер для авторизации.
 */
class AuthActivity : AppCompatActivity() {

    /**
     * Метод, вызываемый при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности (если доступно).
     * @param username имя пользователя.
     * @param password пароль.
     * @return `User` объект, если пользователь найден, иначе `null`.
     * @throws SQLiteException если произошла ошибка работы с базой данных.
     */

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim() //trim() удаляет все лишние пробелы
            val pass = userPass.text.toString().trim()

            if (login == "" || pass == "") //если одно из полей пустое
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {   //регистрация пользователя
                GlobalScope.launch(Dispatchers.IO) { //конструкция которая позволяет делать запрос к бд в фоновом потоке не мешая основному отресовывать
                    try {
                        val response = RetrofitInstance.api.greet("Auth", login, "", pass).execute()

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val responseData = response.body()

                                if (responseData?.status == "success") {
                                    Toast.makeText(this@AuthActivity, "Пользователь $login авторизован", Toast.LENGTH_LONG).show()

                                    val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("username", login)
                                    editor.apply()
                                    Log.d("AuthActivity", "User name saved: $login")

                                    userLogin.text.clear()
                                    userPass.text.clear()


                                    // Переход на главную страницу при успешной авторизации
                                    startActivity(Intent(this@AuthActivity, MainPageActivity::class.java))
                                } else {
                                    Toast.makeText(this@AuthActivity, "Пользователь $login не авторизован!", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Log.e("Error", "Response failed: ${response.errorBody()?.string()}")
                                Toast.makeText(this@AuthActivity, "Ошибка сервера. Попробуйте позже.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Exception", "Request error: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AuthActivity, "Ошибка подключения. Проверьте интернет.", Toast.LENGTH_LONG).show()
                        }
                    }
                }



            }
        }
    }
}

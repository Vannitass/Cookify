package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.networkapi.RetrofitInstance
import java.util.concurrent.Executors

class AuthActivity : AppCompatActivity() {
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
            val pass = userLogin.text.toString().trim()

            if (login == "" || pass == "") //если одно из полей пустое
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {   //регистрация пользователя
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, pass)

                if (isAuth) {
                    Executors.newSingleThreadExecutor().execute {
                        val x = RetrofitInstance.api.greet("").execute()
                    }
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_LONG)
                        .show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    // переход на главную страничку при авторизации
                    val intent = Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                } else
                    Toast.makeText(this, "Пользователь $login не авторизован!", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
}

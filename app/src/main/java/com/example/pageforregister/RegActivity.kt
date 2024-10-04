package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegActivity : AppCompatActivity() {
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
            val login = userLogin.text.toString().trim() //trim() удаляет все лишние пробелы
            val email = userLogin.text.toString().trim()
            val pass = userLogin.text.toString().trim()

            if(login == "" || email == "" || pass == "") //если одно из полей пустое
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else{   //регистрация пользователя
                val user = User(login, email, pass) // создание объекта, который основан на классе User

                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()

                userLogin.text.clear()
                userEmail.text.clear()
                userPass.text.clear()
            }
        }
    }
}
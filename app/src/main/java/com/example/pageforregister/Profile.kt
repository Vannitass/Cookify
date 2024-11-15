package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.widget.Button
import android.widget.TextView
import com.example.pageforregister.MainPageActivity.Companion.NEW_POST_REQUEST_CODE


class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)

        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)

        imageButton1.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        imageButton2.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }

        imageButton3.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        // Получаем доступ к SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userLogin = sharedPreferences.getString("user_login", "Гость")

        // Находим TextView и устанавливаем текст с логином пользователя
        val userLoginTextView: TextView = findViewById(R.id.username)
        userLoginTextView.text = "$userLogin"

        val addRecipeButton: Button = findViewById(R.id.publishButton)
        addRecipeButton.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivityForResult(intent, NEW_POST_REQUEST_CODE)
        }

    }
}

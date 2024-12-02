package com.example.pageforregister.Profile


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.chat.Chat
import com.example.pageforregister.MainPage.MainPageActivity.Companion.NEW_POST_REQUEST_CODE
import com.example.pageforregister.R


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

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Default Username") // Если нет данных, покажет "Default Username"
        // Отображение имени пользователя на экране
        findViewById<TextView>(R.id.username).text = username
        Log.d("ProfileActivity", "Username loaded: $username")

        val addRecipeButton: Button = findViewById(R.id.publishButton)
        addRecipeButton.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivityForResult(intent, NEW_POST_REQUEST_CODE)
        }
    }
}

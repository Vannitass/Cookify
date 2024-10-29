package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


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

    }
}

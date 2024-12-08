package com.example.pageforregister.MainPage

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.R
import com.squareup.picasso.Picasso

class Card : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val content = intent.getStringExtra("content")
        val author = intent.getStringExtra("author")
        val imagePath = intent.getStringExtra("imagePath")

        val titleTextView = findViewById<TextView>(R.id.recipeTitle)
        val descriptionTextView = findViewById<TextView>(R.id.cardDescription)
        val authorTextView = findViewById<TextView>(R.id.profileName)
        val imageView = findViewById<ImageView>(R.id.imageView)

        titleTextView.text = title
        descriptionTextView.text = description
        authorTextView.text = "Автор: $author"

        // Используем Picasso для загрузки изображения
        if (imagePath != null) {
            Picasso.get().load(Uri.parse(imagePath)).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image) // Добавьте изображение-заглушку
        }
    }
}

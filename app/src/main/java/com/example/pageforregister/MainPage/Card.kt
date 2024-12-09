package com.example.pageforregister.MainPage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.R
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class Card : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card)

        // Получаем данные из Intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val author = intent.getStringExtra("author")
        val imagePath = intent.getStringExtra("imagePath")

        // Инициализация элементов представления
        val titleTextView = findViewById<TextView>(R.id.recipeTitle)
        val descriptionTextView = findViewById<TextView>(R.id.cardDescription)
        val authorTextView = findViewById<TextView>(R.id.profileName)
        val imageView = findViewById<ImageView>(R.id.imageView)


        // Инициализация кнопок навигации между экранами
        val buttonBack: ImageButton = this.findViewById(R.id.backButton)


        // Анимация
        val intentAnim = Intent(this, MainPageActivity::class.java)





        // Устанавливаем текстовые значения
        titleTextView.text = title
        descriptionTextView.text = description
        authorTextView.text = "Автор: $author"

        // Переход на главную страницу
        buttonBack.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
            // Указываем входящую и исходящую анимации
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Используем Picasso для загрузки изображения
        if (imagePath != null) {
            Picasso.get().load(Uri.parse(imagePath)).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.placeholder_image) // Добавьте изображение-заглушку
        }
    }
}


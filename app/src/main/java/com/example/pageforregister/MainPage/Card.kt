package com.example.pageforregister.MainPage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.Profile.Profile
import com.example.pageforregister.R
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class Card : AppCompatActivity() {

    companion object {
        var inpage_before: Int = 1
        fun set_page_before(inpage_before: Int) {
            this.inpage_before = inpage_before
        }

    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card)

        // Получаем данные из Intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val author = intent.getStringExtra("author")
        val imagePath = intent.getStringExtra("imagePath")
        val timeCook = intent.getStringExtra("timeCook")
        val countPortions = intent.getStringExtra("countPortions")
        val ingredients = intent.getStringExtra("ingredients")

        // Инициализация элементов представления
        val titleTextView = findViewById<TextView>(R.id.recipeTitle)
        val descriptionTextView = findViewById<TextView>(R.id.cardDescription)
        val authorTextView = findViewById<TextView>(R.id.profileName)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val timeCookView = findViewById<TextView>(R.id.cardTime)
        val countPortionsView = findViewById<TextView>(R.id.cardCount)
        val ingredientsView = findViewById<TextView>(R.id.cardIngridients)

        // Инициализация кнопок навигации между экранами
        val buttonBack: ImageButton = this.findViewById(R.id.backButton)


        // Анимация
        val intentAnim = Intent(this, MainPageActivity::class.java)


        // Устанавливаем текстовые значения
        titleTextView.text = title
        descriptionTextView.text = description
        authorTextView.text = "Автор: $author"

        timeCookView.text = "$timeCook (мин)"
        countPortionsView.text = "$countPortions."
        ingredientsView.text = "$ingredients."


        // Переход на главную страницу
        buttonBack.setOnClickListener {
            if(Card.inpage_before == 1){
                startActivity(Intent(this, MainPageActivity::class.java))}
            if(Card.inpage_before == 2){
                startActivity(Intent(this, Profile::class.java))}
            // Указываем входящую и исходящую анимации
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Используем Picasso для загрузки изображения
        if (imagePath != null) {
            Picasso.get().load(Uri.parse(imagePath)).into(imageView)
        }
    }
}

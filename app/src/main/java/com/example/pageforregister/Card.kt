package com.example.pageforregister

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.widget.EditText
import android.widget.ImageView


class Card : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.card)
//
//        // Обработчик нажатия на кнопку "Назад"
//        val backButton = findViewById<Button>(R.id.backButton)
//        backButton.setOnClickListener {
//            onBackPressed()  // Вызов стандартного метода возврата на предыдущую страницу
//        }
        setContentView(R.layout.card)




    }

//    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.",
//        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
//    )
//    override fun onBackPressed() {
//        super.onBackPressed()
//        // Здесь может быть дополнительная логика (например, сохранение состояния)
//    }
}

// NewPostActivity.kt
package com.example.pageforregister

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class NewPostActivity : AppCompatActivity() {

    private lateinit var dishImage: ImageView
    private lateinit var dishTitle: EditText
    private lateinit var dishDescription: EditText
    private lateinit var dishIngredients: EditText
    private lateinit var dishCookingTime: EditText
    private lateinit var dishServings: EditText
    private lateinit var stepsList: MutableList<Step>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        dishImage = findViewById(R.id.image_preview)
        dishTitle = findViewById(R.id.edit_dish_name)
        dishDescription = findViewById(R.id.edit_dish_description)
        dishIngredients = findViewById(R.id.edit_dish_ingredients)
        dishCookingTime = findViewById(R.id.edit_dish_time)
        dishServings = findViewById(R.id.edit_dish_servings)
        stepsList = mutableListOf()

        val postButton: Button = findViewById(R.id.btn_publish)
        postButton.setOnClickListener { createNewPost() }
    }

    private fun createNewPost() {
        val title = dishTitle.text.toString()
        val description = dishDescription.text.toString()
        val ingredients = dishIngredients.text.toString()
        val cookingTime = dishCookingTime.text.toString().takeIf { it.isNotEmpty() }
        val servings = dishServings.text.toString().toIntOrNull()

        // Собираем новый рецепт (публикацию)
        val newItem = Item(
            id = (0..1000).random(),  // временный случайный ID
            image = "",  // В реальном приложении, это будет путь к URI изображения
            title = title,
            description = description,
            cookingTime = cookingTime,
            servings = servings,
            ingredients = ingredients,
            steps = stepsList,
            author = "Текущий Пользователь"
        )

        val resultIntent = Intent().apply {
            putExtra("newItem", newItem)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

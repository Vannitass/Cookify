package com.example.pageforregister.MainPage

import RecipeAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.Profile.Profile
import com.example.pageforregister.R
import com.example.pageforregister.chat.Chat
import com.example.pageforregister.networkapi.RetrofitInstance
import com.squareup.picasso.Picasso

import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


@Suppress("DEPRECATION")
class MainPageActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)
        val intentAnim = Intent(this, MainPageActivity::class.java)


        val imageButton1 = findViewById<AppCompatImageButton>(R.id.button1)
        val imageButton2 = findViewById<AppCompatImageButton>(R.id.button2)
        val imageButton3 = findViewById<AppCompatImageButton>(R.id.button3)

        val search: EditText = findViewById(R.id.search)




        imageButton1.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            if (this !is MainPageActivity) {
                startActivity(intent)
            }
        }

        imageButton2.setOnClickListener {

            startActivity(intentAnim)

            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            val intent = Intent(this, Chat::class.java)
            startActivity(intent)



        }

        imageButton3.setOnClickListener {

            startActivity(intentAnim)

            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            val intent = Intent(this, Profile::class.java)
            startActivity(intent)



        }




        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.itemsList)
        recipeAdapter = RecipeAdapter(mutableListOf()) // Начальный пустой список
        recyclerView.adapter = recipeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)



        val searchButton: ImageButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            val query = search.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchRecipes(query) // Передаем текст запроса в метод загрузки рецептов
            } else {
                Toast.makeText(this, "Введите текст для поиска", Toast.LENGTH_SHORT).show()
            }
        }



        handler = Handler(mainLooper) // Создаем handler для выполнения задач в основном потоке
        runnable = object : Runnable {
            override fun run() {
                val query = search.text.toString().trim()

                // Если поле ввода пустое, отправляем пустой запрос
                if (query.isEmpty()) {
                    fetchRecipes("") // Пустой запрос
                }

                // Повторяем задачу через 2 секунды
                handler.postDelayed(this, 10000)
            }
        }

        // Запускаем периодический запрос
        handler.post(runnable)
    }

    private fun fetchRecipes(query:String) {
        // метод выгрузки из базы данных
        RetrofitInstance.api.getRecipes(query).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    val recipes = recipeResponse?.recipes
                    recipes?.let {
                        Log.d("Recipes", "Загруженные рецепты: $it")
                        recipeAdapter.updateRecipes(it) // Обновляем адаптер
                    }
                } else {
                    Log.e("Error", "Ошибка сервера: ${response.message()}")
                    Toast.makeText(this@MainPageActivity, "Ошибка загрузки рецептов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("Error", "Ошибка подключения: ${t.message}")
                Toast.makeText(this@MainPageActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        const val NEW_POST_REQUEST_CODE = 1001
    }
}

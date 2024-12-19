package com.example.pageforregister.Profile


import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.MainPage.Card
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.chat.Chat
import com.example.pageforregister.MainPage.MainPageActivity.Companion.NEW_POST_REQUEST_CODE
import com.example.pageforregister.MainPage.RecipeResponse
import com.example.pageforregister.R
import com.example.pageforregister.networkapi.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class Profile : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)

        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)
        val intentAnim = Intent(this, MainPageActivity::class.java)

        imageButton1.setOnClickListener {

            startActivity(intentAnim)
            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)


        }

        imageButton2.setOnClickListener {
            startActivity(intentAnim)

            // Указываем входящую и исходящую анимации:
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            val intent = Intent(this, Chat::class.java)
            startActivity(intent)

        }

        imageButton3.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            if(this !is Profile) {
                startActivity(intent)
            }
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

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.itemsList)
        recipeAdapter = RecipeAdapter(mutableListOf()) // Начальный пустой список
        recyclerView.adapter = recipeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Загрузка рецептов с сервера
        fetchRecipes()
        Card.set_page_before(2)

    }

    private fun fetchRecipes() {
        // метод выгрузки из базы данных
        RetrofitInstance.api.getRecipes().enqueue(object : Callback<RecipeResponse> {
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
                    Toast.makeText(this@Profile, "Ошибка загрузки рецептов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("Error", "Ошибка подключения: ${t.message}")
                Toast.makeText(this@Profile, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

package com.example.pageforregister.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pageforregister.MainPage.MainPageActivity
import com.example.pageforregister.R
import com.example.pageforregister.networkapi.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
 * `NewPostActivity` — Activity для создания нового поста/рецепта.
 *
 * Функциональность:
 * - Выбор изображения из галереи.
 * - Ввод заголовка, описания и содержимого рецепта.
 * - Загрузка рецепта на сервер.
 */
class NewPostActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dishTimeEditText: EditText
    private lateinit var dishServingsEditText: EditText
    private lateinit var dishIngredientsEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var chooseImageButton: Button
    private var selectedImageUri: Uri? = null

    /**
     * Инициализация Activity. Устанавливает макет, находит элементы интерфейса и назначает обработчики событий.
     *
     * @param savedInstanceState сохранённое состояние Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        imagePreview = findViewById(R.id.image_preview)
        titleEditText = findViewById(R.id.edit_dish_name)
        descriptionEditText = findViewById(R.id.edit_dish_description)

        dishTimeEditText = findViewById(R.id.edit_dish_time)
        dishServingsEditText = findViewById(R.id.edit_dish_servings)
        dishIngredientsEditText = findViewById(R.id.edit_dish_ingredients)

        uploadButton = findViewById(R.id.btn_publish)
        chooseImageButton = findViewById(R.id.btn_choose_photo)
        val intentAnim = Intent(this, MainPageActivity::class.java)
        val buttonBack: ImageButton = this.findViewById(R.id.backButton)


        buttonBack.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
            // Указываем входящую и исходящую анимации
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Обработчик выбора изображения
        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        // Обработчик кнопки загрузки рецепта
        uploadButton.setOnClickListener {
            uploadRecipe()
        }
    }
    /**
     * Обработка результата выбора изображения из галереи.
     *
     * @param requestCode код запроса.
     * @param resultCode код результата.
     * @param data данные, возвращённые из другой Activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                imagePreview.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Загружает рецепт на сервер. Проверяет корректность введённых данных
     * и отправляет их с помощью API.
     */
    private fun uploadRecipe() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val dishTime = dishTimeEditText.text.toString()
        val dishServings = dishServingsEditText.text.toString()
        val dishIngredients = dishIngredientsEditText.text.toString()

        // Проверка на заполненность полей
        if (title.isEmpty() || description.isEmpty() || dishTime.isEmpty() || dishServings.isEmpty() || dishIngredients.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val author = sharedPreferences.getString("username", "Default Username") // Если нет данных, покажет "Default Username"


        // Получаем временный файл из Uri
        val imageFile = getFileFromUri(selectedImageUri!!)
        if (imageFile == null || !imageFile.exists()) {
            Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
            return
        }

        // Создаём части для запроса
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val authorPart = RequestBody.create("text/plain".toMediaTypeOrNull(), author.toString())
        val timeCookPart = RequestBody.create("text/plain".toMediaTypeOrNull(), dishTime)
        val countPortionsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), dishServings.toString())
        val ingredientsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), dishIngredients.toString())

// Вызов API
        // Выполняем запрос на сервер
        GlobalScope.launch(Dispatchers.IO) { //конструкция которая позволяет делать запрос к бд в фоновом потоке не мешая основному отресовывать
            try {
                RetrofitInstance.api.uploadRecipe(titlePart, descriptionPart, authorPart, body, timeCookPart, countPortionsPart, ingredientsPart)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@NewPostActivity, "Рецепт успешно добавлен", Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                Log.e("Upload Error", "Код ответа: ${response.code()}, Тело: ${response.errorBody()?.string()}")
                                val errorBody = response.errorBody()?.string()
                                Log.e("Upload Error", "Код ответа: ${response.code()}, Ошибка: $errorBody")
                                Toast.makeText(this@NewPostActivity, "Ошибка загрузки: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Upload Error", "Ошибка: ${t.localizedMessage}")
                            val message = t.localizedMessage ?: "Неизвестная ошибка"
                            Log.e("Upload Error", "Ошибка подключения: $message")
                            runOnUiThread {
                                Toast.makeText(this@NewPostActivity, "Ошибка подключения: $message", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } catch (e: Exception) {
//                Log.e("Exception", "Request error: ${e.message}")
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@NewPostActivity, "Ошибка подключения. Проверьте интернет.", Toast.LENGTH_LONG).show()
//                }
            }
        }
    }


    // Метод для получения временного файла из Uri
    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload", ".jpeg", cacheDir)
            tempFile.outputStream().use { inputStream.copyTo(it) }
            tempFile
        } catch (e: Exception) {
            Log.e("File Error", "Ошибка получения файла из Uri: ${e.message}")
            null
        }
    }


    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}

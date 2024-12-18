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
    private lateinit var contentEditText: EditText
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
        contentEditText = findViewById(R.id.edit_dish_content)
        uploadButton = findViewById(R.id.btn_publish)
        chooseImageButton = findViewById(R.id.btn_choose_photo)

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

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
            imagePreview.setImageURI(selectedImageUri)
        }
    }
    /**
     * Загружает рецепт на сервер. Проверяет корректность введённых данных
     * и отправляет их с помощью API.
     */
    private fun uploadRecipe() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val content = contentEditText.text.toString()

        if (title.isEmpty() || description.isEmpty() || content.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }


        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val author = sharedPreferences.getString("username", "Default Username") // Если нет данных, покажет "Default Username"


        val file = File(selectedImageUri!!.path!!)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

// Создание частей запроса
        val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val contentPart = RequestBody.create("text/plain".toMediaTypeOrNull(), content)
        val authorPart = RequestBody.create("text/plain".toMediaTypeOrNull(), author.toString()) // Добавляем "author"

// Вызов API





        GlobalScope.launch(Dispatchers.IO) { //конструкция которая позволяет делать запрос к бд в фоновом потоке не мешая основному отресовывать
            try {
                RetrofitInstance.api.uploadRecipe(titlePart, descriptionPart, contentPart, authorPart, body)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@NewPostActivity, "Рецепт успешно добавлен", Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                Toast.makeText(this@NewPostActivity, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@NewPostActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
                        }
                    })
            } catch (e: Exception) {
                Log.e("Exception", "Request error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@NewPostActivity, "Ошибка подключения. Проверьте интернет.", Toast.LENGTH_LONG).show()
                }
            }
        }





    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}

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

class NewPostActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var chooseImageButton: Button
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        imagePreview = findViewById(R.id.image_preview)
        titleEditText = findViewById(R.id.edit_dish_name)
        descriptionEditText = findViewById(R.id.edit_dish_description)
        //contentEditText = findViewById(R.id.edit_dish_content)
        uploadButton = findViewById(R.id.btn_publish)
        chooseImageButton = findViewById(R.id.btn_choose_photo)

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

    private fun uploadRecipe() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()

        // Проверка на заполненность полей
        if (title.isEmpty() || description.isEmpty() || selectedImageUri == null) {
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

//        val file = File(selectedImageUri!!.path!!)
//        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//// Создание частей запроса
//        val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
//        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
//        //val contentPart = RequestBody.create("text/plain".toMediaTypeOrNull(), content)
//        val authorPart = RequestBody.create("text/plain".toMediaTypeOrNull(), author.toString()) // Добавляем "author"

        // Создаём части для запроса
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), title)
        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val authorPart = RequestBody.create("text/plain".toMediaTypeOrNull(), author.toString())





// Вызов API
        // Выполняем запрос на сервер
        GlobalScope.launch(Dispatchers.IO) { //конструкция которая позволяет делать запрос к бд в фоновом потоке не мешая основному отресовывать
            try {
                RetrofitInstance.api.uploadRecipe(titlePart, descriptionPart, authorPart, body)
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
            val tempFile = File.createTempFile("upload", null, cacheDir)
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

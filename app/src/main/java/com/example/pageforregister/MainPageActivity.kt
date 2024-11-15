package com.example.pageforregister

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainPageActivity : AppCompatActivity() {

    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var itemsList: RecyclerView
    private var items = mutableListOf<Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        itemsList = findViewById(R.id.itemsList)
        itemsAdapter = ItemsAdapter(items, this)
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = itemsAdapter

        loadInitialItems()





//        val itemsList: RecyclerView = findViewById(R.id.itemsList)
//        val items = arrayListOf<Item>()





        itemsList.layoutManager = LinearLayoutManager(this) // в каком формате будут распологаться элементы, как будто идут друг под другом
        itemsList.adapter = ItemsAdapter(items, this)





        val imageButton1: ImageButton = findViewById(R.id.button1)
        val imageButton2: ImageButton = findViewById(R.id.button2)
        val imageButton3: ImageButton = findViewById(R.id.button3)

        val search: EditText = findViewById(R.id.search)

        imageButton1.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        imageButton2.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }

        imageButton3.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadInitialItems() {
        // Загрузка примера
        items.add(Item(1, "D:\\Programming\\Android Studio\\ProjectKotlin\\app\\src\\main\\res\\drawable\\souptomyam.jpg", "     Суп Том-Ям", "\n" +
                "Варить этот тайский суп меня научила знакомая кореянка. " +
                "Она держит магазинчик на рынке с корейскими специями. " +
                "И все, что нужно для приготовления, в таком магазинчике есть. " +
                "Но и в обычном супермаркете можно купить специи для том яма.", "",
            2, "", listOf(
                Step(1, "Подготовьте ингредиенты", null),
                Step(2, "Смешайте специи", null)
            ), "Arseniy"))
        items.add(Item(2, "soupramen", "     Суп Рамэн", "\n" +
                "Суп «Рамэн»— это одно из самых популярных блюд в Японии. " +
                "Очень сытное блюдо. Готовят его практически на каждом шагу, " +
                "вариаций супа великое множество, наверное, сколько поваров в Японии, " +
                "столько и вариантов супов  ...", "", 2, "",
            listOf(
                Step(1, "Подготовьте ингредиенты", null),
                Step(2, "Смешайте специи", null)
            ), "Arseniy"))
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            val newItem = data?.getParcelableExtra<Item>("newItem")
            newItem?.let {
                items.add(it)
                itemsAdapter.notifyItemInserted(items.size - 1)
            }
        }
    }

    companion object {
        const val NEW_POST_REQUEST_CODE = 1001
    }
}

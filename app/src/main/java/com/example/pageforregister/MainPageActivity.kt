package com.example.pageforregister

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        val itemsList: RecyclerView = findViewById(R.id.itemsList)
        val items = arrayListOf<Item>()
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

        val answer_search = search.text.toString()


        items.add(Item(1, "souptomyam", "     Суп Том-Ям", "\n" +
                "Варить этот тайский суп меня научила знакомая кореянка. " +
                "Она держит магазинчик на рынке с корейскими специями. " +
                "И все, что нужно для приготовления, в таком магазинчике есть. " +
                "Но и в обычном супермаркете можно купить специи для том яма.", "", "Arseniy"))
        items.add(Item(2, "soupramen", "     Суп Рамэн", "\n" +
                "Суп «Рамэн»— это одно из самых популярных блюд в Японии. " +
                "Очень сытное блюдо. Готовят его практически на каждом шагу, " +
                "вариаций супа великое множество, наверное, сколько поваров в Японии, " +
                "столько и вариантов супов  ...", "", "Arseniy"))

        itemsList.layoutManager = LinearLayoutManager(this) // в каком формате будут распологаться элементы, как будто идут друг под другом
        itemsList.adapter = ItemsAdapter(items, this)
    }
}

package com.example.pageforregister

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide


class ItemsAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>(){

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.dish_name)
        val author: TextView = view.findViewById(R.id.profileName)
        val description: TextView = view.findViewById(R.id.dish_description) // Добавьте это поле в XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.author.text = item.author
        holder.description.text = item.description

        // Загрузка изображения с использованием Glide
        Glide.with(context)
            .load(item.image)
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()

            // Переход на экран с детальным описанием рецепта
            val intent = Intent(context, Card::class.java).apply {
                putExtra("itemId", item.id)  // Передаем идентификатор элемента
            }
            context.startActivity(intent)
        }
    }




}

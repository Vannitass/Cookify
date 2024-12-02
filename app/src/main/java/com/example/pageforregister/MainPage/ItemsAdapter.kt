package com.example.pageforregister.MainPage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pageforregister.R


class ItemsAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>(){

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.cardImage)
        val title: TextView = view.findViewById(R.id.cardDescription)
        val author: TextView = view.findViewById(R.id.userNickname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.author.text = items[position].author

        val imageId = context.resources.getIdentifier(
            items[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(imageId)

        holder.itemView.setOnClickListener {
            // Действие при нажатии на элемент, например, можно передать данные в новую активность
            Toast.makeText(context, "Clicked: ${items[position].title}", Toast.LENGTH_SHORT).show()

            // Переход на другую активность при нажатии на элемент
            val intent = Intent(context, Card::class.java)
            context.startActivity(intent)
        }
    }


}

package com.example.pageforregister.chat


import android.content.Context

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.R

class MessageAdapter(private val context: Context, private val messages: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // Создание ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text
        holder.timeTextView.text = message.time

        // Настройка выравнивания (например, для отправленных сообщений)
        val layoutParams = holder.messageTextView.layoutParams as LinearLayout.LayoutParams
        layoutParams.gravity = if (message.isSent) Gravity.END else Gravity.START
        holder.messageTextView.layoutParams = layoutParams
    }

    // Возвращаем размер списка
    override fun getItemCount(): Int = messages.size

    // Метод для добавления сообщения
    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    // ViewHolder для каждого сообщения
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    }
}

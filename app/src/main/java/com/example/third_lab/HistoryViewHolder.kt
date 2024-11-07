package com.example.third_lab

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ViewHolder для связывания данных истории с элементами интерфейса и для создания контекстного меню
class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
    val historyItemTextView: TextView = itemView.findViewById(R.id.historyItemTextView)
    var currentPosition: Int = 0

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(adapterPosition, 100, 0, "Обновить")
        menu?.add(adapterPosition, 101, 1, "Удалить")
        currentPosition = adapterPosition
    }
}

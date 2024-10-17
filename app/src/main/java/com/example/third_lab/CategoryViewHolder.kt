package com.example.third_lab

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
    val textViewCategoryItem: TextView = itemView.findViewById(R.id.textViewCategoryItem)
    var currentPosition: Int = 0

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    // Создание контекстного меню
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(adapterPosition, 7, 0, "Update") // ID = 121
        menu?.add(adapterPosition, 13, 1, "Delete") // ID = 122
        currentPosition = adapterPosition // Сохраняем позицию элемента
    }
}

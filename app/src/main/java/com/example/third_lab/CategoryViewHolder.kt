package com.example.third_lab

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ViewHolder для связывания данных категории с элементами интерфейса и для создания контекстного меню
class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
    val textViewCategoryItem: TextView = itemView.findViewById(R.id.textViewCategoryItem)
    var currentPosition: Int = 0

    // Устанавливаем контекстное меню на элемент списка
    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    // Создание контекстного меню
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(adapterPosition, 0, 0, "Обновить")
        menu?.add(adapterPosition, 1, 1, "Удалить")
        currentPosition = adapterPosition // Сохраняем позицию элемента
    }
}

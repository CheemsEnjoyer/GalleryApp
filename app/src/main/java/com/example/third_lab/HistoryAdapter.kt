package com.example.third_lab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private var historyItems: MutableList<String>) : RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.historyItemTextView.text = historyItems[position]
        holder.currentPosition = position
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    fun updateItem(position: Int, newHistoryItem: String) {
        historyItems[position] = newHistoryItem
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int) {
        historyItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getHistoryItems(): List<String> {
        return historyItems
    }
}

package com.example.third_lab.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.third_lab.R
import com.example.third_lab.domain.entity.Photo

class CategoryAdapter(
    var photos: MutableList<Photo>,
    private val onDelete: (Photo) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<CategoryAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPhoto: ImageView = itemView.findViewById(R.id.imageViewPhoto)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showPhotoDialog(position)
                }
            }

            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showContextMenu(itemView, position)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        Glide.with(holder.itemView.context)
            .load(photo.photoPath)
            .into(holder.imageViewPhoto)
    }

    override fun getItemCount(): Int = photos.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPhotos: List<Photo>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    private fun showPhotoDialog(position: Int) {
        val photo = photos[position]
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)

        Glide.with(context).load(photo.photoPath).into(imageView)

        AlertDialog.Builder(context)
            .setTitle("Просмотр фото")
            .setView(dialogView)
            .setPositiveButton("Закрыть", null)
            .setNegativeButton("Удалить") { _, _ ->
                onDelete(photo)
            }
            .show()
    }

    private fun showContextMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.photo_context_menu, popupMenu.menu)
        popupMenu.menu.findItem(R.id.menu_update_photo).isVisible = false

        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_delete_photo) {
                onDelete(photos[position])
                true
            } else false
        }

        popupMenu.show()
    }
}


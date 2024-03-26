package com.example.sampleandroidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.mvvm.settings

class SettingsAdapter(
    var settingsList: MutableList<settings>,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onEditClickListener: OnEditClickListener,
    private val onAddClickListener: OnAddClickListener
) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    interface OnEditClickListener {
        fun onEditClick(position: Int)
    }

    interface OnAddClickListener {
        fun onAddClick()
    }

    inner class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txvTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.txvDescription)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivRowDelete)
        private val editImageView: ImageView = itemView.findViewById(R.id.ivRowEdit)
        private val addImageView: ImageView = itemView.findViewById(R.id.ivRowAdd)

        fun bind(setting: settings, position: Int) {
            titleTextView.text = setting.title
            descriptionTextView.text = setting.description

            deleteImageView.setOnClickListener {
                onDeleteClickListener.onDeleteClick(position)
            }

            editImageView.setOnClickListener {
                onEditClickListener.onEditClick(position)
            }

            addImageView.setOnClickListener {
                onAddClickListener.onAddClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings, parent, false)
        return SettingsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(settingsList[position], position)
    }

    override fun getItemCount() = settingsList.size

    fun addSetting(setting: settings): MutableList<settings> {
        settingsList.add(setting)
        notifyItemInserted(settingsList.size - 1)
        return settingsList
    }

    fun updateSetting(position: Int, setting: settings): MutableList<settings> {
        settingsList[position] = setting
        notifyItemChanged(position)
        return settingsList
    }

    fun removeSetting(position: Int): MutableList<settings> {
        settingsList.removeAt(position)
        notifyItemRemoved(position)
        return settingsList
    }

    fun getSetting(position: Int): settings {
        return settingsList[position]
    }
}

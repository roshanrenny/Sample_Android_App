package com.example.sampleandroidapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.adapters.SettingsAdapter
import com.example.sampleandroidapp.mvvm.settings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsFrangment : Fragment(), SettingsAdapter.OnDeleteClickListener,
    SettingsAdapter.OnEditClickListener, SettingsAdapter.OnAddClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SettingsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_frangment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SettingPref", Context.MODE_PRIVATE)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SettingsAdapter(loadSettings(), this, this, this)
        recyclerView = requireView().findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadSettings(): MutableList<settings> {
        val settingsJson = sharedPreferences.getString("settings", "")
        return if (settingsJson.isNullOrEmpty()) {
            mutableListOf(
                settings("1", "Wi-Fi", "Manage your Wi-Fi settings"),
                settings("2", "Bluetooth", "Manage your Bluetooth settings"),
                settings("3", "Display", "Adjust your display settings"),
                settings("4", "Sound", "Adjust your sound and volume settings"),
                settings("5", "Notifications", "Manage app notifications"),
                settings("6", "Battery", "Monitor battery usage and settings"),
                settings("7", "Storage", "View and manage device storage"),
                settings("8", "Apps", "Manage installed applications"),
                settings("9", "Security", "Manage device security settings"),
                settings("10", "Location", "Manage location services"),
                settings("11", "Accounts", "Manage your accounts"),
                settings("12", "Language", "Change the language settings"),
                settings("13", "Date & Time", "Adjust date and time settings")

            )
        } else {
            Gson().fromJson(settingsJson, object : TypeToken<MutableList<settings>>() {}.type)
        }
    }

    override fun onDeleteClick(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this setting?")
            .setPositiveButton("Delete") { _, _ ->
                val updatedSettings = adapter.removeSetting(position)
                saveSettings(updatedSettings)
                showToast("Item Deleted")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onEditClick(position: Int) {
        val setting = adapter.getSetting(position)
        val editDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_setting, null)
        val editTextTitle = editDialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = editDialogView.findViewById<EditText>(R.id.editTextDescription)

        editTextTitle.setText(setting.title)
        editTextDescription.setText(setting.description)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit")
            .setView(editDialogView)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = editTextTitle.text.toString()
                val newDescription = editTextDescription.text.toString()
                val updatedSetting = setting.copy(title = newTitle, description = newDescription)
                adapter.updateSetting(position, updatedSetting)
                saveSettings(adapter.settingsList)
                showToast("Item Edited")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onAddClick() {
        val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_setting, null)
        val editTextTitle = addDialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = addDialogView.findViewById<EditText>(R.id.editTextDescription)

        AlertDialog.Builder(requireContext())
            .setTitle("Add")
            .setView(addDialogView)
            .setPositiveButton("Add") { _, _ ->
                val newTitle = editTextTitle.text.toString()
                val newDescription = editTextDescription.text.toString()
                val newSetting = settings((adapter.itemCount + 1).toString(), newTitle, newDescription)
                val updatedSettings = adapter.addSetting(newSetting)
                saveSettings(updatedSettings)
                showToast("Item Added")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveSettings(settings: MutableList<settings>) {
        val settingsJson = Gson().toJson(settings)
        sharedPreferences.edit().putString("settings", settingsJson).apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

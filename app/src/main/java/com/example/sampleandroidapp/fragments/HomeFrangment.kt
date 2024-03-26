package com.example.sampleandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sampleandroidapp.adapters.GridAdapter
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.activity.CameraActivity
import com.example.sampleandroidapp.activity.ApiActivity
import com.example.sampleandroidapp.activity.BackgroundActivity
import com.example.sampleandroidapp.activity.MapsActivity
import com.example.sampleandroidapp.activity.OTPActivity

class HomeFrangment : Fragment() {
    private var gridView: GridView? = null
    private val items =
        arrayOf("HP", "Dell", "Acer", "Asus", "Lenovo", "Mac", "MI", "Samsung", "LG", "HCL")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_frangment, container, false)
        gridView = view.findViewById(R.id.grid)

        val adapter = GridAdapter(requireContext(), items)
        gridView?.adapter = adapter

        gridView?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val clickedItem = items[position]
                Toast.makeText(requireContext(), "Clicked item: $clickedItem", Toast.LENGTH_SHORT)
                    .show()
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serviceButton: Button = view.findViewById(R.id.servicebutton)
        val apiButton : Button = view.findViewById(R.id.Apibutton)
        val mapsButton : Button = view.findViewById(R.id.Mapsbutton)

        serviceButton.setOnClickListener {
            startActivity(Intent(requireContext(), BackgroundActivity::class.java))
        }
        apiButton.setOnClickListener{
            startActivity(Intent(requireContext(), ApiActivity::class.java))
        }
        mapsButton.setOnClickListener { showPopupMenu(mapsButton) }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.pop_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.maps -> {
                    Toast.makeText(requireContext(), "Opening Google Maps", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(),MapsActivity::class.java))
                    true
                }
                R.id.phone -> {
                    Toast.makeText(requireContext(), "Opening OTP Page", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), OTPActivity::class.java))
                    true
                }
                R.id.camera -> {
                    Toast.makeText(requireContext(), "Opening Camera Page", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), CameraActivity::class.java))
                    true
                }
                else -> false
            }

        }
        popupMenu.show()
    }
}
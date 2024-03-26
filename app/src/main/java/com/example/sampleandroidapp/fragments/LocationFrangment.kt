package com.example.sampleandroidapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.databinding.FragmentLocationFrangmentBinding

class LocationFrangment<T> : Fragment() {

    private var _binding: FragmentLocationFrangmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationFrangmentBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val locations = getListOfLocations()
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_list, R.id.locationTextView, locations)
        binding.locationListView.adapter = adapter

        return rootView
    }

    private fun getListOfLocations(): List<String> {
        return listOf(
            "Hyderabad", "Chennai", "Bangalore",
            "Kerala", "Mumbai", "Assam", "Delhi", "Kolkata", "Pune", "Jaipur",
            "Ahmedabad", "Lucknow", "Goa",
            "Varanasi", "Agra", "Shimla",
            "Udaipur", "Srinagar", "Nagpur",
            "Indore", "Bhopal", "Mexico", "New York", "Canada", "Dubai",
            "Russia", "China", "Japan", "Sri Lanka", "Denmark"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

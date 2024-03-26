package com.example.sampleandroidapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sampleandroidapp.mvvm.DetailsViewModel
import com.example.sampleandroidapp.R

class DetailsFrangment<T> : Fragment() {

    private lateinit var databtn: Button
    private lateinit var datafetched: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var message: TextView

    private lateinit var viewModel: DetailsViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_frangment, container, false)

        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        databtn = view.findViewById(R.id.data_btn)
        datafetched = view.findViewById(R.id.data_fetched)
        progressBar = view.findViewById(R.id.idLoadingPB)
        message = view.findViewById(R.id.messageTextView)

        databtn.setOnClickListener {
            viewModel.fetchData()
        }

        viewModel.dataFetched.observe(viewLifecycleOwner) { data ->
            datafetched.text = data.value
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) { messageText ->
            message.visibility = if (messageText.isNotEmpty()) View.VISIBLE else View.GONE
            message.text = messageText
        }

        return view
    }
}

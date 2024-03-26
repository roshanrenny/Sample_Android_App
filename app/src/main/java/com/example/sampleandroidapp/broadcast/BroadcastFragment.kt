package com.example.sampleandroidapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.sampleandroidapp.R

class BroadcastFragment : Fragment() {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            intent?.let {
                val message = it.getStringExtra("message")
                message?.let { msg ->
                    showToast("Message received: $msg")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter("ACTION_SEND_MESSAGE"),
            Context.RECEIVER_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.broadcast, container, false)

        val messageInput = view.findViewById<EditText>(R.id.message_input)
        val sendButton = view.findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                val intent = Intent("ACTION_SEND_MESSAGE")
                intent.putExtra("message", message)
                requireActivity().sendBroadcast(intent)
                messageInput.setText("")
                showToast("Message sent: $message")
            } else {
                showToast("Please enter a message")
            }
        }

        return view
    }
}

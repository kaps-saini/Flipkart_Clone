package com.example.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private lateinit var msg:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        msg = findViewById(R.id.tvMsg)


        try {
            // Replace "http://yourserver.com" with your server URL
            mSocket = IO.socket("http://172.16.2.4:3000")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        // Connecting to the server
        mSocket.connect()

        // Listening for messages from the server
        mSocket.on("message", onNewMessage)

        // Emitting (sending) a message to the server
        sendMessageToServer("Hello, Server!")
    }

    // Listener for new messages from the server
    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            val data = args[0] as JSONObject
            val message = data.getString("message")
            // Handle the message (e.g., display it in the UI)
            msg.text = message
        }
    }

    // Function to send a message to the server
    private fun sendMessageToServer(message: String) {
        val jsonObject = JSONObject()
        jsonObject.put("message", message)
        mSocket.emit("message", jsonObject)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("message", onNewMessage)
    }
}
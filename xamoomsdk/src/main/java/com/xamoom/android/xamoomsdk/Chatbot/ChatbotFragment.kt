package com.xamoom.android.xamoomsdk.Chatbot

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.xamoom.android.xamoomsdk.EnduserApi

import com.xamoom.android.xamoomsdk.R


class ChatbotFragment(val botId: String, val enduserApi: EnduserApi, val applicationContext: Context) : Fragment() {
    private val RECORD_REQUEST_CODE = 101
    private val voiceChat = VoiceChat(applicationContext, enduserApi, botId)

    private lateinit var speakButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_chatbot, container, false)

        setupPermissions(applicationContext)
        setupSpeakButton(view)

        return view;
    }

    private fun setupPermissions(context: Context) {
        val permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ChatbotFragment", "Permission to record audio denied")
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Permission to access the microphone is required for this app to record audio.")
                                .setTitle("Permission required")

                                builder.setPositiveButton("OK"
                                ) { dialog, id ->
                            Log.i("ChatbotFragment", "Clicked")
                            makeRequest()
                        }

                        val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
    }

    private fun makeRequest() {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("ChatbotFragment", "Permission has been denied by user")
                } else {
                    Log.i("ChatbotFragment", "Permission has been granted by user")
                }
            }
        }
    }

    private fun setupSpeakButton(view: View){
        speakButton = view.findViewById(R.id.speakButton)

        speakButton.setOnTouchListener { v, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                voiceChat.startListening(object : VoiceChatListener() {
                    override fun onReady() {
                        Toast.makeText(applicationContext, "Ready", Toast.LENGTH_LONG).show()
                    }

                    override fun onFinished(answer: Answer) {
                        val actionStringbuilder = StringBuilder()

                        answer.actions?.forEach { action ->
                            actionStringbuilder.append(action.type.toString() + " -> " + action.value + "\n")
                        }

                        val actionsString = actionStringbuilder.toString()
                        val text = answer.text
                        val response = "$text - Actions: $actionsString"
                        Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                    }

                    override fun onError(error: String) {
                        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                    }
                })
            } else if(event?.action == MotionEvent.ACTION_UP) {
                voiceChat.stopListening()
            }

            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(botId: String, enduserApi: EnduserApi, applicationContext: Context) =
                ChatbotFragment(botId, enduserApi, applicationContext)
    }
}

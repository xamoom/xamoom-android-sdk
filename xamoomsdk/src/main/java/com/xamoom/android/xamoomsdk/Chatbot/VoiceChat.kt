package com.xamoom.android.xamoomsdk.Chatbot

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.xamoom.android.xamoomsdk.EnduserApi
import java.util.*

class VoiceChat(val context: Context, val enduserApi: EnduserApi, val botId: String) {
    private val TAG = "com.xamoom.android.xamoomsdk.Chatbot.VoiceChat"

    // Voice Recognition
    private val language =  Locale.getDefault()
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private val recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    // Voice Output
    private lateinit var tts: TextToSpeech

    // Chatbot API
    private val textChat: TextChat = TextChat(context, enduserApi, botId)


    init {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.getDefault())
            }
        })

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.toString())
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.xamoom.android.xamoomsdk.Chatbot")
    }

    fun startListening(listener: VoiceChatListener){
        val recognizerListener = VoiceChatRecognizerListener(listener, textChat, tts)
        recognizer.setRecognitionListener(recognizerListener)
        recognizer.startListening(recognizerIntent)
    }

    fun stopListening(){
        recognizer.stopListening()
    }

    fun getCurrentConversationContext(): String{
        return textChat.getCurrentConversationContext()
    }

    fun clearConversationContext() {
        textChat.clearConversationContext()
    }

}

class VoiceChatRecognizerListener(val listener: VoiceChatListener, val textChat: TextChat, val tts: TextToSpeech) : RecognitionListener {
    private val TAG = "xamoomVoiceChatBot"

    override fun onReadyForSpeech(params: Bundle?) {
        listener.onReady()
    }

    override fun onRmsChanged(rmsdB: Float) {
        listener.onRmsChanged(rmsdB)
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d(TAG, "onBufferReceived")
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val voiceResults = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        if(voiceResults != null) {
            if (voiceResults.size > 0) {
                if(voiceResults[0].length > 0) {
                    listener.onPartialResult(voiceResults[0])
                }
            }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d(TAG, "onEvent")
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech")
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech")
    }

    override fun onError(error: Int) {
        /**
         *  ERROR_NETWORK_TIMEOUT = 1;
         *  ERROR_NETWORK = 2;
         *  ERROR_AUDIO = 3;
         *  ERROR_SERVER = 4;
         *  ERROR_CLIENT = 5;
         *  ERROR_SPEECH_TIMEOUT = 6;
         *  ERROR_NO_MATCH = 7;
         *  ERROR_RECOGNIZER_BUSY = 8;
         *  ERROR_INSUFFICIENT_PERMISSIONS = 9;
         *
         * @param error code is defined in SpeechRecognizer
         */

        Log.d(TAG, "onError: $error")
        listener.onError(error.toString())
    }

    override fun onResults(results: Bundle?) {
        val voiceResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        if(voiceResults != null) {
            if (voiceResults.size > 0) {
                var input = voiceResults[0]

                textChat.chat(input, object: TextChatListener() {
                    override fun onFinished(answer: Answer) {
                        listener.onFinished(answer)

                        // Speak
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(answer.text,TextToSpeech.QUEUE_FLUSH,null,null)
                        } else {
                            tts.speak(answer.text, TextToSpeech.QUEUE_FLUSH, null)
                        }
                    }

                    override fun onError(errorCode: String, message: String) {
                        listener.onError(errorCode)
                    }
                })
            }
        } else {
            Log.e(TAG, "no voice results")
            onError(1000)
        }
    }

}

abstract class VoiceChatListener {
    abstract fun onReady()
    abstract fun onError(error: String)
    abstract fun onFinished(answer: Answer)
    abstract fun onRmsChanged(rmsdB: Float)
    abstract fun onPartialResult(partialResult: String)
}
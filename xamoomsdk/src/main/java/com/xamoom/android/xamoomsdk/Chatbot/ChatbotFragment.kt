package com.xamoom.android.xamoomsdk.Chatbot

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import com.xamoom.android.xamoomsdk.*
import kotlinx.android.synthetic.main.content_block_4_layout.view.*


class ChatbotFragment(val botId: String, val backgroundColor: Int, val foregroundColor: Int,
                      val microphoneActiveIcon: Int, val microphoneInactiveIcon: Int,
                      val enduserApi: EnduserApi,  val actionClickedListener: ActionClickedListener,
                      val applicationContext: Context) : Fragment() {
    private val RECORD_REQUEST_CODE = 101
    private val voiceChat = VoiceChat(applicationContext, enduserApi, botId)

    private lateinit var speakButton: ImageButton
    private lateinit var speakAnimationImage: ImageView
    private var speakButtonCurrentScale: Float = 1f
    private var speakIsAnimating: Boolean = false

    private lateinit var progressbar: ProgressBar
    private lateinit var outputTextView: TextView
    private lateinit var inputTextView: TextView
    private lateinit var outputRecyclerView: RecyclerView
    private lateinit var infoTextView: TextView
    private lateinit var examplesTextView: TextView
    private lateinit var backgroundView: FrameLayout
    private lateinit var hintsView: LinearLayout

    private var permissionsGranted: Boolean = false

    private var isLoading: Boolean = false

    private val sampleQuestions: ArrayList<String> = ArrayList()
    private val actions: ArrayList<ChatbotAction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_chatbot, container, false)

        setupViews(view)
        styleViews()
        resetViewToInitialState()
        loadChatbotSamples(0)

        return view;
    }

    private fun setupViews(view: View){
        speakButton = view.findViewById(R.id.speakButton)
        speakAnimationImage = view.findViewById(R.id.speakAnimationImage)
        progressbar = view.findViewById(R.id.progressBar)
        outputTextView = view.findViewById(R.id.outputTextView)
        inputTextView = view.findViewById(R.id.inputTextView)
        infoTextView = view.findViewById(R.id.infoTextView)
        examplesTextView = view.findViewById(R.id.examplesTextView)
        backgroundView = view.findViewById(R.id.backgroundView)
        hintsView = view.findViewById(R.id.hints)

        outputRecyclerView = view.findViewById(R.id.outputRecyclerView)
        outputRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        outputRecyclerView.adapter = ChatbotActionsAdapter(actions, actionClickedListener, enduserApi)
    }

    private fun styleViews(){
        speakButton.setBackgroundResource(microphoneInactiveIcon)
        speakAnimationImage.setBackgroundResource(microphoneActiveIcon)
        speakAnimationImage.alpha = 0.2f
        progressbar.indeterminateDrawable.setColorFilter(
                resources.getColor(foregroundColor), android.graphics.PorterDuff.Mode.SRC_IN)
        outputTextView.setTextColor(resources.getColor(foregroundColor))
        inputTextView.setTextColor(resources.getColor(foregroundColor))
        infoTextView.setTextColor(resources.getColor(foregroundColor))
        examplesTextView.setTextColor(resources.getColor(foregroundColor))
        backgroundView.setBackgroundColor(resources.getColor(backgroundColor))
    }

    private fun resetViewToInitialState(){
        if(permissionsGranted) {
            speakButton.setBackgroundResource(microphoneActiveIcon)
        } else {
            speakButton.setBackgroundResource(microphoneInactiveIcon)
        }

        speakButton.isEnabled = true
        speakButton.isClickable = true
        speakButton.alpha = 1.0F
        speakButton.visibility = View.VISIBLE
        progressbar.visibility = View.INVISIBLE
        outputTextView.visibility = View.VISIBLE
        inputTextView.visibility = View.VISIBLE

        if(actions.size == 0) {
            outputRecyclerView.visibility = View.INVISIBLE
        } else {
            outputRecyclerView.visibility = View.VISIBLE
        }

        infoTextView.visibility = View.VISIBLE
        examplesTextView.visibility = View.VISIBLE

        setupSpeakButton()
    }

    private fun showLoading(){
        isLoading = true

        speakButton.isEnabled = false
        speakButton.isClickable = false
        speakButton.alpha = 0.5F
        progressbar.visibility = View.VISIBLE
        outputTextView.visibility = View.INVISIBLE
        inputTextView.visibility = View.VISIBLE
        outputRecyclerView.visibility = View.INVISIBLE
        infoTextView.visibility = View.VISIBLE
        examplesTextView.visibility = View.VISIBLE
    }

    private fun showExampleQuestions(){
        if(sampleQuestions.size > 0){
            val sb = StringBuilder()

            sb.append(resources.getString(R.string.chatbot_examples_title))
            sb.append("\n\n")
            for(sample in sampleQuestions){
                sb.append(sample)
                sb.append("\n")
            }

            examplesTextView.text = sb.toString()
        } else {
            examplesTextView.text = ""
        }
    }

    private fun setupPermissions(context: Context) {
        val permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false
            resetViewToInitialState()

            Log.i("ChatbotFragment", "Permission to record audio denied")
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage(applicationContext.getString(R.string.chatbot_microphone_permission_text))
                                .setTitle(applicationContext.getString(R.string.chatbot_microphone_permission_title))

                                builder.setPositiveButton("OK"
                                ) { dialog, id ->
                                    makeRequest()
                                }

                        val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        } else {
            permissionsGranted = true
            resetViewToInitialState()
        }
    }

    private fun makeRequest() {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()

        val permission = ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.RECORD_AUDIO)

        permissionsGranted = permission == PackageManager.PERMISSION_GRANTED

        if(!isLoading) {
            resetViewToInitialState()
        }
    }

    private fun loadChatbotSamples(warmupRetries: Int) {
        showLoading()

        enduserApi.getChatbotSamples(botId, object: ChatSamplesAPICallback {
            override fun finished(samples: java.util.ArrayList<String>?) {
                sampleQuestions.clear()
                samples?.let { sampleQuestions.addAll(it) }

                showExampleQuestions()

                isLoading = false
                resetViewToInitialState()

                sayWelcome(0)
            }

            override fun error(status_code: String?, message: String?) {
                isLoading = false

                if(message == "timeout" && warmupRetries < 4){
                    Timer().schedule(4000){
                        val retries = warmupRetries + 1
                        loadChatbotSamples(retries)
                    }
                } else {
                    Toast.makeText(context, "Failed to load sample questions. $status_code - $message", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun sayWelcome(warmupRetries: Int) {
        showLoading()

        enduserApi.chat("", "", botId, object : ChatAPICallback {
            override fun finished(text: String, context: String, actions: java.util.ArrayList<ChatbotAction>, confidence: Double, success: Boolean) {
                voiceChat.speak(text)

                isLoading = false
                resetViewToInitialState()
            }

            override fun error(status_code: String, message: String) {
                if(message == "timeout" && warmupRetries < 4){
                    Timer().schedule(4000){
                        isLoading = false
                        val retries = warmupRetries + 1
                        sayWelcome(retries)
                    }
                } else {
                    Toast.makeText(context, "Failed to load welcome. $status_code - $message", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSpeakButton(){
        if(!permissionsGranted){
            speakButton.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    setupPermissions(applicationContext)
                }
            })
        } else {
            speakButton.setOnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    voiceChat.startListening(object : VoiceChatListener() {
                        override fun onStartRequest(input: String) {
                            animateSpeakButton(1f)
                            inputTextView.text = "„$input“"
                            showLoading()
                        }

                        override fun onRmsChanged(rmsdB: Float) {
                            var to = 1f

                            if (rmsdB > 8) {
                                to = 1.6f
                            } else if (rmsdB > 6) {
                                to = 1.5f
                            } else if (rmsdB > 5) {
                                to = 1.4f
                            } else if (rmsdB > 4) {
                                to = 1.3f
                            } else if (rmsdB > 3) {
                                to = 1.2f
                            } else if (rmsdB > 2) {
                                to = 1.1f
                            } else if (rmsdB > 1) {
                                to = 1.05f
                            } else {
                                to = 1.0f
                            }

                            animateSpeakButton(to)
                        }

                        override fun onPartialResult(partialResult: String) {
                            inputTextView.text = "„$partialResult ...“"
                        }

                        override fun onReady() {
                            actions.clear()
                            outputRecyclerView.visibility = View.INVISIBLE
                            inputTextView.text = "„...“"
                            inputTextView.alpha = 0.5f
                            outputTextView.text = ""
                        }

                        override fun onFinished(answer: Answer) {
                            isLoading = false

                            animateSpeakButton(1f)

                            val text = answer.text
                            val confidence = answer.confidence
                            Log.d("ChatbotFragment", "Confidence: $confidence")

                            outputTextView.text = "„$text“"
                            outputTextView.alpha = 1f

                            resetViewToInitialState()

                            if(answer.success!!){
                                hintsView.visibility = View.INVISIBLE

                                //show actions
                                actions.clear()

                                if(answer.actions!!.size > 0) {
                                    actions.addAll(answer.actions!!)

                                    // TODO REMOVE DEMO ACTIONS
                                    val action_0 = ChatbotAction(0, "https://xamoom.com")
                                    val action_1 = ChatbotAction(1, "https://www.oeticket.com/artist/iron-maiden/?affiliate=B38")
                                    val action_2 = ChatbotAction(2, "https://www.google.com/maps/place/Pyramidenkogel+Tower/@46.6090217,14.1428867,17z/data=!4m5!3m4!1s0x477077758dcaab47:0x119d60a4d0b3dd67!8m2!3d46.6090217!4d14.1450754")
                                    val action_3 = ChatbotAction(3, "+436802339221")
                                    val action_4 = ChatbotAction(4, "georg@xamoom.com")
                                    val action_5 = ChatbotAction(5, "TEST")
                                    val action_6 = ChatbotAction(6, "2764")
                                    val action_7 = ChatbotAction(7, "X-MERCEDES-TEAM")
                                    val action_8 = ChatbotAction(8, "1364")

                                    actions.clear()
                                    actions.add(action_0)
                                    actions.add(action_1)
                                    actions.add(action_2)
                                    actions.add(action_3)
                                    actions.add(action_4)
                                    actions.add(action_5)
                                    actions.add(action_6)
                                    actions.add(action_7)
                                    actions.add(action_8)
                                    // END DEMO ACTIONS


                                    outputRecyclerView.visibility = View.VISIBLE
                                } else {
                                    outputRecyclerView.visibility = View.INVISIBLE
                                }

                                outputRecyclerView.adapter!!.notifyDataSetChanged()
                            } else {
                                loadChatbotSamples(0)
                                hintsView.visibility = View.VISIBLE
                                outputRecyclerView.visibility = View.INVISIBLE
                            }
                        }

                        override fun onError(error: String) {
                            animateSpeakButton(1f)

                            isLoading = false
                            resetViewToInitialState()
                            Log.e("ChatbotFragment", "Voice Recognizer Error: $error")

                            if(error.toInt() > 9){
                                loadChatbotSamples(0)

                                hintsView.visibility = View.VISIBLE
                                outputRecyclerView.visibility = View.INVISIBLE

                                val errorText = applicationContext.getString(R.string.chatbot_local_error_response)
                                outputTextView.text = errorText
                                voiceChat.speak(errorText)
                            }

                            hintsView.visibility = View.VISIBLE
                            outputRecyclerView.visibility = View.INVISIBLE

                            inputTextView.text = ""
                        }
                    })
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    animateSpeakButton(1f)
                    voiceChat.stopListening()
                }

                true
            }
        }
    }

    fun animateSpeakButton(endScale: Float) {
        if(!speakIsAnimating) {
            speakIsAnimating = true

            val anim = ScaleAnimation(speakButtonCurrentScale, endScale, speakButtonCurrentScale, endScale,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            anim.fillAfter = true // Needed to keep the result of the animation
            anim.duration = 50
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    speakIsAnimating = false
                }

                override fun onAnimationStart(animation: Animation?) { }
            })

            var animationSize = 1f
            var alpha = 1F
            if(endScale > 1){
                alpha = 2 - endScale
                animationSize = (endScale * 1.3).toFloat()
            }
            val anim2 = ScaleAnimation(speakButtonCurrentScale, animationSize, speakButtonCurrentScale, animationSize,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            anim2.fillAfter = true // Needed to keep the result of the animation
            anim2.duration = 100

            val anim3 = AlphaAnimation(1F, alpha)
            anim3.duration = 100

            speakButtonCurrentScale = endScale

            speakAnimationImage.startAnimation(anim2)
            speakButton.startAnimation(anim)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(botId: String, backgroundColor: Int, foregroundColor: Int,
                        microphoneActiveIcon: Int, microphoneInactiveIcon: Int,
                        enduserApi: EnduserApi, actionClickedListener: ActionClickedListener,
                        applicationContext: Context) =
                ChatbotFragment(botId, backgroundColor, foregroundColor,
                        microphoneActiveIcon, microphoneInactiveIcon,
                        enduserApi, actionClickedListener, applicationContext)
    }
}

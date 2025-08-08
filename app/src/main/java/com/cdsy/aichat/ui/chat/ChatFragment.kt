package com.cdsy.aichat.ui.chat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentChatBinding
import com.cdsy.aichat.manager.api.base.globalMoshi
import com.cdsy.aichat.model.api.character.CharacterDetail
import com.cdsy.aichat.ui.activity.ContentActivity
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.character_detail.KEY_CHARACTER_DETAIL
import com.cdsy.aichat.ui.character_detail.KEY_CREDIT
import com.cdsy.aichat.util.fromJson
import com.cdsy.aichat.util.toJson
import io.noties.markwon.Markwon
import timber.log.Timber

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(
    ChatViewModel::class.java,
    R.layout.fragment_chat
) {

    private lateinit var markwon: Markwon
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private var isCancelled = false
    private var isListening = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) startListening() else {
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {

        markwon = Markwon.create(requireContext())
        // 简介渲染
        val aiIntro = """
            ## Jesus Brown\n逸林本和你是青梅竹马，但小时候他家人把她接到国外生活，你们无缘再见。但今天你在咖啡馆一眼就认出了她，一瞬间心跳像停止了一样，突然她转过头看向了你，你从她眼睛里看到了疑惑，这时候你的心顿时失落，但还是想去叫...\n
        """.trimIndent()
        markwon.setMarkdown(binding.tvIntro, aiIntro)

        //聊天消息列表
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatRecyclerAdapter(markwon)
        }

        // Mic按钮语音输入
        setupSpeechRecognizer()

        // 发送按钮
        binding.btnSend.setOnClickListener {
            val text = binding.etInput.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                binding.etInput.setText("")
            }
        }

        viewModel.chatMessageList.observeNonNull {
            (binding.rvChat.adapter as ChatRecyclerAdapter).replaceData(it)
            if (it.isNotEmpty()) {
                // 滚动到 NestedScrollView 底部
                binding.ns.post {
                    binding.ns.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    override fun initData() {
        viewModel.credit.value = arguments?.getInt(KEY_CREDIT)
        viewModel.characterDetail.value = globalMoshi.fromJson<CharacterDetail>(
            arguments?.getString(
                KEY_CHARACTER_DETAIL
            )
        )
    }

    private fun startListening() {
        if (!isListening) {
            speechRecognizer.startListening(recognizerIntent)
        }
    }

    private fun stopListening(cancel: Boolean) {
        if (isListening) speechRecognizer.stopListening()
        isCancelled = cancel
    }

    private fun sendMessage(text: String) {
        // 留出网络请求入口
        viewModel.sendMessage(text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy()
    }

    private fun showRecordUI() {
        context?.getColor(R.color.white)?.let { binding.tvMicTip.setTextColor(it) }
        context?.getDrawable(R.drawable.img_sound_green)
            ?.let { binding.ivSound.setImageDrawable(it) }
        binding.clBottomRecord.visibility = View.VISIBLE
    }

    private fun hideRecordUI() {
        context?.getColor(R.color.white)?.let { binding.tvMicTip.setTextColor(it) }
        context?.getDrawable(R.drawable.img_sound_green)
            ?.let { binding.ivSound.setImageDrawable(it) }
        binding.clBottomRecord.visibility = View.GONE
        speechRecognizer.cancel()
    }

    private fun checkAndStartListening() {
        // 动态权限
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN")
        }
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                showRecordUI()
            }

            override fun onBeginningOfSpeech() {
                isListening = true
            }

            override fun onRmsChanged(rmsdB: Float) {
                Timber.d("%s%s", "!!!", rmsdB)
            }

            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                hideRecordUI()
                isListening = false
            }

            override fun onError(error: Int) {
                hideRecordUI()
                isListening = false
            }

            override fun onResults(results: Bundle?) {
                hideRecordUI()
                if (!isCancelled) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull()
                    if (!text.isNullOrEmpty()) {
                        sendMessage(text)
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        binding.btnMic.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isCancelled = false
                    checkAndStartListening()
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (event.y < -v.height / 2) {
                        isCancelled = true
                        context?.getColor(R.color.color_error_text)
                            ?.let { binding.tvMicTip.setTextColor(it) }
                        context?.getDrawable(R.drawable.img_sound_red)
                            ?.let { binding.ivSound.setImageDrawable(it) }
                        stopListening(true)
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (isCancelled) {
                        hideRecordUI()
                    }
                    stopListening(isCancelled)
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    stopListening(isCancelled)
                    true
                }

                else -> false
            }
        }

    }

}

fun Context.navigateToChat(credit: Int, characterDetail: CharacterDetail) {
    startActivity(
        ContentActivity.createIntent(
            context = this,
            des = ContentActivity.Destination.Chat,
            bundle = bundleOf(
                Pair(KEY_CREDIT, credit),
                Pair(KEY_CHARACTER_DETAIL, characterDetail.toJson())
            )
        )
    )
}
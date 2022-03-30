package com.meghdut.nimie.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.meghdut.nimie.R
import com.meghdut.nimie.data.image.ImageCache
import com.meghdut.nimie.databinding.ActivityChatBinding
import com.meghdut.nimie.ui.util.ChatAdapter
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import javax.inject.Inject


@AndroidEntryPoint
class ChatActivity : AppCompatActivity(R.layout.activity_chat), LifecycleObserver {
    val viewModel: ChatViewModel by viewModels()
    private val binding by viewBinding(ActivityChatBinding::bind)

    @Inject
    lateinit var imageCache: ImageCache

    companion object {
        const val CONVERSATION_ID = "convid"
    }

    val PICK_IMAGES_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter  = ChatAdapter(imageCache)
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.msgRv.layoutManager = llm
        binding.msgRv.adapter = adapter

        val conversationId = intent.getLongExtra(CONVERSATION_ID, 0)


        viewModel.getMessages(conversationId).observe(this) {
            it?.let {
                binding.loadingTv.visibility = View.GONE
                adapter.submitData(lifecycle, it)
            }
        }
        viewModel.openConversation(conversationId)

        viewModel.currentConversation.observe(this) {
            it?.let {
                binding.apply {
                    profileIv.load(avatar(it.otherName))
                    userNameTv.text = it.otherName
                }
            }
        }

        binding.openCamera.setOnClickListener {
            openGalleryChooser()
        }

        binding.sendIv.setOnClickListener {
            val text = binding.msgText.text.trim().toString()
            binding.msgText.setText("")
            if (text.isNotBlank()) {
                viewModel.sendMessage(text)
            }
        }

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val conversationId = intent.getLongExtra(CONVERSATION_ID, 0)
        viewModel.openConversation(conversationId)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGES_REQUEST -> {
                    val mediaUri: Uri = data?.data!!
                    val inputStream: InputStream? =
                        baseContext.contentResolver.openInputStream(mediaUri)
                    inputStream?.use {
                        val readBytes = it.readBytes()
                        viewModel.sendImageMessage(readBytes)
                    }
                }
            }
        }
    }

    private fun openGalleryChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGES_REQUEST)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeConversation()
    }


}
package com.meghdut.nimie.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityChatBinding
import com.meghdut.nimie.ui.util.ChatAdapter
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity(R.layout.activity_chat) {
    val viewModel: ChatViewModel by viewModels()
    private val binding by viewBinding(ActivityChatBinding::bind)

    companion object {
        const val CONVERSATION_ID = "convid"
    }


    private val adapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.msgRv.layoutManager = llm
        binding.msgRv.adapter = adapter

        val conversationId = intent.getLongExtra(CONVERSATION_ID,0)


        viewModel.getMessages(conversationId).observe(this) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }

        viewModel.currentConversation.observe(this) {
            it?.let {
                binding.apply {
                    profileIv.load(avatar(it.otherName))
                    userNameTv.text = it.otherName
                }
            }
        }


    }
}
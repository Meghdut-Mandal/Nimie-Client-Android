package com.meghdut.nimie.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityChatBinding
import com.meghdut.nimie.ui.util.ChatAdapter
import com.meghdut.nimie.ui.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity(R.layout.activity_chat) {
    val viewModel: ChatViewModel by viewModels()
    private val binding by viewBinding(ActivityChatBinding::bind)

    companion object{
        const val CONVERSATION_ID = "convid"
    }


    private val adapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.msgRv.layoutManager = llm
        binding.msgRv.adapter = adapter

        val conversationId = intent.getStringExtra(CONVERSATION_ID)?.toLong() ?: -1


        viewModel.getMessages(conversationId).observe(this){
            it?.let {
                adapter.submitData(lifecycle,it)
            }
        }


    }
}
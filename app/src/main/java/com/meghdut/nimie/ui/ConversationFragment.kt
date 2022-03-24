package com.meghdut.nimie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.meghdut.nimie.R
import com.meghdut.nimie.data.model.LocalConversation
import com.meghdut.nimie.databinding.ConversationItemBinding
import com.meghdut.nimie.databinding.FragmentConversationBinding
import com.meghdut.nimie.ui.util.XPagedAdapter
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.util.navigateTo
import com.meghdut.nimie.ui.viewmodel.ConversationViewModel
import java.text.SimpleDateFormat
import java.util.*


class ConversationFragment : Fragment(R.layout.fragment_conversation) {

    private val viewModel: ConversationViewModel by viewModels()
    private val binding by viewBinding(FragmentConversationBinding::bind)

    private val adapter = XPagedAdapter(R.layout.conversation_item, ::bindConversation)
    private val dateFormat = SimpleDateFormat("hh.mm aa")

    private fun bindConversation(view: View, item: LocalConversation, pos: Int) {
        val bind = ConversationItemBinding.bind(view)

        bind.dpIv.load(avatar(item.otherName))
        bind.userNametv.text = item.otherName
        bind.statusTxtTv.text = item.lastText
        val date = Date(item.createTime)
        bind.timeTv.text = dateFormat.format(date)
        bind.root.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            println("Setting the CHAT ID to be ${item.conversationId}")
            intent.putExtra(ChatActivity.CONVERSATION_ID,item.conversationId)
            startActivity(intent)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.converations.layoutManager = LinearLayoutManager(context)
        binding.converations.adapter = adapter

        viewModel.loadConversations()

        viewModel.conversation.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }

    }

}
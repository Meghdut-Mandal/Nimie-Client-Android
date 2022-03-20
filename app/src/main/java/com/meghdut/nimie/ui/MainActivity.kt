package com.meghdut.nimie.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityMainBinding
import com.meghdut.nimie.databinding.ConversationItemBinding
import com.meghdut.nimie.databinding.LayoutTextPromptBinding
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.model.uistate.ApiUIState
import com.meghdut.nimie.ui.util.GenericAdapter
import com.meghdut.nimie.ui.util.snackBar
import com.meghdut.nimie.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val binding  by viewBinding(ActivityMainBinding::bind)


    private val statusAdapter = GenericAdapter(R.layout.conversation_item, ::bindConversation)
    private val dateFormat = SimpleDateFormat("hh.mm aa")

    private fun bindConversation(view: View, localStatus: LocalStatus, i: Int) {
        val bind = ConversationItemBinding.bind(view)

        bind.dpIv.load(localStatus.avatar)
        bind.userNametv.text = localStatus.userName
        bind.statusTxtTv.text = localStatus.text
        val date = Date(localStatus.createdTime)
        bind.timeTv.text = dateFormat.format(date)

        bind.root.setOnClickListener {
            openReplyStatusDialog(localStatus.statusId)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadStatus()
        binding.allStatusRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = statusAdapter
        }

        binding.addStatusButton.setOnClickListener {
            openAddStatusDialog()
        }

        addLiveDataObservers()
    }

    private fun addLiveDataObservers() {
        viewModel.getActiveUser().observe(this) {
            it?.let {
                binding.userDp.load(it.avatar)
                binding.userName.text = it.name
            }
        }
        viewModel.addStatusLiveData.observe(this) { state ->
            when (state) {
                is ApiUIState.Uninitialised -> {

                }
                is ApiUIState.Loading -> {
                    snackBar(binding.root, "Adding Status..")
                }
                is ApiUIState.Error -> {
                    snackBar(binding.root, " Error ${state.error}")
                }

                is ApiUIState.Done -> {
                    snackBar(binding.root, "Status Added ${state.result.linkId}")
                }
            }
        }

        viewModel.getStatues().observe(this) {state ->
            state?.let {
                statusAdapter.submitList(state)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        viewModel.replyConLiveData.observe(this){state ->
            when (state) {
                is ApiUIState.Uninitialised -> {

                }
                is ApiUIState.Loading -> {
                    snackBar(binding.root, "Replying to Status ${state.status}")
                }
                is ApiUIState.Error -> {
                    snackBar(binding.root, " Error ${state.error}")
                }

                is ApiUIState.Done -> {
                    snackBar(binding.root, "Reply Done ${state.result.statusId}")
                }
            }
        }
    }

    private fun openAddStatusDialog() {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.layout_text_prompt, scrollable = true, horizontalPadding = true)
            val addStatusBinding = LayoutTextPromptBinding.bind(getCustomView())
            addStatusBinding.textInputLayout3.hint="Status text"
            title(text = "Add Status")

            positiveButton(text = "Save") {
                val status = addStatusBinding.inputText.text
                viewModel.addStatus(status.toString())
            }
            negativeButton(text = "Cancel") {

            }
        }
    }

    private fun openReplyStatusDialog(statusId:Long) {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.layout_text_prompt, scrollable = true, horizontalPadding = true)
            val addStatusBinding = LayoutTextPromptBinding.bind(getCustomView())
            addStatusBinding.textInputLayout3.hint="Reply text"
            title(text = "Reply to Status")

            positiveButton(text = "Reply") {
                val replyText = addStatusBinding.inputText.text
                viewModel.replyStatus(replyText.toString(),statusId)
            }
            negativeButton(text = "Cancel") {

            }
        }
    }
}
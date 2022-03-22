package com.meghdut.nimie.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ConversationItemBinding
import com.meghdut.nimie.databinding.FragmentStatusFragmentsBinding
import com.meghdut.nimie.databinding.LayoutTextPromptBinding
import com.meghdut.nimie.data.model.LocalStatus
import com.meghdut.nimie.ui.model.ApiUIState
import com.meghdut.nimie.ui.util.XListAdapter
import com.meghdut.nimie.ui.util.XPagedAdapter
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.util.snackBar
import com.meghdut.nimie.ui.viewmodel.StatusViewModel
import java.text.SimpleDateFormat
import java.util.*

class StatusFragment : Fragment(R.layout.fragment_status_fragments) {

    private val binding by viewBinding(FragmentStatusFragmentsBinding::bind)
    private val viewModel : StatusViewModel by viewModels()

    private val statusAdapter = XPagedAdapter(R.layout.conversation_item, ::bindStatus)
    private val dateFormat = SimpleDateFormat("hh.mm aa")

    private fun bindStatus(view: View, localStatus: LocalStatus, i: Int) {
        val bind = ConversationItemBinding.bind(view)

        bind.dpIv.load(avatar(localStatus.userName))
        bind.userNametv.text = localStatus.userName
        bind.statusTxtTv.text = localStatus.text
        val date = Date(localStatus.createdTime)
        bind.timeTv.text = dateFormat.format(date)


        bind.root.setOnClickListener {
            openReplyStatusDialog(localStatus.statusId)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
        viewModel.addStatusLiveData.observe(viewLifecycleOwner) { state ->
            binding.progress.isIndeterminate = false
            when (state) {
                is ApiUIState.Uninitialised -> {
                }
                is ApiUIState.Loading -> {
                    binding.progress.isIndeterminate = true
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

        viewModel.getStatues().observe(viewLifecycleOwner) { state ->
            state?.let {
                statusAdapter.submitData(lifecycle,state)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        viewModel.replyConLiveData.observe(viewLifecycleOwner){state ->
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
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
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
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
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
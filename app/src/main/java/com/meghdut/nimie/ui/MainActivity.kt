package com.meghdut.nimie.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityMainBinding
import com.meghdut.nimie.databinding.AddStatusBinding
import com.meghdut.nimie.databinding.ConversationItemBinding
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.model.uistate.ApiUIState
import com.meghdut.nimie.ui.util.GenericAdapter
import com.meghdut.nimie.ui.util.snackBar
import com.meghdut.nimie.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()

    private val statusAdapter = GenericAdapter(R.layout.conversation_item, ::bindConversation)
    private val dateFormat = SimpleDateFormat("hh.mm aa")

    private fun bindConversation(view: View, localStatus: LocalStatus, i: Int) {
        val bind = ConversationItemBinding.bind(view)

        bind.dpIv.load(localStatus.avatar)
        bind.userNametv.text = localStatus.userName
        bind.statusTxtTv.text = localStatus.text
        val date = Date(localStatus.createdTime)
        bind.timeTv.text = dateFormat.format(date)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadStatus()
        binding.allStatusRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = statusAdapter
        }

        viewModel.getStatues().observe(this@MainActivity) {
            it?.let {
                statusAdapter.submitList(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }


        viewModel.getActiveUser().observe(this) {
            it?.let {
                binding.userDp.load(it.avatar)
                binding.userName.text = it.name
            }
        }
        binding.addStatusButton.setOnClickListener {
            openAddStatusDialog()
        }



        viewModel.addStatusLiveData.observe(this) { state ->
            when (state) {
                is ApiUIState.Uninitialised -> {

                }
                is ApiUIState.Creating -> {
                    snackBar(binding.root, "Adding Status..")
                }
                is ApiUIState.Error -> {
                    snackBar(binding.root, " Error ${state.error}")
                }

                is ApiUIState.Done -> {
                    snackBar(binding.root, "Status Added ${state.localStatus.linkId}")

                }
            }
        }

    }

    private fun openAddStatusDialog() {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(R.layout.add_status, scrollable = true, horizontalPadding = true)
            title(text = "Add Status")

            positiveButton(text = "Save") {
                val addStatusBinding = AddStatusBinding.bind(it.getCustomView())
                val status = addStatusBinding.statusText.text
                viewModel.addStatus(status.toString())

            }
            negativeButton(text = "Cancel") {

            }
        }
    }
}
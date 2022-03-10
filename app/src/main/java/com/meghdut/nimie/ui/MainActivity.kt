package com.meghdut.nimie.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityMainBinding
import com.meghdut.nimie.databinding.AddStatusBinding
import com.meghdut.nimie.model.uistate.AddStatusUIState
import com.meghdut.nimie.ui.util.snackBar
import com.meghdut.nimie.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProvider(this)
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                is AddStatusUIState.Uninitialised -> {

                }
                is AddStatusUIState.Creating -> {
                    snackBar(binding.root, "Adding Status..")
                }
                is AddStatusUIState.Error -> {
                    snackBar(binding.root, " Error ${state.error}")
                }

                is AddStatusUIState.Done -> {
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
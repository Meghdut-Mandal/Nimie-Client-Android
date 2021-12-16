package com.meghdut.nimie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.meghdut.nimie.databinding.ActivitySplashBinding
import com.meghdut.nimie.model.uistate.SplashUIState
import com.meghdut.nimie.viewmodel.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProvider(this)
            .get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createUserButton.setOnClickListener {
            viewModel.createLocalUser()
        }

        viewModel.uiState.observe(this) {
            binding.animationView.visibility = View.INVISIBLE
            binding.createUserButton.visibility = View.INVISIBLE
            when (it) {
                is SplashUIState.Success -> {
                    snackBar(binding.root, "User made ! ${it.localUser.name}")
                }
                is SplashUIState.Working -> {
                    binding.animationView.visibility = View.VISIBLE
                }
                is SplashUIState.Uninitialised -> {
                    binding.createUserButton.visibility = View.VISIBLE
                }
                is SplashUIState.Error -> {
                    snackBar(binding.root, it.message)
                    binding.createUserButton.visibility = View.VISIBLE
                }
            }
        }


    }

    private fun snackBar(
        view: View,
        message: String
    ) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
            .show()
    }
}
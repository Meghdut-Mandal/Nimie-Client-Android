package com.meghdut.nimie.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.meghdut.nimie.databinding.ActivitySplashBinding
import com.meghdut.nimie.model.uistate.SplashUIState
import com.meghdut.nimie.ui.util.navigateTo
import com.meghdut.nimie.ui.util.snackBar
import com.meghdut.nimie.viewmodel.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel:SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createUserButton.setOnClickListener {
            viewModel.createLocalUser()
        }
        viewModel.checkActiveUser()

        viewModel.uiState.observe(this) {
            binding.animationView.visibility = View.INVISIBLE
            binding.createUserButton.visibility = View.INVISIBLE
            when (it) {
                is SplashUIState.Success -> {
                    snackBar(binding.root, "User made ! ${it.localUser.name}")
                    navigateTo(MainActivity::class.java)
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

}
package com.meghdut.nimie.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivitySplashBinding
import com.meghdut.nimie.databinding.ItemOnboardingContainerBinding
import com.meghdut.nimie.ui.model.SplashUIState
import com.meghdut.nimie.ui.util.*
import com.meghdut.nimie.ui.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkActiveUser()
        binding = ActivitySplashBinding.inflate(layoutInflater)

        val onBoardingViewPager = findViewById<ViewPager2>(R.id.introViewPager)
        onBoardingViewPager.adapter = OnBoardingAdapter(SplashUtils.getOnBoardingItems())


        onBoardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.pageIndicatorView.onPageScrolled(
                    position,
                    positionOffset,
                    positionOffsetPixels
                )
            }
        })


        binding.loginBtn.setOnClickListener {
            viewModel.createLocalUser()
        }



        viewModel.uiState.observe(this) {
            binding.animationView.visibility = View.INVISIBLE
            binding.loginBtn.visibility = View.INVISIBLE
            when (it) {
                is SplashUIState.Success -> {
                    navigateTo(MainActivity::class.java)
                }
                is SplashUIState.Working -> {
                    binding.animationView.visibility = View.VISIBLE
                }
                is SplashUIState.Uninitialised -> {
                    binding.loginBtn.visibility = View.VISIBLE
                }
                is SplashUIState.Error -> {
                    snackBar(binding.root, it.message)
                    binding.loginBtn.visibility = View.VISIBLE
                }
            }
        }


    }

//    private fun bindOnboarding(view: View, item: OnBoardingItem, i: Int) {
//        val binding = ItemOnboardingContainerBinding.bind(view)
//        binding.onBoardingIv.setImageResource(item.imageSrcId)
//        binding.title.text = resources.getText(item.titleSrcId)
//        binding.description.text = resources.getText(item.descriptionSrcId)
//    }

}
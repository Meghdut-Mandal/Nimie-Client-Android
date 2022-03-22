package com.meghdut.nimie.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ActivityMainBinding
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.util.navigateTo
import com.meghdut.nimie.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewPager by lazy { binding.viewPager }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLiveDataObservers()
        binding.logOut.setOnClickListener {
            viewModel.logOutUser()
        }
        viewPager.adapter = pagerAdapter
    }

    private fun addLiveDataObservers() {
        viewModel.getActiveUser().observe(this) {
            it?.let {
                binding.userDp.load(avatar(it.name))
                binding.userName.text = it.name
            }
            if (it == null) {
                navigateTo(SplashActivity::class.java)
            }
        }
    }

    private val pagerAdapter = object : FragmentStateAdapter(this) {

        val map: Map<Int, Fragment> by lazy {
            mapOf(
                0 to StatusFragment(),
                1 to ConversationFragment()
            )
        }

        override fun getItemCount() = map.size

        override fun createFragment(position: Int): Fragment {

            return map[position]!!
        }

    }

}
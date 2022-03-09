package com.meghdut.nimie.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.meghdut.nimie.R
import com.meghdut.nimie.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProvider(this)
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
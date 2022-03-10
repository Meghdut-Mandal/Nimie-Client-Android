package com.meghdut.nimie.ui.util

import android.app.Activity
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun Activity.navigateTo(targetActivity: Class<*>) {
    val intent = Intent(this, targetActivity)
    startActivity(intent)
    finishAffinity()
}

fun snackBar(
    view: View,
    message: String
) {
    Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
        .show()
}
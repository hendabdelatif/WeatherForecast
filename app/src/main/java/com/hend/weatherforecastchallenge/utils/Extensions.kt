package com.hend.weatherforecastchallenge.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Extension to get City Names List
 * Handled if there is (,) in the end
 */
fun String.getCityNamesList(): List<String> {
    if (this.trim().endsWith(","))
        return this.dropLast(1).split(",").map { it.trim() }
    return this.split(",").map { it.trim() }
}

/**
 * Extension to force hide keyboard
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}




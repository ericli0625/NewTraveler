package com.example.eric.newtraveler.extension

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.isShow(): Boolean = visibility == View.VISIBLE

fun View.showOrHide(isValid: Boolean?) {
    if (isValid == true) {
        show()
    } else {
        hide()
    }
}
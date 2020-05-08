package com.example.eric.newtraveler.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        subscribeObservers()
    }

    @CallSuper
    protected open fun subscribeObservers() {

    }

    /***** Subscribe methods implementation *****/

}
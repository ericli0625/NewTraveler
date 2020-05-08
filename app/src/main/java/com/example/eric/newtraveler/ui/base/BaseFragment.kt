package com.example.eric.newtraveler.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    @CallSuper
    protected open fun subscribeObservers() {

    }
}
package com.app.farm.appUI.fragments

import android.content.Context
import com.app.farm.utilities.MyPreferences
import io.reactivex.disposables.Disposable

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    lateinit var mContext: Context
    var disposable: Disposable? = null
    var myPreferences: MyPreferences? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        myPreferences = MyPreferences(mContext)

    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}
package com.app.farm.appUI.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.farm.utilities.MyPreferences
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity()  {

    var disposable: Disposable? = null

    val mContext: Context
    get() = this

    var myPreferences: MyPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myPreferences = MyPreferences(mContext)
    }

    override fun onDestroy() {
        //We dispose here, so that it will not get update after activity is destroyed.
        disposable?.dispose()
        super.onDestroy()
    }

    open fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

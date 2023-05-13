package com.app.farm.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet

open class MyEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    override fun setError(error: CharSequence?, icon: Drawable?) {
        setCompoundDrawables(null, null, icon, null)
    }
}
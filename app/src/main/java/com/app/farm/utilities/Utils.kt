package com.app.farm.utilities

import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.farm.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

object Utils {

    fun checkAccessToken(mContext: Context) {
        if (Constant.authentication_header_token.isEmpty()) {
            Constant.authentication_header_token =
                MyPreferences(mContext).getString(Constant.API_TOKEN).toString()
        }
        Log.d("api Token: ", Constant.authentication_header_token)
    }

    fun isConnectedToInternet(context: Context): Boolean {
        var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = true
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    result = true
                }
            }
        }
        return result
    }

    fun handleErrorMessage(errorBodyResponse: ResponseBody?, context: Context?) {
        val error = StringBuilder()
        try {
            if (errorBodyResponse == null) {
                error("Assertion failed")
            }
            val ereader = BufferedReader(
                InputStreamReader(
                    errorBodyResponse.byteStream()
                )
            )
            var eline: String? = null
            while (ereader.readLine().also { eline = it } != null) {
                error.append(eline)
            }
            ereader.close()
        } catch (e: java.lang.Exception) {
            error.append(e.message)
        }

        try {
            val reader = JSONObject(error.toString())
            val message = reader.getString("message")
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getErrorMessage(mContext: Context, throwable: Throwable): String {
        return throwable.message.takeIf { throwable.message != null } ?: ""
    }

    fun isEditTextEmpty(
        context: Context,
        editText: MyEditText,
        errorMessage: TextView,
        string: String
    ): Boolean {
        return if (editText.text.toString().isBlank()) {
            setBgToError(context, editText, errorMessage, string)
            true
        } else {
            false
        }
    }

    fun validateEmail(
        context: Context,
        editText: MyEditText,
        errorMessage: TextView,
        string: String
    ): Boolean {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.text.toString().trim())
                .matches()
        ) {
            setBgToError(context, editText, errorMessage, string)
            true
        } else {
            false
        }
    }

    fun validatePassword(
        context: Context,
        editText: MyEditText,
        errorMessage: TextView,
        string: String
    ): Boolean {
        val pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\p{Alnum}]).{8,}\$")
        val matcher = pattern.matcher(editText.text.toString().trim())
        return if (!matcher.matches()) {
            setBgToError(context, editText, errorMessage, string)
            true
        } else
            false
    }

    fun isPasswordsAreSame(
        context: Context,
        password: MyEditText,
        confirmPassword: MyEditText,
        errorMessage: TextView,
        string: String
    ): Boolean {
        if (password.text.toString() != confirmPassword.text.toString()) {
            setBgToError(context, confirmPassword, errorMessage, string)
            return true
        }
        return false
    }

    fun setBgToNormal(context: Context, et: MyEditText, etError: TextView) {
        etError.visibility = View.GONE
        et.setError("", null)
        et.background = ContextCompat.getDrawable(context, R.drawable.et_bg_normal)
    }

    fun setBgToError(context: Context, et: MyEditText, etError: TextView, msg: String) {
        val errorIcon = ContextCompat.getDrawable(context, R.drawable.error)
        errorIcon!!.bounds = Rect(
            0,
            0,
            65,
            65
        )

        et.requestFocus()
        etError.visibility = View.VISIBLE
        et.setError("", errorIcon)
        etError.text = msg
        et.background = ContextCompat.getDrawable(context, R.drawable.et_bg_error)
    }

    fun checkNull(input: String?): String {
        return when (input) {
            null -> "NA"
            "" -> ""
            else -> input
        }
    }

}
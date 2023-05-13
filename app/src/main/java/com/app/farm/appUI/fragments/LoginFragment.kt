package com.app.farm.appUI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.app.farm.R
import com.app.farm.api.ApiClient
import com.app.farm.databinding.FragmentLoginBinding
import com.app.farm.models.login.LoginParamsModel
import com.app.farm.utilities.AlertMessage
import com.app.farm.utilities.Constant
import com.app.farm.utilities.CustomProgressDialogue
import com.app.farm.utilities.Utils
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class LoginFragment : BaseFragment() {

    private lateinit var progressDialog: CustomProgressDialogue

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        if (myPreferences!!.getBoolean(Constant.IS_NEW_USER)) {
            findNavController().navigate(R.id.action_nav_login_to_nav_dashboard)
        } else {
            progressDialog = CustomProgressDialogue(mContext)
            getLoginInit()
        }

        binding.loginMenu.setOnClickListener {
            val popup = PopupMenu(binding.loginMenu.context, binding.loginMenu)
            popup.menuInflater.inflate(R.menu.login_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_login -> {}
                    R.id.menu_register -> {
                        findNavController().navigate(R.id.action_nav_login_to_nav_register)
                    }
                }
                true
            }
            popup.show()
        }

        binding.btnLogin.setOnClickListener {
            if (checkValidations()) {
                progressDialog.show()
                loginAPI()
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        binding.email.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(mContext, binding.email, binding.emailError)
                } else {
                    Utils.setBgToError(
                        mContext,
                        binding.email,
                        binding.emailError,
                        mContext.getString(R.string.email_warning)
                    )
                }
            }
        }

        binding.password.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(requireContext(), binding.password, binding.passwordError)
                } else {
                    Utils.setBgToError(
                        requireContext(),
                        binding.password,
                        binding.passwordError,
                        mContext.getString(R.string.password_warning)
                    )
                }
            }
        }

        return binding.root
    }

    private fun checkValidations(): Boolean {
        return when {
            Utils.isEditTextEmpty(
                mContext,
                binding.email,
                binding.emailError,
                mContext.getString(R.string.email_warning)
            ) -> false

            Utils.validateEmail(
                mContext,
                binding.email,
                binding.emailError,
                mContext.getString(R.string.email_validation)
            ) -> false

            Utils.isEditTextEmpty(
                mContext,
                binding.password,
                binding.passwordError,
                mContext.getString(R.string.password_warning)
            ) -> false

            Utils.validatePassword(
                mContext,
                binding.password,
                binding.passwordError,
                mContext.getString(R.string.password_validation)
            ) -> false

            else -> true
        }
    }

    private fun getLoginInit() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }

        progressDialog.show()
        disposable = ApiClient.client.loginInit()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                disposable?.dispose()

                progressDialog.dismiss()

                if (it.isSuccessful) {
                    val responseBody = it.body()

                    if (responseBody != null) {

                        if (responseBody.success == true) {
                            binding.email.hint = Utils.checkNull(responseBody.email)
                            binding.password.hint = Utils.checkNull(responseBody.password)
                        }

                    } else {
                        progressDialog.dismiss()
                        AlertMessage.showMessage(mContext, "Data loading failed")
                    }

                } else {
                    progressDialog.dismiss()
                    Utils.handleErrorMessage(it.errorBody()!!, mContext)
                }
            }, {
                disposable?.dispose()
                progressDialog.dismiss()
                AlertMessage.showMessage(mContext, Utils.getErrorMessage(mContext, it))
            })
    }

    private fun loginAPI() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }

        val gson = Gson()
        val jsonParams = gson.toJson(
            LoginParamsModel(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        )

        val requestBody = jsonParams.toRequestBody("application/json".toMediaTypeOrNull())

        disposable = ApiClient.client.login(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                disposable?.dispose()

                progressDialog.dismiss()

                if (it.isSuccessful) {
                    val responseBody = it.body()

                    if (responseBody != null) {

                        if (responseBody.success == true) {

                            // Saving Token
                            Constant.authentication_header_token =
                                Utils.checkNull(responseBody.apiKey)
                            myPreferences!!.putString(
                                Constant.API_TOKEN,
                                Utils.checkNull(responseBody.apiKey)
                            )

                            // Saving Role using detail API
                            myPreferences!!.putBoolean(Constant.IS_NEW_USER, true)
                            myPreferences!!.putUserData(Constant.USER_OBJ, responseBody)

                            findNavController().navigate(R.id.action_nav_login_to_nav_dashboard)

                        } else {
                            val arr = responseBody.validationErrors
                            if (arr != null) {
                                if (arr.isNotEmpty()) {
                                    AlertMessage.showMessage(
                                        mContext,
                                        Utils.checkNull(arr[0].message)
                                    )
                                }
                            }
                        }

                    } else {
                        AlertMessage.showMessage(mContext, "Data loading failed")
                    }

                } else {
                    progressDialog.dismiss()
                    Utils.handleErrorMessage(it.errorBody()!!, mContext)
                }
            }, {
                disposable?.dispose()
                progressDialog.dismiss()
                AlertMessage.showMessage(mContext, Utils.getErrorMessage(mContext, it))
            })
    }

}
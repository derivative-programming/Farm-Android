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
import com.app.farm.databinding.FragmentRegisterBinding
import com.app.farm.models.register.RegisterParamsModel
import com.app.farm.utilities.AlertMessage
import com.app.farm.utilities.Constant
import com.app.farm.utilities.CustomProgressDialogue
import com.app.farm.utilities.Utils
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RegisterFragment : BaseFragment() {

    private lateinit var progressDialog: CustomProgressDialogue
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        progressDialog = CustomProgressDialogue(mContext)

        getRegisterInit()

        binding.btnRegister.setOnClickListener {
            if (checkValidations()) {
                registerAPI()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_nav_register_to_nav_login)
        }

        binding.email.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(requireContext(), binding.email, binding.emailError)
                } else {
                    Utils.setBgToError(
                        requireContext(),
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
                        mContext.getString(R.string.confirm_password_error)
                    )
                }
            }
        }

        binding.confirmPassword.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(
                        requireContext(),
                        binding.confirmPassword,
                        binding.confirmPasswordError
                    )
                } else {
                    Utils.setBgToError(
                        requireContext(),
                        binding.confirmPassword,
                        binding.confirmPasswordError,
                        mContext.getString(R.string.confirm_password_error)
                    )
                }
            }
        }

        binding.firstName.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(requireContext(), binding.firstName, binding.firstNameError)
                } else {
                    Utils.setBgToError(
                        requireContext(),
                        binding.firstName,
                        binding.firstNameError,
                        mContext.getString(R.string.first_name_error)
                    )
                }
            }
        }

        binding.lastName.doAfterTextChanged {
            if (it != null) {
                if (it.isNotEmpty()) {
                    Utils.setBgToNormal(requireContext(), binding.lastName, binding.lastNameError)
                } else {
                    Utils.setBgToError(
                        requireContext(),
                        binding.lastName,
                        binding.lastNameError,
                        mContext.getString(R.string.last_name_error)
                    )
                }
            }
        }

        binding.registerMenu.setOnClickListener {
            val popup = PopupMenu(binding.registerMenu.context, binding.registerMenu)
            popup.menuInflater.inflate(R.menu.register_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_login -> {
                        findNavController().navigate(R.id.action_nav_register_to_nav_login)
                    }

                    R.id.menu_register -> {}
                }
                true
            }
            popup.show()
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

            Utils.isEditTextEmpty(
                mContext,
                binding.confirmPassword,
                binding.confirmPasswordError,
                mContext.getString(R.string.confirm_password_error)
            ) -> false

            Utils.validatePassword(
                mContext,
                binding.confirmPassword,
                binding.confirmPasswordError,
                mContext.getString(R.string.password_validation)
            ) -> false

            Utils.isPasswordsAreSame(
                mContext,
                binding.password,
                binding.confirmPassword,
                binding.confirmPasswordError,
                mContext.getString(R.string.the_passwords_do_not_match)

            ) -> false

            Utils.isEditTextEmpty(
                mContext,
                binding.firstName,
                binding.firstNameError,
                mContext.getString(R.string.first_name_error)
            ) -> false

            Utils.isEditTextEmpty(
                mContext,
                binding.lastName,
                binding.lastNameError,
                mContext.getString(R.string.last_name_error)
            ) -> false

            else -> true
        }
    }

    private fun getRegisterInit() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }

        progressDialog.show()
        disposable = ApiClient.client.registerInit()
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
                            binding.confirmPassword.hint =
                                Utils.checkNull(responseBody.confirmPassword)
                            binding.firstName.hint = Utils.checkNull(responseBody.firstName)
                            binding.lastName.hint = Utils.checkNull(responseBody.lastName)
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

    private fun registerAPI() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }

        val gson = Gson()
        val jsonParams = gson.toJson(
            RegisterParamsModel(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),
                binding.confirmPassword.text.toString().trim(),
                binding.firstName.text.toString().trim(),
                binding.lastName.text.toString().trim(),
            )
        )

        val requestBody = jsonParams.toRequestBody("application/json".toMediaTypeOrNull())

        progressDialog.show()
        disposable = ApiClient.client.register(requestBody)
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

                            findNavController().navigate(R.id.action_nav_register_to_nav_dashboard)

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
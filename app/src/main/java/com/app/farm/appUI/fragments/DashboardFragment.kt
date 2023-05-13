package com.app.farm.appUI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.app.farm.R
import com.app.farm.api.ApiClient
import com.app.farm.databinding.FragmentDashboardBinding
import com.app.farm.utilities.AlertMessage
import com.app.farm.utilities.CustomProgressDialogue
import com.app.farm.utilities.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DashboardFragment : BaseFragment() {

    private lateinit var progressDialog: CustomProgressDialogue

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        progressDialog = CustomProgressDialogue(mContext)
        getDashboardInit()

        binding.dashboardMenu.setOnClickListener {
            val popup = PopupMenu(binding.dashboardMenu.context, binding.dashboardMenu)
            popup.menuInflater.inflate(R.menu.dashboard_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_dashboard -> {}

                    R.id.menu_logout -> {
                        logout()
                    }
                }
                true
            }
            popup.show()
        }

        return binding.root
    }

    private fun logout() {
        myPreferences!!.clear()
        findNavController().navigate(R.id.action_nav_dashboard_to_nav_login)
    }

    private fun getDashboardInit() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }
        Utils.checkAccessToken(mContext)

        progressDialog.show()
        disposable = ApiClient.client.dashboardInit()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                disposable?.dispose()

                progressDialog.dismiss()

                if (it.isSuccessful) {
                    val responseBody = it.body()

                    if (responseBody != null) {

                        if (responseBody.success == true) {
                            dashboardAPI()
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

    private fun dashboardAPI() {

        if (!Utils.isConnectedToInternet(mContext)) {
            AlertMessage.showMessage(mContext, R.string.no_internet_connection)
            return
        }
        Utils.checkAccessToken(mContext)

        disposable = ApiClient.client.dashboard()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                disposable?.dispose()

                progressDialog.dismiss()

                if (it.isSuccessful) {
                    val responseBody = it.body()

                    if (responseBody != null) {

                        if (responseBody.success == true) {
                            //Everything is good
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
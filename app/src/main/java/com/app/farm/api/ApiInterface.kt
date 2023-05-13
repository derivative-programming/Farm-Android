package com.app.farm.api

import com.app.farm.models.login.LoginInitModel
import com.app.farm.models.dashboard.DashboardInitModel
import com.app.farm.models.dashboard.DashboardModel
import com.app.farm.models.login.LoginResponseModel
import com.app.farm.models.register.RegisterInitModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    /* --- Login APIs --- */

    @GET(APIConstants.logInInit)
    fun loginInit(): Observable<Response<LoginInitModel>>

    @POST(APIConstants.logIn)
    fun login(@Body params: RequestBody): Observable<Response<LoginResponseModel>>

    /* --- Register APIs --- */

    @GET(APIConstants.registerInit)
    fun registerInit(): Observable<Response<RegisterInitModel>>

    @POST(APIConstants.register)
    fun register(@Body params: RequestBody): Observable<Response<LoginResponseModel>>

    /* --- Dashboard APIs --- */

    @GET(APIConstants.dashboardInit)
    fun dashboardInit(): Observable<Response<DashboardInitModel>>

    @GET(APIConstants.dashboard)
    fun dashboard(): Observable<Response<DashboardModel>>

}
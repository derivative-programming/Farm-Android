package com.app.farm.models.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardInitModel (

    @SerializedName("customerCode"     ) var customerCode     : String?  = null,
    @SerializedName("success"          ) var success          : Boolean? = null,
    @SerializedName("message"          ) var message          : String?  = null,
    @SerializedName("validationErrors" ) var validationErrors : String?  = null

)
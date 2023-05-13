package com.app.farm.models.login

import com.google.gson.annotations.SerializedName

data class LoginInitModel (

    @SerializedName("email"            ) var email            : String?  = null,
    @SerializedName("password"         ) var password         : String?  = null,
    @SerializedName("success"          ) var success          : Boolean? = null,
    @SerializedName("message"          ) var message          : String?  = null,
    @SerializedName("validationErrors" ) var validationErrors : String?  = null

)
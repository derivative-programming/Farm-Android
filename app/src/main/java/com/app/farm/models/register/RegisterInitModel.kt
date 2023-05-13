package com.app.farm.models.register

import com.google.gson.annotations.SerializedName

data class RegisterInitModel (

    @SerializedName("email"            ) var email            : String?  = null,
    @SerializedName("password"         ) var password         : String?  = null,
    @SerializedName("confirmPassword"  ) var confirmPassword  : String?  = null,
    @SerializedName("firstName"        ) var firstName        : String?  = null,
    @SerializedName("lastName"         ) var lastName         : String?  = null,
    @SerializedName("success"          ) var success          : Boolean? = null,
    @SerializedName("message"          ) var message          : String?  = null,
    @SerializedName("validationErrors" ) var validationErrors : String?  = null

)
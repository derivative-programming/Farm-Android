package com.app.farm.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponseModel (

    @SerializedName("customerCode"       ) var customerCode       : String?                     = null,
    @SerializedName("email"              ) var email              : String?                     = null,
    @SerializedName("userCodeValue"      ) var userCodeValue      : String?                     = null,
    @SerializedName("uTCOffsetInMinutes" ) var uTCOffsetInMinutes : Int?                        = null,
    @SerializedName("roleNameCSVList"    ) var roleNameCSVList    : String?                     = null,
    @SerializedName("apiKey"             ) var apiKey             : String?                     = null,
    @SerializedName("success"            ) var success            : Boolean?                    = null,
    @SerializedName("message"            ) var message            : String?                     = null,
    @SerializedName("validationErrors"   ) var validationErrors   : ArrayList<ValidationErrors>? = arrayListOf()

)

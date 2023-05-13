package com.app.farm.models.login

import com.google.gson.annotations.SerializedName

data class ValidationErrors (

    @SerializedName("property" ) var property : String? = null,
    @SerializedName("message"  ) var message  : String? = null

)

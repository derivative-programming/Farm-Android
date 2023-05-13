package com.app.farm.models.dashboard

import com.google.gson.annotations.SerializedName

data class Request (

    @SerializedName("pageNumber"        ) var pageNumber        : Int?     = null,
    @SerializedName("itemCountPerPage"  ) var itemCountPerPage  : Int?     = null,
    @SerializedName("orderByColumnName" ) var orderByColumnName : String?  = null,
    @SerializedName("orderByDescending" ) var orderByDescending : Boolean? = null,
    @SerializedName("forceErrorMessage" ) var forceErrorMessage : String?  = null

)
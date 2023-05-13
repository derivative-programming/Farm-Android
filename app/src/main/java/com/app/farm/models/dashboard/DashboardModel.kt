package com.app.farm.models.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardModel (

    @SerializedName("pageNumber"        ) var pageNumber        : Int?             = null,
    @SerializedName("items"             ) var items             : ArrayList<DashboardItems> = arrayListOf(),
    @SerializedName("itemCountPerPage"  ) var itemCountPerPage  : Int?             = null,
    @SerializedName("orderByColumnName" ) var orderByColumnName : String?          = null,
    @SerializedName("orderByDescending" ) var orderByDescending : Boolean?         = null,
    @SerializedName("success"           ) var success           : Boolean?         = null,
    @SerializedName("recordsTotal"      ) var recordsTotal      : Int?             = null,
    @SerializedName("recordsFiltered"   ) var recordsFiltered   : Int?             = null,
    @SerializedName("message"           ) var message           : String?          = null,
    @SerializedName("appVersion"        ) var appVersion        : String?          = null,
    @SerializedName("request"           ) var request           : Request?         = Request()

)
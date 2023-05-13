package com.app.farm.api

object APIConstants {

    var BASE_URL = "https://dp-farm-pageapi.azurewebsites.net/api/v1_0/"

    // API Endpoints
    const val logInInit = "tac-login/00000000-0000-0000-0000-000000000000/init"
    const val logIn = "tac-login/00000000-0000-0000-0000-000000000000"

    const val registerInit = "tac-register/00000000-0000-0000-0000-000000000000/init"
    const val register = "tac-register/00000000-0000-0000-0000-000000000000"

    const val dashboardInit = "tac-farm-dashboard/00000000-0000-0000-0000-000000000000/init"
    const val dashboard = "https://dp-farm-pageapi.azurewebsites.net/api/v1_0/tac-farm-dashboard/{tacCode}"

}
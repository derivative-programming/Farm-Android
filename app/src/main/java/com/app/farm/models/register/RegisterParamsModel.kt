package com.app.farm.models.register

data class RegisterParamsModel(
    var email: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)
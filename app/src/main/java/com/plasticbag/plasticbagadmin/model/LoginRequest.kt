package com.plasticbag.plasticbagadmin.model

data class LoginRequest(
    val userId: String,
    val name: String,
    val phoneNo: String,
    val email: String
) {
    constructor(): this("", "", "", "")
}

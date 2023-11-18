package com.plasticbag.plasticbagadmin.model

data class AdminDetails(
    val name: String,
    val email: String,
    val phoneNo: String,
    val userId: String,
) {
    constructor(): this ("", "", "", "",)
}

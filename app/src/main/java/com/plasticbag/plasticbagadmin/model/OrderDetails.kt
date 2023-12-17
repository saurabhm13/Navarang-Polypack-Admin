package com.plasticbag.plasticbagadmin.model

data class OrderDetails(
    val orderId: String,
    val orderDateTime: String,
    val dispatchDateTime: String,
    val productDetails: ProductDetails,
    val userDetails: UserDetails

) {

    constructor(): this("", "", "", ProductDetails("", "", ""),
        UserDetails("", "", "", "", "", ""))
}

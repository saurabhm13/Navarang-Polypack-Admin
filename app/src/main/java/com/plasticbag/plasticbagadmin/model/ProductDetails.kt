package com.plasticbag.plasticbagadmin.model

data class ProductDetails(
    val productId: String,
    val image: String,
    val title: String,
    val quantity: String
) {
    constructor(): this("", "", "", "")
}

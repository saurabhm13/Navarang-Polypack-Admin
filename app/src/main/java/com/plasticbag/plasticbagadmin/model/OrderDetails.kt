package com.plasticbag.plasticbagadmin.model

data class OrderDetails(
//    val title: String,
//    val image : String,
//    val quantity: String,
//    val bayerAddress: String,
//    val bayerName: String,
//    val bayerPhoneNo: String,
//    val bayerUserId: String,

    val orderId: String,
    val productDetails: ProductDetails,
    val userDetails: UserDetails

) {

    constructor(): this("", ProductDetails("", "", ""),
        UserDetails("", "", "", "", "", ""))
//    constructor(): this("", "", "", "", "", "", "")
}

package com.plasticbag.plasticbagadmin.presentation.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbagadmin.model.OrderDetails
import com.plasticbag.plasticbagadmin.model.UserOrderDetails
import com.plasticbag.plasticbagadmin.util.Constants.Companion.ADMIN
import com.plasticbag.plasticbagadmin.util.Constants.Companion.DELIVERED
import com.plasticbag.plasticbagadmin.util.Constants.Companion.DISPATCH
import com.plasticbag.plasticbagadmin.util.Constants.Companion.ORDERS
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PENDING
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCTS
import com.plasticbag.plasticbagadmin.util.Constants.Companion.QUANTITY
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var _pendingOrderLiveData = MutableLiveData<List<OrderDetails>>()
    val pendingOrderLiveData: LiveData<List<OrderDetails>> get() = _pendingOrderLiveData

    private var _dispatchOrderLiveData = MutableLiveData<List<OrderDetails>>()
    val dispatchOrderLiveData: LiveData<List<OrderDetails>> get() = _dispatchOrderLiveData

    private var _deliveredOrderLiveData = MutableLiveData<List<OrderDetails>>()
    val deliveredOrderLiveData: LiveData<List<OrderDetails>> get() = _deliveredOrderLiveData

    private var _productQuantityLiveData = MutableLiveData<String>()
    val productQuantityLiveData: LiveData<String> get() = _productQuantityLiveData

    var successCallBack: (() -> Unit)? = null
    var errorCallBack: ((String) -> Unit)? = null

    fun getPendingOrder() {
        database.child(ADMIN).child(ORDERS).child(PENDING).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderDetails>()

                for (orderSnapshot in snapshot.children) {
                    for (productSnapshot in orderSnapshot.children) {
                        val order = productSnapshot.getValue(OrderDetails::class.java)
                        order?.let {
                            orderList.add(order)
                        }
                    }
                }

                _pendingOrderLiveData.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun getDispatchOrder() {
        database.child(ADMIN).child(ORDERS).child(DISPATCH).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderDetails>()

                for (orderSnapshot in snapshot.children) {
                    for (productSnapshot in orderSnapshot.children) {
                        val order = productSnapshot.getValue(OrderDetails::class.java)
                        order?.let {
                            orderList.add(order)
                        }
                    }
                }

                _dispatchOrderLiveData.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun getDeliveredOrder() {
        database.child(ADMIN).child(ORDERS).child(DELIVERED).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderDetails>()

                for (dataSnapshot in snapshot.children) {
                    val order = dataSnapshot.getValue(OrderDetails::class.java)
                    order?.let {
                        orderList.add(order)
                    }
                }

                _deliveredOrderLiveData.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun addQuantity(order: OrderDetails, productQuantity: String) {

        val newQuantity = order.productDetails.quantity.toInt()+1

        if (productQuantity != "null" && productQuantity.toInt() > 0) {
            database.child(ADMIN).child(ORDERS).child(PENDING).child(order.orderId).child(order.productDetails.productId).child("productDetails").child(
                QUANTITY).setValue(newQuantity.toString())
                .addOnSuccessListener {
                    successCallBack?.invoke()
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                }

            database.child(USER).child(order.userDetails.userId).child(ORDERS).child(PENDING).child(order.orderId).child(order.productDetails.productId).child(
                QUANTITY).setValue(newQuantity.toString())

            database.child(PRODUCTS).child(order.productDetails.productId).child(QUANTITY).setValue((productQuantity.toInt()-1).toString())
        }

    }

    fun minusQuantity(order: OrderDetails, productQuantity: String) {

        val newQuantity = order.productDetails.quantity.toInt()-1

        if (productQuantity != "null" && newQuantity >= 1) {
            database.child(ADMIN).child(ORDERS).child(PENDING).child(order.orderId).child(order.productDetails.productId).child("productDetails").child(
                QUANTITY).setValue(newQuantity.toString())
                .addOnSuccessListener {
                    successCallBack?.invoke()
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                }

            database.child(USER).child(order.userDetails.userId).child(ORDERS).child(PENDING).child(order.productDetails.productId).child(
                QUANTITY).setValue(newQuantity.toString())

            database.child(PRODUCTS).child(order.productDetails.productId).child(QUANTITY).setValue((productQuantity.toInt()+1).toString())
        }else {
            // TODO show dialog
        }
    }

    fun deleteOrder(order: OrderDetails) {
        database.child(ADMIN).child(ORDERS).child(PENDING).child(order.orderId).removeValue()
            .addOnSuccessListener {
                successCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }

        database.child(USER).child(order.userDetails.userId).child(ORDERS).child(PENDING).child(order.orderId).removeValue()
    }

    fun addToDispatch(order: OrderDetails) {

        val dispatchOrder = OrderDetails(order.orderId, order.orderDateTime, getCurrentDate(), order.productDetails, order.userDetails)
        val userOrderDetails = UserOrderDetails(order.orderId, order.orderDateTime, getCurrentDate(),
        order.productDetails.productId, order.productDetails.title, order.productDetails.quantity)

        database.child(ADMIN).child(ORDERS).child(DISPATCH).child(order.orderId).child(order.productDetails.productId).setValue(dispatchOrder)
            .addOnSuccessListener {
                successCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }

        database.child(USER).child(order.userDetails.userId).child(ORDERS).child(DISPATCH).child(order.orderId).child(order.productDetails.productId).setValue(userOrderDetails)

        database.child(ADMIN).child(ORDERS).child(PENDING).child(order.orderId).removeValue()
            .addOnSuccessListener {
                successCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }

        database.child(USER).child(order.userDetails.userId).child(ORDERS).child(PENDING).child(order.orderId).removeValue()

    }

    fun addToDelivered(order: OrderDetails) {
        database.child(ADMIN).child(ORDERS).child(DELIVERED).child(order.productDetails.productId).setValue(order)
            .addOnSuccessListener {
                successCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }

        database.child(USER).child(order.userDetails.userId).child(ORDERS).child(DELIVERED).child(order.productDetails.productId).setValue(order.productDetails)

        database.child(ADMIN).child(ORDERS).child(DISPATCH).child(order.productDetails.productId).removeValue()
            .addOnSuccessListener {
                successCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }

        database.child(USER).child(order.userDetails.userId).child(ORDERS).child(DISPATCH).child(order.productDetails.productId).removeValue()

    }

    fun deleteDispatchOrder(order: OrderDetails) {
        database.child(ADMIN).child(ORDERS).child(DISPATCH).child(order.orderId).removeValue()
    }

    fun getProductQuantity(productId: String): String {
        var quantity: String = ""
        database.child(PRODUCTS).child(productId).child(QUANTITY).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                quantity = snapshot.value.toString()
                _productQuantityLiveData.value = quantity
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })

        return quantity
    }

    suspend fun getProductQuantity1(productId: String, callback: (String) -> Unit) = coroutineScope {
        var quantity: String = ""
        val job = async {
            database.child(PRODUCTS).child(productId).child(QUANTITY).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    quantity = snapshot.value.toString()
                    _productQuantityLiveData.value = quantity
                    callback(quantity)
                }

                override fun onCancelled(error: DatabaseError) {
                    quantity = "0"
                    callback(quantity)
                }

            })

        }
        job.await()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }


}
package com.plasticbag.plasticbagadmin.presentation.add_edit_product

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.plasticbag.plasticbagadmin.model.ProductDetails
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCTS
import com.plasticbag.plasticbagadmin.util.Constants.Companion.QUANTITY
import com.plasticbag.plasticbagadmin.util.Constants.Companion.TITLE
import java.util.UUID

class AddEditProductViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference

    var successCallback: (() -> Unit)? = null
    var errorCallBack: ((String) -> Unit)? = null
    var deleteCallBack: (() -> Unit)? = null

    fun saveProductToFirebase(title: String, quantity: String) {

//        val imageUri: Uri = image
//        val imageRef = storageReference.child("products/images/${UUID.randomUUID()}")
//        val uploadTask = imageRef.putFile(imageUri)

//        uploadTask.addOnSuccessListener { taskSnapshot ->
//            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                val imageUrl = uri.toString()
//
//                val newChildRef = database.child(PRODUCTS).push()
//                val pushKey = newChildRef.key
//                val productDetails = ProductDetails(pushKey!!, imageUrl, title, quantity)
//                newChildRef.setValue(productDetails)
//
//                successCallback?.invoke()
//
//            }.addOnFailureListener { exception ->
//                errorCallback?.invoke()
//            }
//        }.addOnFailureListener { exception ->
//            errorCallback?.invoke()
//        }

        val newChildRef = database.child(PRODUCTS).push()
        val pushKey = newChildRef.key
        val productDetails = ProductDetails(pushKey!!, title, quantity)
        newChildRef.setValue(productDetails)

        successCallback?.invoke()
    }

    fun editTitleQuantityToFirebase(productId: String, title: String, quantity: String) {
        val product = ProductDetails(productId, title, quantity)

        database.child(PRODUCTS).child(productId).setValue(product)
            .addOnSuccessListener {
                successCallback?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }
    }

    fun editProductInFirebase(image: Uri, productId: String, title: String, quantity: String) {
        val imageUri: Uri = image
        val imageRef = storageReference.child("products/images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                val productDetails = ProductDetails(productId, title, quantity)

                database.child(PRODUCTS).child(productId).setValue(productDetails)

                successCallback?.invoke()

            }.addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }
        }.addOnFailureListener {
            it.message?.let { it1 -> errorCallBack?.invoke(it1) }
        }
    }

    fun deleteProduct(productId: String) {
        database.child(PRODUCTS).child(productId).removeValue()
            .addOnSuccessListener {
                deleteCallBack?.invoke()
            }
            .addOnFailureListener {
                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
            }
    }

}
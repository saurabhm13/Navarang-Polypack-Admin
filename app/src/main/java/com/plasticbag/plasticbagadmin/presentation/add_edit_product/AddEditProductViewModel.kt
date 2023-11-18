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
    var errorCallback: (() -> Unit)? = null

    fun saveProductToFirebase(image: Uri, title: String, quantity: String) {

        val imageUri: Uri = image
        val imageRef = storageReference.child("products/images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                val newChildRef = database.child(PRODUCTS).push()
                val pushKey = newChildRef.key
                val productDetails = ProductDetails(pushKey!!, imageUrl, title, quantity)
                newChildRef.setValue(productDetails)

                successCallback?.invoke()

            }.addOnFailureListener { exception ->
                errorCallback?.invoke()
            }
        }.addOnFailureListener { exception ->
            errorCallback?.invoke()
        }
    }

    fun editTitleQuantityToFirebase(productId: String, title: String, quantity: String) {
        database.child(PRODUCTS).child(productId).child(TITLE).setValue(title)
        database.child(PRODUCTS).child(productId).child(QUANTITY).setValue(quantity)
            .addOnSuccessListener {
                successCallback?.invoke()
            }
            .addOnFailureListener {
                errorCallback?.invoke()
            }
    }

    fun editProductInFirebase(image: Uri, productId: String, title: String, quantity: String) {
        val imageUri: Uri = image
        val imageRef = storageReference.child("products/images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                val productDetails = ProductDetails(productId, imageUrl, title, quantity)

                database.child(PRODUCTS).child(productId).setValue(productDetails)

                successCallback?.invoke()

            }.addOnFailureListener { exception ->
                errorCallback?.invoke()
            }
        }.addOnFailureListener { exception ->
            errorCallback?.invoke()
        }
    }

}
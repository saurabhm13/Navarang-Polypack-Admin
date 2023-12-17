package com.plasticbag.plasticbagadmin.presentation.login_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbagadmin.model.LoginRequest
import com.plasticbag.plasticbagadmin.util.Constants
import com.plasticbag.plasticbagadmin.util.Constants.Companion.ADMIN
import com.plasticbag.plasticbagadmin.util.Constants.Companion.NOT_VERIFIED
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER_DETAILS
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER_LOGIN
import com.plasticbag.plasticbagadmin.util.Constants.Companion.VERIFIED

class LoginRequestViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var _verifiedUsersLiveData = MutableLiveData<List<LoginRequest>>()
    val verifiedUsersLiveData: LiveData<List<LoginRequest>> get() = _verifiedUsersLiveData

    private var _notVerifiedUsersLiveData = MutableLiveData<List<LoginRequest>>()
    val notVerifiedUsersLiveData: LiveData<List<LoginRequest>> get() = _notVerifiedUsersLiveData

    var errorCallBack: ((String) -> Unit)? = null

    fun getNotVerifiedUsers() {
        database.child(ADMIN).child(USER_LOGIN).child(NOT_VERIFIED).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loginList = mutableListOf<LoginRequest>()

                for (dataSnapshot in snapshot.children) {
                    val login = dataSnapshot.getValue(LoginRequest::class.java)

                    login?.let {
                        loginList.add(login)
                    }
                }

                _notVerifiedUsersLiveData.value = loginList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun getVerifiedUsers() {
        database.child(Constants.ADMIN).child(Constants.USER_LOGIN).child(VERIFIED).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loginList = mutableListOf<LoginRequest>()

                for (dataSnapshot in snapshot.children) {
                    val login = dataSnapshot.getValue(LoginRequest::class.java)

                    login?.let {
                        loginList.add(login)
                    }
                }

                _verifiedUsersLiveData.value = loginList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun approveLoginRequest(loginRequest: LoginRequest) {
        database.child(USER).child(loginRequest.userId).child(USER_DETAILS).child("userVerified").setValue(true)

        database.child(ADMIN).child(USER_LOGIN).child(VERIFIED).child(loginRequest.userId).setValue(loginRequest)

        database.child(ADMIN).child(USER_LOGIN).child(NOT_VERIFIED).child(loginRequest.userId).removeValue()
    }

    fun denyLoginRequest(loginRequest: LoginRequest) {
        database.child(USER).child(loginRequest.userId).child(USER_DETAILS).child("userVerified").setValue(false)

        database.child(ADMIN).child(USER_LOGIN).child(NOT_VERIFIED).child(loginRequest.userId).setValue(loginRequest)

        database.child(ADMIN).child(USER_LOGIN).child(VERIFIED).child(loginRequest.userId).removeValue()
    }


}
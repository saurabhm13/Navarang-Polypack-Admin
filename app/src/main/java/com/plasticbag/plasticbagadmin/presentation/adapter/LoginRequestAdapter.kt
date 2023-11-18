package com.plasticbag.plasticbagadmin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.LoginReqListItemBinding
import com.plasticbag.plasticbagadmin.databinding.OrderListItemBinding
import com.plasticbag.plasticbagadmin.model.LoginRequest

class LoginRequestAdapter(
    private var onApproveClick: (LoginRequest) -> Unit,
    private var onDenyClick: (LoginRequest) -> Unit
): RecyclerView.Adapter<LoginRequestAdapter.LoginRequestViewHolder>() {

    private var loginRequestList = ArrayList<LoginRequest>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(loginRequestList: List<LoginRequest>) {
        this.loginRequestList.clear()
        this.loginRequestList.addAll(loginRequestList)
        notifyDataSetChanged()
    }

    class LoginRequestViewHolder(val binding: LoginReqListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginRequestViewHolder {
        return LoginRequestViewHolder(LoginReqListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return loginRequestList.size
    }

    override fun onBindViewHolder(holder: LoginRequestViewHolder, position: Int) {

        holder.binding.name.text = loginRequestList[position].name
        holder.binding.phoneNo.text = loginRequestList[position].phoneNo
        holder.binding.email.text = loginRequestList[position].email

        holder.binding.approve.setOnClickListener {
            onApproveClick.invoke(loginRequestList[position])
        }

        holder.binding.deny.setOnClickListener {
            onDenyClick.invoke(loginRequestList[position])
        }

    }


}
package com.plasticbag.plasticbagadmin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plasticbag.plasticbagadmin.databinding.NotVerifiedListItemBinding
import com.plasticbag.plasticbagadmin.model.LoginRequest

class NotVerifiedAdapter(
    private var onApproveClick: (LoginRequest) -> Unit
): RecyclerView.Adapter<NotVerifiedAdapter.LoginRequestViewHolder>() {

    private var loginRequestList = ArrayList<LoginRequest>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(loginRequestList: List<LoginRequest>) {
        this.loginRequestList.clear()
        this.loginRequestList.addAll(loginRequestList)
        notifyDataSetChanged()
    }

    class LoginRequestViewHolder(val binding: NotVerifiedListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginRequestViewHolder {
        return LoginRequestViewHolder(NotVerifiedListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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

    }


}
package com.plasticbag.plasticbagadmin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.OrderListItemBinding
import com.plasticbag.plasticbagadmin.model.OrderDetails

class DeliveredOrderAdapter(): RecyclerView.Adapter<DeliveredOrderAdapter.PendingOrderViewHolder>() {

    private var orderList = ArrayList<OrderDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(orderList: List<OrderDetails>) {
        this.orderList.clear()
        this.orderList.addAll(orderList)
        notifyDataSetChanged()
    }

    class PendingOrderViewHolder(val binding: OrderListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        return PendingOrderViewHolder(OrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {

        holder.binding.title.text = orderList[position].productDetails.title
        holder.binding.quantity.text = orderList[position].productDetails.quantity + " kg"
        holder.binding.bayerName.text = orderList[position].userDetails.name
        holder.binding.bayerPhoneNo.text = orderList[position].userDetails.phoneNo

        holder.binding.addQuantity.visibility = View.GONE
        holder.binding.minusQuantity.visibility = View.GONE
        holder.binding.deleteProduct.visibility = View.GONE
        holder.binding.dispatchProduct.visibility = View.GONE
        holder.binding.editQuantity.visibility = View.GONE

        holder.binding.expandArrow.setOnClickListener {
            if (holder.binding.moreDetailsCl.isVisible) {
                holder.binding.moreDetailsCl.visibility = View.GONE
                holder.binding.expandArrow.setImageResource(R.drawable.ic_down_arrow)
            }else {
                holder.binding.moreDetailsCl.visibility = View.VISIBLE
                holder.binding.expandArrow.setImageResource(R.drawable.ic_up_arrow)
            }
        }

    }


}
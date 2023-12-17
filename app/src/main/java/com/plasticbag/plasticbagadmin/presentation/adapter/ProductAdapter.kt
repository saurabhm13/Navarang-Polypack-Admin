package com.plasticbag.plasticbagadmin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.ProductListItemBinding
import com.plasticbag.plasticbagadmin.model.ProductDetails

class ProductAdapter(
    private val onItemClick: (ProductDetails) -> Unit
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = ArrayList<ProductDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(productList: List<ProductDetails>) {
        this.productList.clear()
        this.productList.addAll(productList)
        notifyDataSetChanged()
    }

    class ProductViewHolder(val binding: ProductListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.binding.productTitle.text = productList[position].title
        holder.binding.productQuantity.text = productList[position].quantity + " kg"

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(productList[position])
        }

        val backgroundColor = if (position % 2 == 0) {
            holder.itemView.context.getColor(R.color.evenBackgroundColor)
        } else {
            holder.itemView.context.getColor(R.color.oddBackgroundColor)
        }
        holder.binding.productCL.setBackgroundColor(backgroundColor)
    }


}
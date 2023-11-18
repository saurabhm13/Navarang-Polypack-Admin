package com.plasticbag.plasticbagadmin.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.plasticbag.plasticbagadmin.databinding.FragmentProductBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.ProductAdapter
import com.plasticbag.plasticbagadmin.presentation.add_edit_product.AddEditProductActivity
import com.plasticbag.plasticbagadmin.util.Constants.Companion.IMAGE
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCT_ID
import com.plasticbag.plasticbagadmin.util.Constants.Companion.QUANTITY
import com.plasticbag.plasticbagadmin.util.Constants.Companion.TITLE

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)

        binding.btnAddProduct.setOnClickListener {
            val intoAddEdit = Intent(activity, AddEditProductActivity::class.java)
            startActivity(intoAddEdit)
        }

        viewModel.getProducts()
        prepareProductRecyclerView()

        return binding.root
    }

    private fun prepareProductRecyclerView() {
        val productAdapter = ProductAdapter(
            onItemClick = {
                val intoAddEdit = Intent(activity, AddEditProductActivity::class.java)
                intoAddEdit.putExtra(IMAGE, it.image)
                intoAddEdit.putExtra(TITLE, it.title)
                intoAddEdit.putExtra(QUANTITY, it.quantity)
                intoAddEdit.putExtra(PRODUCT_ID, it.productId)
                startActivity(intoAddEdit)
            }
        )

        binding.productRv.apply {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }

        viewModel.productLiveData.observe(viewLifecycleOwner) {
            productAdapter.setProductList(it)
        }
    }
}
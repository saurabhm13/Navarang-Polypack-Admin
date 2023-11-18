package com.plasticbag.plasticbagadmin.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.FragmentPendingOrdersBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.PendingOrderAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PendingOrdersFragment : Fragment() {

    private lateinit var binding: FragmentPendingOrdersBinding
    private val viewModel: OrderViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPendingOrdersBinding.inflate(layoutInflater, container, false)

        viewModel.getPendingOrder()
        prepareRecyclerView()


        return binding.root
    }

    private fun prepareRecyclerView() {
        val orderAdapter = PendingOrderAdapter(
            onAddQuantityClick = {product ->
//                viewModel.getProductQuantity(product.productDetails.productId)
//                viewModel.productQuantityLiveData.observe(viewLifecycleOwner) {quantity ->
//                    viewModel.addQuantity(product, quantity)
//                }
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getProductQuantity1(product.productDetails.productId) { q ->
                        viewModel.addQuantity(product, q)
                    }
                }
            },
            onMinusQuantityClick = {product ->
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getProductQuantity1(product.productDetails.productId) { q ->
                        viewModel.minusQuantity(product, q)
                    }
                }
            },
            onDeleteClick = {
                viewModel.deleteProduct(it)
            },
            onDispatchClick = {
                viewModel.addToDispatch(it)
            }
        )

        binding.pendingOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }

        viewModel.pendingOrderLiveData.observe(viewLifecycleOwner) {
            orderAdapter.setProductList(it)
        }
    }

}
package com.plasticbag.plasticbagadmin.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.FragmentDeliveredOrderBinding
import com.plasticbag.plasticbagadmin.databinding.FragmentDispatchOrdersBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.DeliveredOrderAdapter
import com.plasticbag.plasticbagadmin.presentation.adapter.DispatchOrderAdapter

class DeliveredOrderFragment : Fragment() {

    private lateinit var binding: FragmentDeliveredOrderBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveredOrderBinding.inflate(layoutInflater, container, false)

        viewModel.getDeliveredOrder()
        prepareRecyclerView()

        viewModel.errorCallBack = {
            Toast.makeText(activity, "Error: $it", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun prepareRecyclerView() {
        val orderAdapter = DeliveredOrderAdapter()

        binding.deliveredOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }

        viewModel.deliveredOrderLiveData.observe(viewLifecycleOwner) {
            orderAdapter.setProductList(it)
        }
    }

}
package com.plasticbag.plasticbagadmin.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.FragmentDispatchOrdersBinding
import com.plasticbag.plasticbagadmin.databinding.FragmentPendingOrdersBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.DispatchOrderAdapter
import com.plasticbag.plasticbagadmin.presentation.adapter.PendingOrderAdapter

class DispatchOrdersFragment : Fragment() {

    private lateinit var binding: FragmentDispatchOrdersBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDispatchOrdersBinding.inflate(layoutInflater, container, false)

        viewModel.getDispatchOrder()
        prepareRecyclerView()

        return binding.root
    }

    private fun prepareRecyclerView() {
        val orderAdapter = DispatchOrderAdapter(
            onDeliveredClick = {
                viewModel.addToDelivered(it)
            }
        )

        binding.dispatchedOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }

        viewModel.dispatchOrderLiveData.observe(viewLifecycleOwner) {
            orderAdapter.setProductList(it)
        }
    }

}
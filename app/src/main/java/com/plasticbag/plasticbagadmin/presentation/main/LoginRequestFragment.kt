package com.plasticbag.plasticbagadmin.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.FragmentLoginRequestBinding
import com.plasticbag.plasticbagadmin.databinding.LoginReqListItemBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.LoginRequestAdapter

class LoginRequestFragment : Fragment() {

    private lateinit var binding: FragmentLoginRequestBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginRequestBinding.inflate(layoutInflater, container, false)

        viewModel.getLoginRequest()
        prepareRecyclerView()

        return binding.root
    }

    private fun prepareRecyclerView() {
        val loginAdapter = LoginRequestAdapter(
            onApproveClick = {
                viewModel.approveLoginRequest(it)
            },
            onDenyClick = {
                viewModel.denyLoginRequest(it)
            }
        )

        binding.loginReq.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = loginAdapter
        }

        viewModel.loginRequestLiveData.observe(viewLifecycleOwner) {
            loginAdapter.setProductList(it)
        }
    }

}
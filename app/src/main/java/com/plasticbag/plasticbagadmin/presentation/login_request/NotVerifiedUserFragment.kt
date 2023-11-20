package com.plasticbag.plasticbagadmin.presentation.login_request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.databinding.FragmentNotVerifiedBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.NotVerifiedAdapter

class NotVerifiedUserFragment : Fragment() {

    private lateinit var binding: FragmentNotVerifiedBinding
    private val viewModel: LoginRequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotVerifiedBinding.inflate(layoutInflater, container, false)

        viewModel.getNotVerifiedUsers()
        prepareRecyclerView()

        return binding.root
    }

    private fun prepareRecyclerView() {
        val loginAdapter = NotVerifiedAdapter(
            onApproveClick = {
                viewModel.approveLoginRequest(it)
            },
            onDenyClick = {
//                viewModel.denyLoginRequest(it)
            }
        )

        binding.loginReq.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = loginAdapter
        }

        viewModel.notVerifiedUsersLiveData.observe(viewLifecycleOwner) {
            loginAdapter.setProductList(it)
        }
    }

}
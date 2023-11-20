package com.plasticbag.plasticbagadmin.presentation.login_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbagadmin.databinding.FragmentVerifiedUserBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.VerifiedAdapter

class VerifiedUserFragment : Fragment() {

    private lateinit var binding: FragmentVerifiedUserBinding
    private val viewModel: LoginRequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerifiedUserBinding.inflate(layoutInflater, container, false)

        viewModel.getVerifiedUsers()
        prepareRecyclerView()

        return binding.root
    }

    private fun prepareRecyclerView() {
        val loginAdapter = VerifiedAdapter(
            onDenyClick = {
                viewModel.denyLoginRequest(it)
            }
        )

        binding.loginReq.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = loginAdapter
        }

        viewModel.verifiedUsersLiveData.observe(viewLifecycleOwner) {
            loginAdapter.setProductList(it)
        }
    }

}
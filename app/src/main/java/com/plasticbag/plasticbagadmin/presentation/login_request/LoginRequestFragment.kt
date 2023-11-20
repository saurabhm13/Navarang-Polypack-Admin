package com.plasticbag.plasticbagadmin.presentation.login_request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.plasticbag.plasticbagadmin.databinding.FragmentLoginRequestBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.LoginRequestVPAdapter

class LoginRequestFragment : Fragment() {

    private lateinit var binding: FragmentLoginRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginRequestBinding.inflate(layoutInflater, container, false)

        val pagerAdapter = LoginRequestVPAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position){
                0 -> tab.text = "Verified Users"
                1 -> tab.text = "Not Verified Users"
            }
        }.attach()

        return binding.root
    }

}
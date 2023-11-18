package com.plasticbag.plasticbagadmin.presentation.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.plasticbag.plasticbagadmin.R
import com.plasticbag.plasticbagadmin.databinding.FragmentOrderBinding
import com.plasticbag.plasticbagadmin.presentation.adapter.ViewPagerAdapter

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position){
                0 -> tab.text = "Pending"
                1 -> tab.text = "Dispatched"
                2 -> tab.text = "Delivered"
            }
        }.attach()

        return binding.root
    }

}
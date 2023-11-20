package com.plasticbag.plasticbagadmin.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plasticbag.plasticbagadmin.presentation.order.DispatchOrdersFragment
import com.plasticbag.plasticbagadmin.presentation.order.PendingOrdersFragment

class OrderVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingOrdersFragment()
            1 -> DispatchOrdersFragment()
//            2 -> DeliveredOrderFragment()
            else -> PendingOrdersFragment()
        }
    }


}
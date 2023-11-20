package com.plasticbag.plasticbagadmin.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plasticbag.plasticbagadmin.presentation.login_request.NotVerifiedUserFragment
import com.plasticbag.plasticbagadmin.presentation.login_request.VerifiedUserFragment
import com.plasticbag.plasticbagadmin.presentation.order.DeliveredOrderFragment
import com.plasticbag.plasticbagadmin.presentation.order.DispatchOrdersFragment
import com.plasticbag.plasticbagadmin.presentation.order.PendingOrdersFragment

class LoginRequestVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VerifiedUserFragment()
            1 -> NotVerifiedUserFragment()
//            2 -> DeliveredOrderFragment()
            else -> VerifiedUserFragment()
        }
    }


}
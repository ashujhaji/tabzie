package com.pixerapps.tabzee

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FragmentOne()
            1 -> return FragmentTwo()
            2 -> return FragmentThree()
            else -> return FragmentFour()
        }
    }

    override fun getCount(): Int = 4

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "One"
            1 -> return "Two"
            2 -> return "Three"
            else-> return "Four"
        }
    }
}
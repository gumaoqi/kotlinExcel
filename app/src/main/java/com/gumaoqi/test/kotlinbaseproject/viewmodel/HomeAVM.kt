package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.fragment.*

class HomeAVM:ViewModel() {
    lateinit var bottomNavigationFragment: GuBottomNavigationFragment
    lateinit var homeFragment: HomeFragment
    lateinit var actiongFragment: ActionFragment
    lateinit var disCountFragment: DisCountFragment
    lateinit var recommendFragment: RecommendFragment
    lateinit var mineFragment: MineFragment
}
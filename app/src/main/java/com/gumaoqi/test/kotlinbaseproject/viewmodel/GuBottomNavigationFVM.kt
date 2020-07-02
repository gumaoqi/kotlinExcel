package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.base.GuBottomNavigationBean

class GuBottomNavigationFVM:ViewModel() {
    lateinit var guBottomNavigationBeanList: MutableList<GuBottomNavigationBean>
}
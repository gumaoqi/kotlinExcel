package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.MineAdapter

class MineFVM : ViewModel() {
    lateinit var mineAdapter: MineAdapter
}
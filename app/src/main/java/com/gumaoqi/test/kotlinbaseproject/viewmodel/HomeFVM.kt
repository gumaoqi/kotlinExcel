package com.gumaoqi.test.kotlinbaseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.gumaoqi.test.kotlinbaseproject.adapter.ExcelAdapter
import com.gumaoqi.test.kotlinbaseproject.adapter.MineAdapter
import java.io.File

class HomeFVM : ViewModel() {
    lateinit var excelAdapter: ExcelAdapter
    lateinit var fileList: ArrayList<File>
}
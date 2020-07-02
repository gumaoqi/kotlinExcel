package com.gumaoqi.test.kotlinbaseproject.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import java.io.File

class UpdateHeadImgFVM:ViewModel() {
    lateinit var imageUri: Uri
    lateinit var outputImage: File
}
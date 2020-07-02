package com.gumaoqi.test.kotlinbaseproject.service

import com.gumaoqi.test.kotlinbaseproject.entity.DeleteBean
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.HashMap

interface DeleteService {
    @FormUrlEncoded
    @POST("f43174e44ad9f6f2/shopDeleteUser")
    fun delete(@FieldMap paramMap: HashMap<String, String>): Call<DeleteBean>
}
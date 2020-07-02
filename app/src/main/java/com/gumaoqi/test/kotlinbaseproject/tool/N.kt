package com.gumaoqi.test.kotlinbaseproject.tool

import android.os.Handler
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.entity.*
import com.gumaoqi.test.kotlinbaseproject.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object N {
    private const val TAG = "网络请求"

    /**
     * 用retrofit进行添加数据
     * 增
     */
    fun addByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopAddUser"
        Re.addSign(paramMap, GuApplication.context)
        val addService = Re.getRetrofit()
                .create(AddService::class.java)
        val call = addService.add(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<AddBean> {
            override fun onResponse(call: Call<AddBean>, response: Response<AddBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val getBean = response.body()
                L.i(TAG, "${action}接口返回:$getBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = getBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<AddBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用retrofit进行删除数据
     * 删
     */
    fun deleteByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopDeleteUser"
        Re.addSign(paramMap, GuApplication.context)
        val deleteService = Re.getRetrofit()
                .create(DeleteService::class.java)
        val call = deleteService.delete(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<DeleteBean> {
            override fun onResponse(call: Call<DeleteBean>, response: Response<DeleteBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val deleteBean = response.body()
                L.i(TAG, "${action}接口返回:$deleteBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = deleteBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<DeleteBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用retrofit修改数据
     * 改
     */
    fun updateByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopUpdateUser"
        Re.addSign(paramMap, GuApplication.context)
        val updateService = Re.getRetrofit()
                .create(UpdateService::class.java)
        val call = updateService.update(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<UpdateBean> {
            override fun onResponse(call: Call<UpdateBean>, response: Response<UpdateBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val updateBean = response.body()
                L.i(TAG, "${action}接口返回:$updateBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = updateBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<UpdateBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用retrofit取获取数据
     * 查
     */
    fun getByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopGetUser"
        Re.addSign(paramMap, GuApplication.context)
        val loginService = Re.getRetrofit()
                .create(LoginService::class.java)
        val call = loginService.login(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<GetBean> {
            override fun onResponse(call: Call<GetBean>, response: Response<GetBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val getBean = response.body()
                L.i(TAG, "${action}接口返回:$getBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = getBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<GetBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用Retrofit去获取验证码
     */
    fun getSmsByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopSendSms"
        Re.addSign(paramMap, GuApplication.context)
        val getSmsService = Re.getRetrofit()
                .create(GetSmsService::class.java)
        val call = getSmsService.getSms(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<SmsBean> {
            override fun onResponse(call: Call<SmsBean>, response: Response<SmsBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val smsBean = response.body()
                L.i(TAG, "${action}接口返回:$smsBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = smsBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<SmsBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用Retrofit去验证验证码
     */
    fun sureSmsByRetrofit(paramMap: HashMap<String, String>, handler: Handler, action: String, arg1: Int) {
        Re.serviceName = "f43174e44ad9f6f2/shopSureSms"
        Re.addSign(paramMap, GuApplication.context)
        val sureSmsService = Re.getRetrofit()
                .create(SureSmsService::class.java)
        val call = sureSmsService.sureSms(paramMap)
        L.i(TAG, action)
        call.enqueue(object : Callback<SureSmsBean> {
            override fun onResponse(call: Call<SureSmsBean>, response: Response<SureSmsBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "${action}接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val smsBean = response.body()
                L.i(TAG, "${action}接口返回:$smsBean")
                val message = handler.obtainMessage()
                message.arg1 = arg1
                message.obj = smsBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<SureSmsBean>, t: Throwable) {
                L.i(TAG, "${action}接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }


    /**
     * 用retrofit找回密码
     * 很特殊的一个接口，需要根据手机号找到获取其objectId，然后再进行重置密码
     */
    fun findPasswordByRetrofit(paramMap: HashMap<String, String>, handler: Handler) {
        Re.serviceName = "f43174e44ad9f6f2/shopUpdatePassword"
        Re.addSign(paramMap, GuApplication.context)
        val updateService = Re.getRetrofit()
                .create(FindPasswordService::class.java)
        val call = updateService.update(paramMap)
        L.i(TAG, "找回密码")
        call.enqueue(object : Callback<UpdateBean> {
            override fun onResponse(call: Call<UpdateBean>, response: Response<UpdateBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "找回密码接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val updateBean = response.body()
                L.i(TAG, "找回密码接口返回:$updateBean")
                val message = handler.obtainMessage()
                message.arg1 = HandlerArg.FIND_PASSWORD_BACK
                message.obj = updateBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<UpdateBean>, t: Throwable) {
                L.i(TAG, "找回密码接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }

    /**
     * 用retrofit找回密码
     * 很特殊的一个接口，需要根据手机号和输入的旧密码找到获取其objectId，然后再进行修改密码
     */
    fun updatePasswordByRetrofit(paramMap: HashMap<String, String>, handler: Handler) {
        Re.serviceName = "f43174e44ad9f6f2/shopUpdatePasswordTwo"
        Re.addSign(paramMap, GuApplication.context)
        val updateService = Re.getRetrofit()
                .create(UpdatePasswordService::class.java)
        val call = updateService.update(paramMap)
        L.i(TAG, "修改密码")
        call.enqueue(object : Callback<UpdateBean> {
            override fun onResponse(call: Call<UpdateBean>, response: Response<UpdateBean>?) {
                if (response?.body() == null) {
                    L.i(TAG, "修改密码接口返回为空")
                    T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
                    return
                }
                val updateBean = response.body()
                L.i(TAG, "修改密码接口返回:$updateBean")
                val message = handler.obtainMessage()
                message.arg1 = HandlerArg.UPDATE_PASSWORD_BACK
                message.obj = updateBean
                handler.sendMessageDelayed(message, 100)
            }

            override fun onFailure(call: Call<UpdateBean>, t: Throwable) {
                L.i(TAG, "修改密码接口连接超时,${t.message}")
                T.s(GuApplication.context.getString(R.string.gu_net_error_or_server_busy))
            }
        })
    }
}
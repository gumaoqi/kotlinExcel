package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.SmsBean
import com.gumaoqi.test.kotlinbaseproject.entity.SureSmsBean
import com.gumaoqi.test.kotlinbaseproject.entity.UpdateBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.service.FindPasswordService
import com.gumaoqi.test.kotlinbaseproject.service.GetSmsService
import com.gumaoqi.test.kotlinbaseproject.service.LoginService
import com.gumaoqi.test.kotlinbaseproject.service.SureSmsService
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.FindPasswordFVM
import kotlinx.android.synthetic.main.fragment_find_password.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: FindPasswordFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(FindPasswordFVM::class.java)
        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (fragment_find_password_sms_bt == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                HandlerArg.SUCCESS -> {
                }
                HandlerArg.FIND_PASSWORD_IF_REGISTER -> {//判断是否已经注册了
                    val getBean = msg.obj as GetBean
                    if (getBean.results == null || getBean.results.isEmpty()) {//暂未注册
                        "该手机号暂未注册，请先进行注册".show()
                    } else {//已经注册了，去获取验证码
                        addParamGetSms(vm.phone)
                    }
                }
                HandlerArg.GET_FIND_PASSWORD_SMS_BACK -> {
                    val smsBean = msg.obj as SmsBean
                    if (smsBean.smsId == null || smsBean.smsId == 0) {
                        "获取验证码过于频繁，请稍后再试".show()
                    } else {//获取验证码成功，开启计时器
                        "验证码获取成功，请查看短信".show()
                        vm.timer = 120
                        val message = gHandler.obtainMessage()
                        message.arg1 = HandlerArg.FIND_PASSWORD_TIMER
                        gHandler.sendMessageDelayed(message, 1000)
                    }
                }
                HandlerArg.FIND_PASSWORD_TIMER -> {//开启计时器
                    vm.timer--
                    if (vm.timer > 0) {
                        fragment_find_password_sms_bt?.text = "" + vm.timer + "s"
                        val message = gHandler.obtainMessage()
                        message.arg1 = HandlerArg.FIND_PASSWORD_TIMER
                        gHandler.sendMessageDelayed(message, 1000)
                    } else {
                        fragment_find_password_sms_bt?.setText(R.string.gu_get_sms)
                    }
                }
                HandlerArg.SURE_FIND_PASSWORD_SMS_BACK -> {
                    val sureSmsBean = msg.obj as SureSmsBean
                    if (sureSmsBean.msg == null) {//验证码填写错误
                       "您填写的验证码有误".show()
                    } else {//验证码填写正确
                        addParamFindPassword()
                    }
                }
                HandlerArg.FIND_PASSWORD_BACK -> {
                    val updateBean = msg.obj as UpdateBean
                    if (updateBean.updatedAt == null) {
                        "找回密码失败，请重试".show()
                        return@Callback false
                    }
                    "找回密码成功，请登录".show()
                    setMessageToActivity(HandlerArg.CHANGE_LOGIN_FRAGMENT, 0)
                }
            }
            false
        })
        vm.timer = 0
        if (I.isShowLog) {
            fragment_find_password_phone_et.setText("18380129598")
            fragment_find_password_password_et.setText("123456")
            fragment_find_password_password_again_et.setText("123456")
        }
    }

    override fun setView() {
        super.setView()
        fragment_find_password_sms_bt.setOnClickListener {
            if (vm.timer > 0) {
                "120秒内只能够获取一次验证码".show()
                return@setOnClickListener
            }
            checkPhoneInput()
        }
        fragment_find_password_bt.setOnClickListener { checkPhoneSmsPasswordInput() }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }


    /**
     * 检测手机号的输入
     */
    private fun checkPhoneInput() {
        vm.phone = fragment_find_password_phone_et.text.toString()
        if (vm.phone.length != 11 || (!vm.phone.startsWith("1"))) {
            "请输入11位有效手机号".show()
            return
        }
        addParamIfRegister(vm.phone)
    }

    /**
     * 检测手机号验证码和密码的输入
     */
    private fun checkPhoneSmsPasswordInput() {
        vm.phone = fragment_find_password_phone_et.text.toString()
        vm.sms = fragment_find_password_sms_et.text.toString()
        vm.password = fragment_find_password_password_et.text.toString()
        vm.passwordAgain = fragment_find_password_password_again_et.text.toString()
        if (vm.phone.length != 11 || (!vm.phone.startsWith("1"))) {
            "请输入11位有效手机号".show()
            return
        }
        if (vm.sms.length != 6) {
            "请输入6位有效手机验证码".show()
            return
        }
        if (vm.password.length < 6 || vm.passwordAgain.length < 6) {
            "请输入6-18位有效密码".show()
            return
        }
        if (vm.password != (vm.passwordAgain)) {
           "两次输入的密码不一致".show()
            return
        }
        addParamSureSms(vm.phone, vm.sms)
    }

    /**
     * 添加参数去判断是否已经注册
     */
    private fun addParamIfRegister(phone: String) {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = phone
        paramMap["tablename"] = "shop_user"
        N.getByRetrofit(paramMap, gHandler, "找回密码时判断是否已经注册", HandlerArg.FIND_PASSWORD_IF_REGISTER)
    }

    /**
     * 添加参数去获取验证码
     */
    private fun addParamGetSms(phone: String) {
        val paramMap = HashMap<String, String>()
        paramMap["phone"] = phone
        N.getSmsByRetrofit(paramMap, gHandler, "找回密码时获取注册验证码", HandlerArg.GET_FIND_PASSWORD_SMS_BACK)
    }


    /**
     * 添加参数去验证验证码
     */
    private fun addParamSureSms(phone: String, sms: String) {
        val paramMap = HashMap<String, String>()
        paramMap["phone"] = phone
        paramMap["sms"] = sms
        N.sureSmsByRetrofit(paramMap, gHandler, "找回密码时验证注册验证码", HandlerArg.SURE_FIND_PASSWORD_SMS_BACK)
    }

    /**
     * 添加参数找回密码
     */
    private fun addParamFindPassword() {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = vm.phone
        paramMap["c2"] = vm.password
        paramMap["tablename"] = "shop_user"
        N.findPasswordByRetrofit(paramMap, gHandler)
    }
}


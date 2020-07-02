package com.gumaoqi.test.kotlinbaseproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_LOGIN_FRAGMENT
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GET_REGISTER_SMS_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.IF_REGISTER
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.REGISTER_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SURE_REGISTER_SMS_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.REGISTER_TIMER
import com.gumaoqi.test.kotlinbaseproject.entity.AddBean
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.entity.SmsBean
import com.gumaoqi.test.kotlinbaseproject.entity.SureSmsBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.service.AddService
import com.gumaoqi.test.kotlinbaseproject.service.GetSmsService
import com.gumaoqi.test.kotlinbaseproject.service.LoginService
import com.gumaoqi.test.kotlinbaseproject.service.SureSmsService
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.RegisterFVM
import kotlinx.android.synthetic.main.fragment_find_password.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: RegisterFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(RegisterFVM::class.java)
        intData()
        setView()
    }

    @SuppressLint("SetTextI18n")
    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (activity == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                IF_REGISTER -> {//判断是否已经注册了
                    val getBean = msg.obj as GetBean
                    if (getBean.results == null || getBean.results.isEmpty()) {//暂未注册去获取验证码
                        addParamGetSms(vm.phone)
                    } else {//已经注册了，提示用户登录或者重置密码
                        "该手机号已经注册，请登录或找回密码".show()
                    }
                }
                GET_REGISTER_SMS_BACK -> {
                    val smsBean = msg.obj as SmsBean
                    if (smsBean.smsId == null) {
                        "获取验证码过于频繁，请稍后再试".show()
                    } else {//获取验证码成功，开启计时器
                        "验证码获取成功，请查看短信".show()
                        vm.timer = 120
                        val message = gHandler.obtainMessage()
                        message.arg1 = REGISTER_TIMER
                        gHandler.sendMessageDelayed(message, 1000)
                    }
                }

                REGISTER_TIMER -> {//开启计时器
                    vm.timer--
                    if (vm.timer > 0) {
                        fragment_register_sms_bt?.text = "" + vm.timer + "s"
                        val message = gHandler.obtainMessage()
                        message.arg1 = REGISTER_TIMER
                        gHandler.sendMessageDelayed(message, 1000)
                    } else {
                        fragment_register_sms_bt?.setText(R.string.gu_get_sms)
                    }
                }
                SURE_REGISTER_SMS_BACK -> {
                    val sureSmsBean = msg.obj as SureSmsBean
                    if (sureSmsBean.msg == null) {//验证码填写错误
                        "您填写的验证码有误".show()
                    } else {//验证码填写正确
                        addParamRegister()
                    }
                }
                REGISTER_BACK -> {
                    val addBean = msg.obj as AddBean
                    if (addBean.createdAt == null) {
                        "注册失败，请重试".show()
                        return@Callback false
                    }
                    "注册成功，请登录".show()
                    setMessageToActivity(CHANGE_LOGIN_FRAGMENT, 0)
                }
            }
            false
        })
        vm.timer = 0
        if (I.isShowLog) {
            fragment_register_phone_et.setText("18380129598")
            fragment_register_password_et.setText("123456")
            fragment_register_password_again_et.setText("123456")
        }
    }

    override fun setView() {
        super.setView()
        fragment_register_sms_bt.setOnClickListener {
            if (vm.timer > 0) {
                "120秒内只能够获取一次验证码".show()
                return@setOnClickListener
            }
            checkPhoneInput()
        }
        fragment_register_register_bt.setOnClickListener { checkPhoneSmsPasswordInput() }
    }

    /**
     * 检测手机号的输入
     */
    private fun checkPhoneInput() {
        vm.phone = fragment_register_phone_et.text.toString()
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
        vm.phone = fragment_register_phone_et.text.toString()
        vm.sms = fragment_register_sms_et.text.toString()
        vm.password = fragment_register_password_et.text.toString()
        vm.passwordAgain = fragment_register_password_again_et.text.toString()
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
        N.getByRetrofit(paramMap, gHandler, "注册时判断是否已经注册", IF_REGISTER)
    }

    /**
     * 添加参数去获取验证码
     */
    private fun addParamGetSms(phone: String) {
        val paramMap = HashMap<String, String>()
        paramMap["phone"] = phone
        N.getSmsByRetrofit(paramMap, gHandler, "注册时获取注册验证码", GET_REGISTER_SMS_BACK)
    }


    /**
     * 添加参数去验证验证码
     */
    private fun addParamSureSms(phone: String, sms: String) {
        val paramMap = HashMap<String, String>()
        paramMap["phone"] = phone
        paramMap["sms"] = sms
        N.sureSmsByRetrofit(paramMap, gHandler, "注册时验证注册验证码", SURE_REGISTER_SMS_BACK)
    }


    /**
     * 添加参数进行注册
     */
    private fun addParamRegister() {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = vm.phone
        paramMap["c2"] = vm.password
        paramMap["tablename"] = "shop_user"
        N.addByRetrofit(paramMap, gHandler, "进行注册", REGISTER_BACK)
    }
}
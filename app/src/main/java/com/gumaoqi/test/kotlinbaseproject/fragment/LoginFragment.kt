package com.gumaoqi.test.kotlinbaseproject.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.HomeActivity
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_FIND_PASSWORD_FRAGMENT
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_REGISTER_FRAGMENT
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.LOGIN_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.entity.GetBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.service.LoginService
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.HomeFVM
import com.gumaoqi.test.kotlinbaseproject.viewmodel.LoadingFVM
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : BaseFragment() {

    private lateinit var gHandler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (fragment_login_login_bt == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                LOGIN_BACK -> {
                    val getBean = msg.obj as GetBean
                    if (getBean.results == null || getBean.results.isEmpty()) {
                        "登录失败，账号或密码错误".show()
                    } else {
                        val result = getBean.results[0]
                        "登录成功".show()
                        S.setString("login_time", "" + System.currentTimeMillis())
                        S.setString("c1", "" + result.c1)
                        S.setString("c2", "" + result.c2)
                        S.setString("c3", "" + result.c3)
                        S.setString("c4", "" + result.c4)
                        S.setString("c5", "" + result.c5)
                        S.setString("c6", "" + result.c6)
                        S.setString("c7", "" + result.c7)
                        S.setString("c8", "" + result.c8)
                        S.setString("c9", "" + result.c9)
                        S.setString("c10", "" + result.c10)
                        S.setString("object_id", "" + result.objectId)
                        S.setString("create_time", "" + result.createdAt)
                        val intent = Intent(GuApplication.context, HomeActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
            false
        })
        if (I.isShowLog) {
            fragment_login_phone_et.setText("18380129598")
            fragment_login_password_et.setText("123456")
        }
    }

    override fun setView() {
        super.setView()
        fragment_login_login_bt.setOnClickListener {
            //检测输入的账号密码是否合法
            checkInput()
        }
        fragment_login_find_password_tv.setOnClickListener {
            //切换到找回密码界面
            setMessageToActivity(CHANGE_FIND_PASSWORD_FRAGMENT, 0)
        }
        fragment_login_register_bt.setOnClickListener {
            //切换到注册页面
            setMessageToActivity(CHANGE_REGISTER_FRAGMENT, 0)
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * 检查输入内容的合法性
     */
    private fun checkInput() {
        val phone = fragment_login_phone_et.text.toString()
        val password = fragment_login_password_et.text.toString()
        if (phone.length != 11) {
            "请输入11位手机号".show()
            return
        }
        if (password.length < 6) {
            "请输入6-18位密码".show()
            return
        }
        addParamLogin(phone, password)
    }

    /**
     * 添加参数去登录
     */
    private fun addParamLogin(phone: String, password: String) {
        val paramMap = HashMap<String, String>()
        paramMap["c1"] = phone
        paramMap["c2"] = password
        paramMap["tablename"] = "shop_user"
        //用retrofit去网络请求，验证账号密码
        N.getByRetrofit(paramMap, gHandler, "登录", LOGIN_BACK)
    }
}
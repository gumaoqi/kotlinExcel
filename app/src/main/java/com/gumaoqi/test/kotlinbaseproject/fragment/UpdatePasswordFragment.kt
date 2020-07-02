package com.gumaoqi.test.kotlinbaseproject.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gumaoqi.test.kotlinbaseproject.LoginActivity
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.R.id.fragment_update_password_bt
import com.gumaoqi.test.kotlinbaseproject.base.ActivityCollector
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.UPDATE_PASSWORD_BACK
import com.gumaoqi.test.kotlinbaseproject.entity.UpdateBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.service.UpdatePasswordService
import com.gumaoqi.test.kotlinbaseproject.tool.*
import kotlinx.android.synthetic.main.fragment_update_head_img.*
import kotlinx.android.synthetic.main.fragment_update_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordFragment : BaseFragment() {

    private lateinit var gHandler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (activity == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                UPDATE_PASSWORD_BACK -> {
                    val updateBean = msg.obj as UpdateBean
                    if (updateBean.updatedAt == null) {
                        "原始密码错误，请重试".show()
                        return@Callback false
                    }
                    "修改密码成功".show()
                    S.clearSharedPreferences()
                    ActivityCollector.finishAll()
                    startActivity(Intent(GuApplication.context, LoginActivity::class.java))
                }
            }
            false
        })
    }

    override fun setView() {
        super.setView()
        fragment_update_password_bt.setOnClickListener { checkInput() }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun checkInput() {
        val oldPassword = fragment_update_password_old_et.text.toString()
        val newPassword = fragment_update_password_new_et.text.toString()
        val againPassword = fragment_update_password_again_et.text.toString()
        if (oldPassword.length < 6 || newPassword.length < 6 || againPassword.length < 6) {
            "请输入6-18位密码".show()
            return
        }
        if (newPassword != againPassword) {
            "两次输入的新密码不一致".show()
            return
        }
        addParamFindPassword(oldPassword, newPassword)
    }

    /**
     * 添加参数修改密码
     */
    private fun addParamFindPassword(oldPassword: String, newPassword: String) {
        val paramMap = HashMap<String, String>()
        paramMap["oldpassword"] = oldPassword
        paramMap["c2"] = newPassword
        paramMap["c1"] = S.getString("c1")
        N.updatePasswordByRetrofit(paramMap, gHandler)
    }
}
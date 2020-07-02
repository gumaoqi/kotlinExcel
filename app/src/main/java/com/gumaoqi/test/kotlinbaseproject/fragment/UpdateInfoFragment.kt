package com.gumaoqi.test.kotlinbaseproject.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.UPDATE_INFO
import com.gumaoqi.test.kotlinbaseproject.entity.UpdateBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.service.UpdateService
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.UpdateInfoFVM
import kotlinx.android.synthetic.main.fragment_update_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateInfoFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: UpdateInfoFVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(UpdateInfoFVM::class.java)
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
                UPDATE_INFO -> {
                    val updateBean = msg.obj as UpdateBean
                    if (updateBean.updatedAt == null) {
                        "更新个人资料失败，请重试".show()
                        return@Callback false
                    }
                    "更新个人资料成功".show()
                    L.i(TAG, "更新个人资料成功$vm.successKey=$vm.successValue")
                    S.setString(vm.successKey, vm.successValue)
                }
            }
            false
        })
    }

    override fun setView() {
        super.setView()
        fragment_update_info_c3_et.setText(S.getString("c3"))
        fragment_update_info_c4_et.setText(S.getString("c4"))
        fragment_update_info_c5_et.setText(S.getString("c5"))
        fragment_update_info_c6_et.setText(S.getString("c6"))
        fragment_update_info_c7_et.setText(S.getString("c7"))
        fragment_update_info_c8_et.setText(S.getString("c8"))
        fragment_update_info_c9_et.setText(S.getString("c9"))
        fragment_update_info_c10_et.setText(S.getString("c10"))
        fragment_update_info_c3_bt.setOnClickListener {
            val value = fragment_update_info_c3_et.text.toString()
            checkInput("c3", value)
        }
        fragment_update_info_c4_bt.setOnClickListener {
            val value = fragment_update_info_c4_et.text.toString()
            checkInput("c4", value)
        }
        fragment_update_info_c5_bt.setOnClickListener {
            val value = fragment_update_info_c5_et.text.toString()
            checkInput("c5", value)
        }
        fragment_update_info_c6_bt.setOnClickListener {
            val value = fragment_update_info_c6_et.text.toString()
            checkInput("c6", value)
        }
        fragment_update_info_c7_bt.setOnClickListener {
            val value = fragment_update_info_c7_et.text.toString()
            checkInput("c7", value)
        }
        fragment_update_info_c8_bt.setOnClickListener {
            val value = fragment_update_info_c8_et.text.toString()
            checkInput("c8", value)
        }
        fragment_update_info_c9_bt.setOnClickListener {
            val value = fragment_update_info_c9_et.text.toString()
            checkInput("c9", value)
        }
        fragment_update_info_c10_bt.setOnClickListener {
            val value = fragment_update_info_c10_et.text.toString()
            checkInput("c10", value)
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun checkInput(key: String, value: String) {
        if (value.isEmpty()) {
            "输入的内容不能为空".show()
            return
        }
        addParamUpdateInfo(key, value)
    }

    /**
     * 添加参数修改个人信息
     */
    private fun addParamUpdateInfo(key: String, value: String) {
        val paramMap = HashMap<String, String>()
        paramMap[key] = value
        paramMap["objectid"] = S.getString("object_id")
        paramMap["tablename"] = "shop_user"
        for (key in paramMap.keys) {
            if (key.startsWith("c")) {
                vm.successKey = key
                vm.successValue = paramMap[key]!!
            }
        }
        N.updateByRetrofit(paramMap, gHandler, "修改个人信息", UPDATE_INFO)
    }
}
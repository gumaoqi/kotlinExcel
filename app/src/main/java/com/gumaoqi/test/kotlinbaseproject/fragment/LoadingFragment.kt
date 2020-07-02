package com.gumaoqi.test.kotlinbaseproject.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.HomeActivity
import com.gumaoqi.test.kotlinbaseproject.LoginActivity
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.LOADING_TIMER
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.tool.S
import com.gumaoqi.test.kotlinbaseproject.viewmodel.LoadingFVM
import kotlinx.android.synthetic.main.fragment_loading.*

class LoadingFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: LoadingFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(LoadingFVM::class.java)
        intData()
        setView()
    }

    override fun intData() {
        super.intData()
        gHandler = Handler(Handler.Callback { msg ->
            if (fragment_loading_tv == null) {//已经与activity解绑了
                return@Callback false
            }
            when (msg.arg1) {
                SUCCESS -> {
                }
                LOADING_TIMER -> {
                    fragment_loading_tv.text = "" + vm.timer
                    vm.timer--
                    if (vm.timer < 0) {
                        if (S.ifNeedLogin()) {//当前没有登录或登录已经过期
                            val intent = Intent(GuApplication.context, HomeActivity::class.java)
                            startActivity(intent)

                        } else {//当前已经登录
                            val intent = Intent(GuApplication.context, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        activity?.finish()
                        return@Callback false
                    }
                    val message = gHandler.obtainMessage()
                    message.arg1 = LOADING_TIMER
                    gHandler.sendMessageDelayed(message, 1000)
                }
            }
            false
        })
        val message = gHandler.obtainMessage()
        message.arg1 = LOADING_TIMER
        gHandler.sendMessageDelayed(message, 1000)
    }

    override fun setView() {
        super.setView()
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

}
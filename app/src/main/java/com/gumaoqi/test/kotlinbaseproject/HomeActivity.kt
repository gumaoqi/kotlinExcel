package com.gumaoqi.test.kotlinbaseproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.provider.Settings
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.base.BaseActivity
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_FIVE
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_FOUR
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_OME
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_THREE
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.CHANGE_TWO
import com.gumaoqi.test.kotlinbaseproject.fragment.GuBottomNavigationFragment
import com.gumaoqi.test.kotlinbaseproject.fragment.*
import com.gumaoqi.test.kotlinbaseproject.tool.L
import com.gumaoqi.test.kotlinbaseproject.viewmodel.HomeAVM
import kotlinx.android.synthetic.main.title.*
import androidx.annotation.RequiresPermission
import android.os.Build
import java.io.IOException


class HomeActivity : BaseActivity() {

    private lateinit var gHandler: Handler
    private lateinit var vm: HomeAVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        iniData()
        setLayout()
    }

    override fun onResume() {
        super.onResume()
        setView()
    }

    private fun iniData() {
        vm = ViewModelProviders.of(this).get(HomeAVM::class.java)
        gHandler = Handler(Handler.Callback { msg ->
            if (this == null) {//activity已经被销毁了
                return@Callback false
            }
            when (msg.arg1) {
                CHANGE_OME -> {
                    L.i(TAG, "页面一")
                    title_back.text = ""
                    title_title.text = "主页"
                    changeFragment(R.id.activity_home_content_fl, vm.homeFragment)
                }
                CHANGE_TWO -> {
                    title_back.text = ""
                    title_title.text = "活动"
                    changeFragment(R.id.activity_home_content_fl, vm.actiongFragment)
                }
                CHANGE_THREE -> {
                    title_back.text = ""
                    title_title.text = "优惠"
                    changeFragment(R.id.activity_home_content_fl, vm.disCountFragment)
                }
                CHANGE_FOUR -> {
                    title_back.text = ""
                    title_title.text = "推荐"
                    changeFragment(R.id.activity_home_content_fl, vm.recommendFragment)
                }
                CHANGE_FIVE -> {
                    title_back.text = ""
                    title_title.text = "我的"
                    changeFragment(R.id.activity_home_content_fl, vm.mineFragment)

                }
            }
            false
        })
        doubleClickBack = true
        vm.bottomNavigationFragment = GuBottomNavigationFragment()
        vm.homeFragment = HomeFragment()
        vm.actiongFragment = ActionFragment()
        vm.disCountFragment = DisCountFragment()
        vm.recommendFragment = RecommendFragment()
        vm.mineFragment = MineFragment()
    }

    private fun setLayout() {
        vm.bottomNavigationFragment.activityHandler = gHandler
        changeFragment(R.id.activity_home_bottom_navigation_fl, vm.bottomNavigationFragment)
//        val message = gHandler.obtainMessage()
//        message.arg1 = CHANGE_OME
//        gHandler.sendMessageDelayed(message, 100)
    }


    private fun setView() {
        if (IsAirModeOn(this)) {
            L.i(TAG, "处于飞行模式")
        } else {
            L.i(TAG, "不处于飞行模式")
        }
    }

    fun IsAirModeOn(context: Context): Boolean {
        return Settings.System.getInt(context.contentResolver,
                Settings.System.AIRPLANE_MODE_ON, 0) == 1
    }


}
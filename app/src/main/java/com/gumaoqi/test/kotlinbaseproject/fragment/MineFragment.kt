package com.gumaoqi.test.kotlinbaseproject.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.gumaoqi.test.kotlinbaseproject.LoginActivity
import com.gumaoqi.test.kotlinbaseproject.MineActivity
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.MineAdapter
import com.gumaoqi.test.kotlinbaseproject.base.ActivityCollector
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.MINE
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.entity.MineBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.S
import com.gumaoqi.test.kotlinbaseproject.tool.T
import com.gumaoqi.test.kotlinbaseproject.viewmodel.MineFVM
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: MineFVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(MineFVM::class.java)
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
                MINE -> {
                    val str = msg.obj as String
                    when (str) {
                        "我的信息" -> {
                            startActivity(Intent(GuApplication.context, MineActivity::class.java).putExtra("fragment", "1"))
                        }
                        "修改头像" -> {
                            startActivity(Intent(GuApplication.context, MineActivity::class.java).putExtra("fragment", "3"))
                        }
                        "修改密码" -> {
                            startActivity(Intent(GuApplication.context, MineActivity::class.java).putExtra("fragment", "2"))
                        }
                        "版本更新" -> {
                            "版本更新".show()
                        }
                        "关于我们" -> {
                            "关于我们".show()
                        }
                        "其他" -> {
                            "其他".show()
                        }
                        "退出登录" -> {
                            "退出登录成功，请重新登录".show()
                            S.clearSharedPreferences()
                            ActivityCollector.finishAll()
                            startActivity(Intent(GuApplication.context, LoginActivity::class.java))
                        }
                    }
                }
            }
            false
        })
        val list = listOf(
                MineBean("我的信息", R.mipmap.ic_launcher, 0, ""),
                MineBean("修改头像", R.mipmap.ic_launcher, 0, ""),
                MineBean("修改密码", R.mipmap.ic_launcher, 0, ""),
                MineBean("版本更新", R.mipmap.ic_launcher, 0, ""),
                MineBean("关于我们", R.mipmap.ic_launcher, 0, ""),
                MineBean("其他", R.mipmap.ic_launcher, 0, "")
        )
        vm.mineAdapter = MineAdapter()
        vm.mineAdapter.init(gHandler)
        vm.mineAdapter.setInfo("退出登录")
        fragment_mine_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_mine_rv.adapter = vm.mineAdapter
        vm.mineAdapter.setList(list)
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
package com.gumaoqi.test.kotlinbaseproject.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.adapter.ExcelAdapter
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.EXECUTE_SCRIPT
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GET_DOCX_BACK
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.SUCCESS
import com.gumaoqi.test.kotlinbaseproject.tool.I.Companion.MAX_FILE_SIZE
import com.gumaoqi.test.kotlinbaseproject.tool.L
import com.gumaoqi.test.kotlinbaseproject.viewmodel.HomeFVM
import com.gumaoqi.test.kotlinbaseproject.viewmodel.LoadingFVM
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.gumaoqi.test.kotlinbaseproject.entity.UserBean
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableWorkbook
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


class HomeFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: HomeFVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(HomeFVM::class.java)
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
                GET_DOCX_BACK -> {
                    vm.fileList.add(msg.obj as File)
                    vm.excelAdapter.setList(vm.fileList)
                }
                EXECUTE_SCRIPT -> {
                    val file = msg.obj as File
                    L.i(TAG, "文件${file.absolutePath}执行脚本")
//                    addUser(UserBean("gu", "male", 28), file)
                    queryUser(file)
                }
            }
            false
        })
        val file = File(Environment.getExternalStorageDirectory().path + "/aaaExcelTest")//创建文件夹
        if (!file.exists()) {
            file.mkdir()
        }
        vm.excelAdapter = ExcelAdapter()
        vm.excelAdapter.adapterInfo = ""
        vm.excelAdapter.gHandler = gHandler
        vm.fileList = ArrayList<File>()
        find(file.path, "xls")
    }

    override fun setView() {
        super.setView()
        fragment_home_srl.setOnRefreshListener {
            intData()
            setView()
            fragment_home_srl.isRefreshing = false
        }
        fragment_home_rv.layoutManager = LinearLayoutManager(GuApplication.context)
        fragment_home_rv.adapter = vm.excelAdapter

    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun find(path: String, reg: String) {
        val file = File(path)
        val arr = file.listFiles()
        for (i in arr!!.indices) {
            //判断是否是文件夹，如果是的话，再调用一下find方法
            if (arr[i].isDirectory) {
                find(arr[i].absolutePath, reg)
                continue
            }
            //根据正则表达式，寻找匹配的文件
            if (arr[i].absolutePath.endsWith(reg)) {
                val myFile = File(arr[i].absolutePath)
                L.i(TAG, arr[i].absolutePath + "文件大小：" + myFile.length())
                if (myFile.length() < MAX_FILE_SIZE) {//只显示30.0M的文件
                    val message = gHandler.obtainMessage()
                    message.arg1 = GET_DOCX_BACK
                    message.obj = myFile
                    gHandler.sendMessageDelayed(message, 100)
                }
            }
        }
    }

    private fun addUser(user: UserBean, file: File) {
        var originWwb: Workbook? = null
        var newWwb: WritableWorkbook? = null
        try {
            L.i(TAG, "插入数据")
            //插入数据需要拿到原来的表格originWwb，然后通过其创建一个新的表格newWwb，在newWwb上完成插入操作
            originWwb = Workbook.getWorkbook(file)
            newWwb = Workbook.createWorkbook(file, originWwb!!)
            //获取指定索引的表格
            val ws = newWwb!!.getSheet(0)
            // 获取该表格现有的行数，将数据插入到底部
            val row = ws.rows
            val lab1 = Label(0, row, user.name)//参数分别代表：列数，行数，插入的内容
            val lab2 = Label(1, row, user.sex)
            val lab3 = Label(2, row, user.age.toString())
            ws.addCell(lab1)
            ws.addCell(lab2)
            ws.addCell(lab3)
            // 从内存中的数据写入到sd卡excel文件中。
            newWwb.write()
        } catch (e: Exception) {
            e.printStackTrace()
            L.i(TAG, "插入数据出现异常：$e")
        } finally {//释放资源
            originWwb?.close()
            if (newWwb != null) {
                try {
                    newWwb.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    /**
     * 获取excel文件中的数据
     */
    private fun queryUser(file: File) {
        var inputStream: InputStream? = null
        var workbook: Workbook? = null
        try {
            //获取到excel对象
            inputStream = FileInputStream(file.path)//获取流
            workbook = Workbook.getWorkbook(inputStream)
            //获取到excel对象的第一个sheet
            val sheet = workbook!!.getSheet(0)
            for (i in 1..60) {//遍历每行
                if (i == 1 || i == 60) {//查看行数是否全部包括
                    L.i(TAG, sheet.getCell(2, i).contents)
                }
            }
            for (i in 1..60) {//遍历每行,规则1、总报考科数=无需阅卷数+纸考阅卷数+总机考阅卷数
                val result = (sheet.getCell(2, i).contents.toInt()
                        - sheet.getCell(3, i).contents.toInt()
                        - sheet.getCell(11, i).contents.toInt()
                        - sheet.getCell(12, i).contents.toInt())
                if (result != 0) {
                    L.i(TAG, "规则1第${i + 1}行数据不正确,结果为：$result")
                }
            }
            for (i in 1..60) {//遍历每行,规则2、无需阅卷数=无需阅卷中省考试院10科+无需阅卷中分配给阅卷老师
                val result = (sheet.getCell(3, i).contents.toInt()
                        - sheet.getCell(4, i).contents.toInt()
                        - sheet.getCell(6, i).contents.toInt())
                if (result != 0) {
                    L.i(TAG, "规则2第${i + 1}行数据不正确,结果为：$result")
                }
            }
            for (i in 1..60) {//遍历每行,规则3、总报考科数（自己学院无阅卷的）=无需阅卷中省考试院10科+无需阅卷中分配给阅卷老师+纸考阅卷+其中自考办阅卷数
                val result = (sheet.getCell(3, i).contents.toInt()
                        - sheet.getCell(4, i).contents.toInt()
                        - sheet.getCell(6, i).contents.toInt())
                if (result != 0) {
                    L.i(TAG, "规则3第${i + 1}行数据不正确,结果为：$result")
                }
            }

            for (i in 1..60) {//遍历每行,规则12、其中自考办阅卷数（加无需阅卷）【适用于校本部，教学点和衔接没有纸考阅卷的学校】=总报考科数
                val result = (sheet.getCell(2, i).contents.toInt()
                        - (if (sheet.getCell(7, i).contents.isEmpty()) 0 else sheet.getCell(7, i).contents.toInt())
                        - sheet.getCell(14, i).contents.toInt())
                if (result != 0) {
                    L.i(TAG, "规则12第${i + 1}行数据不正确,结果为：$result")
                }
            }

            for (i in 1..60) {//遍历每行,规则13、总报考科数=院校阅卷数+其中自考办阅卷数（加无需阅卷）
                val result = (sheet.getCell(2, i).contents.toInt()
                        - (if (sheet.getCell(7, i).contents.isEmpty()) 0 else sheet.getCell(7, i).contents.toInt())
                        - sheet.getCell(14, i).contents.toInt())
                if (result != 0) {
                    L.i(TAG, "规则13第${i + 1}行数据不正确,结果为：$result")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            workbook?.close()
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
}
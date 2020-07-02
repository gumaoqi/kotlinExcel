package com.gumaoqi.test.kotlinbaseproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.BaseAdapter
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.entity.MineBean
import java.io.File

class ExcelAdapter : BaseAdapter() {
    lateinit var adapterList: List<Any>

    fun getList(): List<Any>? {
        return adapterList
    }

    fun setList(list: List<Any>) {
        this.adapterList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                0 -> {
                    val view = LayoutInflater.from(GuApplication.context).inflate(R.layout.item_excel, viewGroup, false)
                    val viewHolder = ContentViewHolder(view)
                    viewHolder
                }
                else -> {
                    val view = LayoutInflater.from(GuApplication.context).inflate(R.layout.item_load_more, viewGroup, false)
                    val viewHolder = LoadMoreViewHolder(view)
                    viewHolder
                }
            }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ContentViewHolder -> {
                val file = adapterList[position] as File
                val name = file.absolutePath.subSequence(file.absolutePath.lastIndexOf("/") + 1, file.absolutePath.length)
                viewHolder.itemExcelTv.text = name
//                viewHolder.itemExcelLl.setOnClickListener {
//                    setMessageToActivity(HandlerArg.EXECUTE_SCRIPT, 0, file)
//                }
                viewHolder.itemExcelBt.setOnClickListener {
                    setMessageToActivity(HandlerArg.EXECUTE_SCRIPT, 0, file)
                }
            }
            is LoadMoreViewHolder -> {
                viewHolder.itemLoadMoreTv.text = adapterInfo
//                viewHolder.itemLoadMoreRootLl.setOnClickListener {
//                    setMessageToActivity(HandlerArg.MINE, 0, adapterInfo)
//                }
            }
        }
    }

    override fun getItemCount(): Int =
            if (::adapterList.isInitialized) {
                adapterList.size + 1
            } else {
                1
            }

    override fun getItemViewType(position: Int): Int =
            if (::adapterList.isInitialized) {
                if (position > adapterList.size - 1) {
                    1
                } else {
                    0
                }
            } else {
                1
            }

    /**
     * 普通item的view
     */
    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemExcelTv: TextView = view.findViewById(R.id.item_excel_tv)
        val itemExcelLl: LinearLayout = view.findViewById(R.id.item_excel_ll)
        val itemExcelBt: Button = view.findViewById(R.id.item_excel_bt)
    }
}
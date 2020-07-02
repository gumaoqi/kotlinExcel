package com.gumaoqi.test.kotlinbaseproject.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.gumaoqi.test.kotlinbaseproject.LoginActivity
import com.gumaoqi.test.kotlinbaseproject.R
import com.gumaoqi.test.kotlinbaseproject.base.ActivityCollector
import com.gumaoqi.test.kotlinbaseproject.base.BaseFragment
import com.gumaoqi.test.kotlinbaseproject.base.GuApplication
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.GET_BITMAP
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.OPEN_ALBUM
import com.gumaoqi.test.kotlinbaseproject.base.HandlerArg.Companion.TAKE_PHOTO
import com.gumaoqi.test.kotlinbaseproject.entity.DeleteBean
import com.gumaoqi.test.kotlinbaseproject.kt.show
import com.gumaoqi.test.kotlinbaseproject.tool.*
import com.gumaoqi.test.kotlinbaseproject.viewmodel.UpdateHeadImgFVM
import kotlinx.android.synthetic.main.fragment_update_head_img.*
import java.io.File

class UpdateHeadImgFragment : BaseFragment() {

    private lateinit var gHandler: Handler
    private lateinit var vm: UpdateHeadImgFVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_head_img, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProviders.of(this).get(UpdateHeadImgFVM::class.java)
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
                HandlerArg.SUCCESS -> {
                }
                GET_BITMAP -> {
                    val bitmap = msg.obj as Bitmap
                    val bitmapSize = C.bitmapToBytes(bitmap).size
                    L.i(TAG, "大小：$bitmapSize")
                    L.i(TAG, "内容：" + C.bitmapToBytes(bitmap))
                    if (bitmapSize > GuApplication.maxPicSize) {
                        "选择的照片不能超过6M".show()
                        return@Callback false
                    }
                    fragment_update_head_img_iv.setImageBitmap(bitmap)
                }
                HandlerArg.DELETE_USER_BACK -> {
                    val deleteBean = msg.obj as DeleteBean
                    if (deleteBean.msg == "ok") {
                        "删除用户成功".show()
                        S.clearSharedPreferences()
                        ActivityCollector.finishAll()
                        startActivity(Intent(GuApplication.context, LoginActivity::class.java))
                    }
                }
            }
            false
        })
    }

    override fun setView() {
        super.setView()
        Glide.with(GuApplication.context).load(S.getString("c10")).error(R.mipmap.ic_launcher).into(fragment_update_head_img_iv)
        fragment_update_head_img_one_bt.setOnClickListener {
            vm.outputImage = File(GuApplication.context.externalCacheDir, "output_image.jpg")
            if (vm.outputImage.exists()) {
                vm.outputImage.delete()
            }
            vm.outputImage.createNewFile()
            vm.imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(activity!!, "com.gumaoqi.test.kotlinbaseproject.fileProvider", vm.outputImage)
            } else {
                Uri.fromFile(vm.outputImage)
            }
            startActivityForResult(
                    Intent("android.media.action.IMAGE_CAPTURE").putExtra(MediaStore.EXTRA_OUTPUT, vm.imageUri)
                    , TAKE_PHOTO)
        }
        fragment_update_head_img_two_bt.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_ALBUM)
        }
        fragment_update_head_img_three_bt.setOnClickListener {
            "暂时无法上传，需要备案域名".show()
//            addParamdeleteUser()
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * 添加参数修改个人信息
     */
    private fun addParamdeleteUser() {
        val paramMap = HashMap<String, String>()
        paramMap["objectid"] = S.getString("object_id")
        paramMap["tablename"] = "shop_user"
        N.deleteByRetrofit(paramMap, gHandler, "删除该用户", HandlerArg.DELETE_USER_BACK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    L.i(TAG, vm.imageUri.toString())
                    Glide.with(GuApplication.context).load(vm.outputImage)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(MyTarget())
                }
            }
            OPEN_ALBUM -> {
                if (resultCode == RESULT_OK && data != null) {
                    Glide.with(GuApplication.context).load(data.data)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(MyTarget())
                }
            }
        }
    }

    /**
     * 打电话的方法
     */
    private fun call() {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:10086")
            startActivity(intent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    /**
     * 内部类，用于获取glide加载图片时的bitmap
     */
    inner class MyTarget : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
            val massage = gHandler.obtainMessage()
            massage.arg1 = GET_BITMAP
            massage.obj = resource
            gHandler.sendMessageDelayed(massage, 100)
        }
    }

}
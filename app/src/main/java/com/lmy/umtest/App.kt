package com.lmy.umtest

import android.app.Application
import android.content.pm.PackageManager
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class App : Application() {
    companion object {
        lateinit var mInstance: App
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initUM()
    }

    /**
     * 初始化友盟统计
     */
    private fun initUM() {
        //友盟SDK Log开关
        UMConfigure.setLogEnabled(true)
        //设置自动采集信息
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, "604052d4b8c8d45c138a95af", null, UMConfigure.DEVICE_TYPE_PHONE, "")
    }

    /**
     * 获取友盟渠道名
     * @return 如果没有获取成功，那么返回值为空
     */
    fun getChannelName(): String {
        var channelName = "官方"
        try {
            val packageManager = packageManager
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                val applicationInfo =
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData["UMENG_CHANNEL"].toString() + ""
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return channelName
    }
}
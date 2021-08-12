package cn.idu.iwan.app

import android.app.Activity
import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class IWanApp : Application() {

    companion object{
        lateinit var app: IWanApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        /****TODO ARouter init****/
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }
}
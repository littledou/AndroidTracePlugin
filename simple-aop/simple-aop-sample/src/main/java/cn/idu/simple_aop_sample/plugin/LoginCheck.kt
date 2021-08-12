package cn.idu.simple_aop_sample.plugin

import android.app.AlertDialog
import android.content.Context
import cn.idu.simple_aop_sample.anno.ActivityCheck
import android.content.Intent
import cn.idu.simple_aop_sample.LoginActivity


class LoginCheck : ActivityCheck {

    private val isLogin = false//是否登陆标记

    override fun intercept(context: Context): Boolean {
        if (!isLogin) {
            AlertDialog.Builder(context)
                .setTitle("登录检查")
                .setMessage("未登录，请先登录之后再继续后续操作")
                .setPositiveButton("确定") { _, _ -> onConfirm(context) }
                .setNegativeButton("取消", null)
                .show()
        }
        return isLogin;
    }

    private fun onConfirm(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    companion object {
    }
}
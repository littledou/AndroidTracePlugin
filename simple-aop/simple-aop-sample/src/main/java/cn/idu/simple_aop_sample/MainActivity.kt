package cn.idu.simple_aop_sample

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openHome(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
package cn.idu.simple_aop_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.idu.simple_aop_sample.anno.Check
import cn.idu.simple_aop_sample.plugin.LoginCheck

@Check(LoginCheck::class)
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
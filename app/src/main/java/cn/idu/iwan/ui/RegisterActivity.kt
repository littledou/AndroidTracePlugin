package cn.idu.iwan.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.idu.iwan.app.Constants
import cn.idu.iwan.databinding.ActivityRegisterBinding
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = Constants.ACTIVITY_REGISTER)
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
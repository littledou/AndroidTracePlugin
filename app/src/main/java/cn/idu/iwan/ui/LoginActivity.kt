package cn.idu.iwan.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.idu.iwan.app.Constants
import cn.idu.iwan.databinding.ActivityLoginBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter


@Route(path = Constants.ACTIVITY_LOGIN)
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.LoginViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val _username_edittext = binding.common.loginUsername
        val _password_edittext = binding.common.loginPassword
        val _login_button = binding.common.loginLogin
        val _register_button = binding.common.loginRegister

        viewModel.isLogin.observe(this, { account ->
            println("account: $account")
            if (!TextUtils.isEmpty(account)) {
                ARouter.getInstance().build(Constants.ACTIVITY_HOME).navigation()
            }
        })

        _login_button.setOnClickListener {
            viewModel.login(
                _username_edittext.editableText.toString().trim(),
                _password_edittext.editableText.toString().trim()
            )
        }

        _register_button.setOnClickListener {
            ARouter.getInstance().build(Constants.ACTIVITY_REGISTER).navigation()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

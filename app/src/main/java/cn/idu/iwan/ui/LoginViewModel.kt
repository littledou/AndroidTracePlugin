package cn.idu.iwan.ui

import androidx.lifecycle.*
import cn.idu.anno.TraceTime
import cn.idu.iwan.app.Constants
import cn.idu.iwan.app.Constants.USER_NAME
import cn.idu.iwan.app.Constants.USER_PWD
import cn.idu.iwan.app.IWanApp
import cn.idu.iwan.ext.read
import cn.idu.iwan.ext.save
import kotlinx.coroutines.runBlocking

class LoginViewModel(val application: IWanApp) : AndroidViewModel(application) {


    private val _isLogin = MutableLiveData<String>()

    val isLogin: LiveData<String?> = _isLogin

    init {
        runBlocking {
            _isLogin.value = application.read(Constants.USER_NAME)
        }
    }

    @TraceTime("LoginViewModel:login:")
    fun login(username: String, password: String) {
        println("username: $username")
        println("password: $password")
        runBlocking {
            _isLogin.value = username
            application.save(USER_NAME, username);
            application.save(USER_PWD, password);
        }
    }


    class LoginViewModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(application = IWanApp.app) as T
        }

    }
}
package cn.idu.sample_proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


//========静态代理
interface Person {
    fun giveTask()
}

class Student(private var name: String) : Person {

    override fun giveTask() {
        println("$name giveTask!")
    }
}

class StudentProxy(private var person: Person) : Person {
    override fun giveTask() {
        person.giveTask()
    }
}

//==========动态代理
class MyInvocationHandler<T>(private val t: T) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        println("MyInvocationHandler 代理执行 ${method?.name} 方法.")
        //before
        val o: Any? = if (args?.size == null) {
            //invoke第二个参数不得为null，否则会报错java.lang.IllegalArgumentException: wrong number of arguments
            method?.invoke(t, *emptyArray())
        } else {
            method?.invoke(t, args)
        }
//        val o = method?.invoke(t, *(args ?: emptyArray()))
        //after
        return o
    }
}

//kotlin 动态代理
class StudentProxyKt(person: Person) : Person by person

class Teacher(private var name: String) {
    fun sleep() {
        println("teacher $name sleeping!")
    }
}

fun main() {
    val lin = Student("qian lin")

    //静态代理测试代码
    val proxy = StudentProxy(lin)
    proxy.giveTask()

    //动态代理测试代码
    val myInvocationHandler = MyInvocationHandler<Person>(lin)
    val personProxy = Proxy.newProxyInstance(
        Person::class.java.classLoader,
        arrayOf<Class<*>>(Person::class.java),
        myInvocationHandler
    ) as Person

    personProxy.giveTask()


    val studentProxy = StudentProxyKt(lin)
    studentProxy.giveTask()

    //测试cglib动态代理, 失败：Illegal reflective access by net.sf.cglib.core.ReflectUtils
//    val enhancer = Enhancer()
//    enhancer.setSuperclass(Teacher::class.java)
//    enhancer.setCallback(TeacherInterceptor())
//    val teacher = enhancer.create() as Teacher
//    teacher.sleep()

}

//class TeacherInterceptor : MethodInterceptor {
//    override fun intercept(
//        obj: Any?,
//        method: Method?,
//        params: Array<out Any>?,
//        proxy: MethodProxy?
//    ): Any? {
//        //before
//        println("${method?.name} method 调用前")
//        val o = proxy?.invoke(obj, params)//拦截方法调用前后
//        //after
//        println("${method?.name} method 调用后")
//        return o
//    }
//}

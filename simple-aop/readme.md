#### 代理模式示例

==simple-proxy==：代理模式示例

```kotlin
//什么是代理？
//代理类为委托类提供消息预处理、消息转发以及后处理

//接口类
interface Person{
    fun run()
}
//委托类
class Student(private name:String): Person{
    override fun run(){
        println("$name run!")
    }
}
val student = Student("li")
```


1. 静态代理

    ```kotlin
    //静态代理类定义
    class StudentProxy(private student:Student): Person{
        override fun run(){
            //before
            student.run()
            //after
        }
    }
    
    //静态代理类使用
    val studentProxy = StudentProxy(student)
    studentProxy.run()
    ```
    
2. 动态代理

    ```kotlin
    //动态代理类定义
    class MyInvocationHandler<T>(private val t: T) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            println("代理执行 ${method?.name} 方法.")
            //before
            val o: Any? = if (args?.size == null) {
                //invoke第二个参数不得为null，否则会报错java.lang.IllegalArgumentException: wrong number of arguments
                method?.invoke(t)
            } else {
                method?.invoke(t, args)
            }
    //        val o = method?.invoke(t, *(args ?: emptyArray()))
            //after
            return o
        }
    }
    
    //动态代理类使用
    val myInvocationHandler = MyInvocationHandler<Person>(student)
    val studentProxy = Proxy.newProxyInstance(
    	Person::class.java.classLoader,
        arrayOf<Class<*>>(Person::class.java),
        myInvocationHandler
    ) as Person
    studentProxy.run()
    ```

==simple-aop-sample==：


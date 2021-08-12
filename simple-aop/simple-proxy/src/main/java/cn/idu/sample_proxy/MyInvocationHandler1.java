package cn.idu.sample_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler1<T> implements InvocationHandler {
    private T t;

    public MyInvocationHandler1(T t) {
        this.t = t;
    }

    public T getTargetInstance() {
        return (T) Proxy.newProxyInstance(
                t.getClass().getClassLoader(),
                t.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

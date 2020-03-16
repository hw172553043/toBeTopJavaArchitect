## Spring的两种动态代理：Jdk和Cglib 的区别和实现



 ### 一、原理区别：

- java动态代理是利用反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用InvokeHandler来处理。
- cglib动态代理是利用asm开源包，对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。

1. 如果目标对象实现了接口，默认情况下会采用JDK的动态代理实现AOP 
2. 如果目标对象实现了接口，可以强制使用CGLIB实现AOP 
3. 如果目标对象没有实现了接口，必须采用CGLIB库，spring会自动在JDK动态代理和CGLIB之间转换

**如何强制使用CGLIB实现AOP？**

 （1）添加CGLIB库，SPRING_HOME/cglib/*.jar
 
 （2）在spring配置文件中加入<aop:aspectj-autoproxy proxy-target-class="true"/>

**JDK动态代理和CGLIB字节码生成的区别？**
1. JDK动态代理只能对实现了接口的类生成代理，而不能针对类
2. CGLIB是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法
   因为是继承，所以该类或方法最好不要声明成final 

### 二、代码实现

用户管理接口
```java
package com.lf.shejimoshi.proxy.entity;
//用户管理接口
public interface UserManager {
    //新增用户抽象方法
    void addUser(String userName,String password);
    //删除用户抽象方法
    void delUser(String userName);
    
}
```

用户管理接口实现类
```java
package com.lf.shejimoshi.proxy.entity;
//用户管理实现类,实现用户管理接口
public class UserManagerImpl implements UserManager{
    //重写新增用户方法
    @Override
    public void addUser(String userName, String password) {
        System.out.println("调用了新增的方法！");
        System.out.println("传入参数为 userName: "+userName+" password: "+password);
    }
    //重写删除用户方法
    @Override
    public void delUser(String userName) {
        System.out.println("调用了删除的方法！");
        System.out.println("传入参数为 userName: "+userName);
    }
    
}
```

#### JDK动态代理
```java
package com.lf.shejimoshi.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.lf.shejimoshi.proxy.entity.UserManager;
import com.lf.shejimoshi.proxy.entity.UserManagerImpl;
//JDK动态代理实现InvocationHandler接口
public class JdkProxy implements InvocationHandler {
    private Object target ;//需要代理的目标对象
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JDK动态代理，监听开始！");
        Object result = method.invoke(target, args);
        System.out.println("JDK动态代理，监听结束！");
        return result;
    }
    //定义获取代理对象方法
    private Object getJDKProxy(Object targetObject){
        //为目标对象target赋值
        this.target = targetObject;
        //JDK动态代理只能针对实现了接口的类进行代理，newProxyInstance 函数所需参数就可看出
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }
    
    public static void main(String[] args) {
        JdkProxy jdkProxy = new JdkProxy();//实例化JDKProxy对象
        UserManager user = (UserManager) jdkProxy.getJDKProxy(new UserManagerImpl());//获取代理对象
        user.addUser("admin", "123123");//执行新增方法
    }
    
}
```
JDK动态代理运行结果
![JDK动态代理运行结果](https://images2017.cnblogs.com/blog/985411/201801/985411-20180111010236347-2008865961.png)


#### Cglib动态代理（需要导入两个jar包，asm-5.2.jar,cglib-3.2.5.jar。版本自行选择）
```java
package com.lf.shejimoshi.proxy.cglib;

import java.lang.reflect.Method;

import com.lf.shejimoshi.proxy.entity.UserManager;
import com.lf.shejimoshi.proxy.entity.UserManagerImpl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

//Cglib动态代理，实现MethodInterceptor接口
public class CglibProxy implements MethodInterceptor {
    private Object target;//需要代理的目标对象
    
    //重写拦截方法
    @Override
    public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
        System.out.println("Cglib动态代理，监听开始！");
        Object invoke = method.invoke(target, arr);//方法执行，参数：target 目标对象 arr参数数组
        System.out.println("Cglib动态代理，监听结束！");
        return invoke;
    }
    //定义获取代理对象方法
    public Object getCglibProxy(Object objectTarget){
        //为目标对象target赋值
        this.target = objectTarget;
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);// 设置回调 
        Object result = enhancer.create();//创建并返回代理对象
        return result;
    }
    
    public static void main(String[] args) {
        CglibProxy cglib = new CglibProxy();//实例化CglibProxy对象
        UserManager user =  (UserManager) cglib.getCglibProxy(new UserManagerImpl());//获取代理对象
        user.delUser("admin");//执行删除方法
    }
    
}
```

Cglib动态代理运行结果
![Cglib动态代理运行结果](https://images2017.cnblogs.com/blog/985411/201801/985411-20180111010415222-40358282.png)


 

 

 

 









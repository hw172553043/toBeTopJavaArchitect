## java序列化及项目中常用的序列化工具比较


### 1. 我们知道在java中有序列化的概念

序列化的过程就是将对象转变成字节码，反序列化即是从字节码转换成对象的过程一般情况下要求实现Serializable接口，
此接口中没有定义任何成员，只是起到标记对象是否可以被序列化的作用。为何需要有序列化呢？一方面是为了存储在磁盘中，另一个作用就是作为网络远程传输的内容。

### 2. java中实现序列化
需要类实现了Serializable或Externalizable接口，否则会抛出异常，然后使用ObjectOutputStream与ObjectInputStream将对象写入写出

代码如下：
      
      //对象转成字节码
      ByteArrayOutputStream byteArrayOutputStream = new  ByteArrayOutputStream();
      ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);                   
      outputStream.writeObject(VoUtil.getUser());
      byte[] bytes = byteArrayOutputStream.toByteArray();
      outputStream.close();
      
      //字节码转换成对象
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);                 
      User result = (User) inputStream.readObject();
      inputStream.close();


### 3. 效率及其它序列化工具

java中实现的序列化效率是极低的，在小项目中使用还尚可，但对于高并发，对序列化速度要求比较高的项目是将会成为瓶颈问题，
目前常见的一些序列化工具都比其效率高(缺点是需要引入第三方的jar包)：
		
1. json/xml，目前使用比较频繁的格式化数据工具，简单直观，可读性好，有jackson，gson，fastjson等等，效率比java原生的序列化快2到4倍的样子
    
    **pom:**
    
    `
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.47</version>
    </dependency>
    `
		 
2. kryo，是一个快速序列化/反序列化工具，效率比java高出一个级别，序列化出来的结果，是其自定义的、独有的一种格式，体积更小，
   一般只用来进行序列化和反序列化，而不用于在多个系统、甚至多种语言间进行数据交换（目前 kryo 也只有 java 实现），目前已经有多家大公司使用，相对比较稳定。
		   
    官方文档：
    
    - 中文：https://blog.csdn.net/fanjunjaden/article/details/72823866
    - 英文：https://github.com/EsotericSoftware/kryo
	- pom:
	
	    `
            <dependency>
              <groupId>com.esotericsoftware</groupId>
              <artifactId>kryo</artifactId>
              <version>4.0.0</version>
            </dependency>
	    `
		   	    
			 
3. fst，与kryo类似是apache组织的一个开源项目，完全兼容JDK序列化协议的系列化框架，序列化速度大概是JDK的4-10倍，大小是JDK大小的1/3左右
   
   官方文档： https://github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
	
   pom:
   
   `
    <dependency>
      <groupId>de.ruedigermoeller</groupId>
      <artifactId>fst</artifactId>
      <version>2.56</version>
    </dependency>
   `
				
4. protostuff，是google在原来的protobuffer是的优化产品。使用起来也比较简单易用，目前效率也是最好的一种序列化工具。
   
   pom:
   `
    <dependency>
      <groupId>io.protostuff</groupId>
      <artifactId>protostuff-core</artifactId>
      <version>1.4.0</version>
    </dependency>
   `
			   
5. Hassion

待补充
	
	
附实验代码：

```java
public class JsonUtil {
 
 
    public static <T> JSON serializer(T t){
        return (JSON)JSONObject.toJSON(t);
    }
 
 
    public static <T> T deserializer(JSON json,Class<T> c) {
        return JSONObject.parseObject(json.toJSONString(),c);
    }
}
 
package com.xps.serilizer.protostuff;
 
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
 
 
/**
 * Created by xiongps on 2018/5/23.
 */
public class ProtostuffUtil {
 
    public static <T> byte[] serializer(T t){
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return ProtostuffIOUtil.toByteArray(t,schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
 
    }
 
 
    public static <T> T deserializer(byte []bytes,Class<T> c) {
        T t = null;
        try {
            t = c.newInstance();
            Schema schema = RuntimeSchema.getSchema(t.getClass());
             ProtostuffIOUtil.mergeFrom(bytes,t,schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
 
 
}
 
 
package com.xps.serilizer.kryo;
 
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.xps.serilizer.vo.User;
import com.xps.serilizer.vo.VoUtil;
import org.objenesis.strategy.StdInstantiatorStrategy;
 
 
import java.io.*;
 
 
/**
 * Created by xiongps on 2018/5/23.
 */
public class SerilizerTestUtil {
 
 
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            /**
             * 不要轻易改变这里的配置,更改之后，序列化的格式就会发生变化，
             * 上线的同时就必须清除 Redis 里的所有缓存，
             * 否则那些缓存再回来反序列化的时候，就会报错
             */
            //支持对象循环引用（否则会栈溢出）
            kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
 
 
            //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
            kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
 
 
            //Fix the NPE bug when deserializing Collections.
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };
 
 
    private static KryoFactory factory = new KryoFactory() {
        @Override
        public Kryo create() {
            Kryo kryo = new Kryo();
            /**
             * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
             * 上线的同时就必须清除 Redis 里的所有缓存，
             * 否则那些缓存再回来反序列化的时候，就会报错
             */
            //支持对象循环引用（否则会栈溢出）
            kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
 
 
            //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
            kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
 
            //Fix the NPE bug when deserializing Collections.
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };
 
 
    private static KryoPool pool = new KryoPool.Builder(factory).softReferences().build();
 
 
    public static void kryoSeriAsFile(){
        Kryo kryo = kryos.get();
        Output output = null;  Input input = null;
 
        try {
            output = new Output(new FileOutputStream("F:\\test\\file.bin"));
            User user = VoUtil.getUser();
            kryo.writeObject(output, user);
            output.flush();
            output.close();
 
            input = new Input(new FileInputStream("F:\\test\\file.bin"));
            User userAsOut = kryo.readObject(input, User.class);
            System.out.println(userAsOut.getUserNm());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
 
 
            input.close();
        }
 
    }
 
 
    public static void kryoSeriAsByte(){
        Kryo kryo = kryos.get();
        byte[] bytes = null;
        long st = System.nanoTime();
        //kryo.register(User.class,new JavaSerializer());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new
                    ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);
 
 
            //kryo.writeObject(output,getUser());
            kryo.writeClassAndObject(output,VoUtil.getUser());
            output.flush();
            output.close();
 
            bytes = byteArrayOutputStream.toByteArray();
 
            ByteArrayInputStream byteArrayInputStream = new
                    ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);
            //User result = (User) kryo.readObject(input,User.class);
            User result = (User) kryo.readClassAndObject(input);
            input.close();
            System.out.println(result.getUserNm()+"kryo time:"+(System.nanoTime()-st));
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
 
        }
    }
 
 
    public static void kryoGetAsPoolSeriAsByte(){
        Kryo kryo = pool.borrow();
        byte[] bytes = null;
        long st = System.nanoTime();
        //kryo.register(User.class,new JavaSerializer());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new
                    ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);
 
 
            //kryo.writeObject(output,getUser());
            kryo.writeClassAndObject(output,VoUtil.getUser());
            output.flush();
            output.close();
            bytes = byteArrayOutputStream.toByteArray();
 
            ByteArrayInputStream byteArrayInputStream = new
                    ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);
            //User result = (User) kryo.readObject(input,User.class);
            User result = (User) kryo.readClassAndObject(input);
            input.close();
            System.out.println(result.getUserNm()+"kryo1 time:"+(System.nanoTime()-st));
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            pool.release(kryo);
        }
 
    }
 
 
    public static void kryoGetAsPoolSeriAsByte2(){
        long st = System.nanoTime();
        try {
           User ret = pool.run(new KryoCallback<User>() {
                @Override
                public User execute(Kryo kryo) {
                    ByteArrayOutputStream byteArrayOutputStream = new
                            ByteArrayOutputStream();
                    Output output = new Output(byteArrayOutputStream);
                    //kryo.writeObject(output,getUser());
                    kryo.writeClassAndObject(output,VoUtil.getUser());
                    output.flush();
                    output.close();
                    byte[] bytes = null;
                    bytes = byteArrayOutputStream.toByteArray();
 
                    ByteArrayInputStream byteArrayInputStream = new
                            ByteArrayInputStream(bytes);
                    Input input = new Input(byteArrayInputStream);
                    //User result = (User) kryo.readObject(input,User.class);
                    User result = (User) kryo.readClassAndObject(input);
                    input.close();
                    return result;
                }
            });
            System.out.println(ret.getUserNm()+"kryo2 time:"+(System.nanoTime()-st));
 
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
 
        }
    }
 
 
    public static void javaSerilizer(){
        byte[] bytes = null;
        long st = System.nanoTime();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new
                    ByteArrayOutputStream();
            ObjectOutputStream outputStream = new
                    ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(VoUtil.getUser());
            outputStream.close();
 
 
            bytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new
                    ByteArrayInputStream(bytes);
 
 
            ObjectInputStream inputStream = new
                    ObjectInputStream(byteArrayInputStream);
            User result = (User) inputStream.readObject();
            inputStream.close();
            System.out.println(result.getUserNm()+"java  time:"+(System.nanoTime()-st));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
 
        }
 
 
    }
 
}	
 
 
 
package com.xps.serilizer.fst;
import org.nustaq.serialization.FSTConfiguration;
 
/**
 * Created by xiongps on 2018/5/23.
 */
public class FstUtil {
 
    private static ThreadLocal<FSTConfiguration> confs = new ThreadLocal(){
        public FSTConfiguration initialValue() {
            return FSTConfiguration.createDefaultConfiguration();
        }
    };
 
    private static FSTConfiguration getFST(){
        return confs.get();
    }
 
    public static <T> byte[] serializer(T t){
        return getFST().asByteArray(t);
    }
 
 
    public static <T> T deserializer(byte []bytes,Class<T> c) {
        return  (T)getFST().asObject(bytes);
    }
 
}

```

结果：

```java
循环10次,共用时(毫秒)：
javaTime      :3.978881
kryoTime      :1.041478
protostuffTime:0.468708
fstTime       :0.860724
jsonTime      :1.340264

循环100次,共用时(毫秒)：
javaTime      :42.63442
kryoTime      :12.16559
protostuffTime:5.23874
fstTime       :9.432315
jsonTime      :16.042395

循环1000次,共用时(毫秒)：
javaTime      :335.050281
kryoTime      :114.677958
protostuffTime:54.912879
fstTime       :109.997724
jsonTime      :151.238032

```



![参考文档](https://blog.csdn.net/xpsallwell/article/details/80421882)










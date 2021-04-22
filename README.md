# SpringBoot load-time-weaver with tomcat8
### 什么是Load Time Weaving？

字面意思即类加载时织入，即通过字节码编辑技术将切面织入目标类

### 实现方式

主要实现方式有通过javaagent和自定义ClassLoader实现，本文讨论的是第二种，实现原理大概就是在自定义classLoader中加入ClassFileTransformer实例，从而在loade时调用ClassFileTransformer进行织入操作。在spring中通过```AspectJWeavingEnabler.enableAspectJWeaving```方法实现，真正实现织入操作的是ClassFileTransformer



参考spring官网的例子写了个ltw的demo，发现在tomcat环境下（不使用Instrumentation），使用```@EnableLoadTimeWeaving```注解无法对@Bean、@Component 和@Configuration注解的bean 织入advice，比如Controller、Service等等，但是普通的new出来的对象能够正常aop。查看debug日志如下，果然如此

```
 debug not weaving ...
```

为什么Controller、Service无法被织入，而不通过spring加载的对象能够被织入增强？猜测可能时spring容器中的bean 的类load时机的问题导致。跟踪```@EnableLoadTimeWeaving```代码，发现该注解import了一个```LoadTimeWeavingConfiguration```配置类，在类中通过@Bean注解定义了一个单例bean LoadTimeWeaver，该bean在实例化的过程中才会调用```AspectJWeavingEnabler.enableAspectJWeaving```从而使，ClassFileTransformer加入的classLoader中去。

众所周知单例bean在spring工厂最后阶段finishBeanFactoryInitialization方法中完成创建，而@Bean,@Component等注解定义的bean是通过```ConfigurationClassPostProcessor```扫描到spring 工厂中并完成bean class的加载的，这说明@Bean、@Component定义的bean的class加载远远早于往classLoader加入ClassFileTransformer的这个动作，说白了，在load @Bean、@Component等定义的bean的class时，classLoader中还没有ClassFileTransformer，这种情况aop当前不会生效。

知道失效原因了，解决就简单了，思路就是在```ConfigurationClassPostProcessor```扫描加载bean class之前，启用load time weaver ,代码如下：

```
//添加一个ApplicationContextInitializer,在初始化applicationContext时就启用load-time-weaver
builder.initializers(applicationContext-> {
        LoadTimeWeaver loadTimeWeaver = new DefaultContextLoadTimeWeaver( applicationContext.getBeanFactory().getBeanClassLoader());
        AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver,  applicationContext.getBeanFactory().getBeanClassLoader());
       // ((DefaultListableBeanFactory)applicationContext.getBeanFactory()).setAllowEagerClassLoading(false);
});
return builder.sources(DemoApplication.class);
```

 
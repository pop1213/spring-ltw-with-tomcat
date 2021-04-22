package com.example.demo;

import com.example.demo.beanFatoryProcessor.MyBeanFactoryProcessor;
import org.apache.catalina.loader.ParallelWebappClassLoader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
        SpringApplicationBuilder builder=new SpringApplicationBuilder();
        //builder.application().getSources().add("classpath:beans.xml");
        builder.sources(DemoApplication.class);
        builder.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.initializers(applicationContext-> {
                LoadTimeWeaver loadTimeWeaver = new DefaultContextLoadTimeWeaver( applicationContext.getBeanFactory().getBeanClassLoader());
                AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver,  applicationContext.getBeanFactory().getBeanClassLoader());
               // ((DefaultListableBeanFactory)applicationContext.getBeanFactory()).setAllowEagerClassLoading(false);
        });
        return builder.sources(DemoApplication.class);
    }
}

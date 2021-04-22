package com.example.demo.beanFatoryProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.stereotype.Component;


public class MyBeanFactoryProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, BeanClassLoaderAware {
    private ClassLoader beanClassLoader;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LoadTimeWeaver loadTimeWeaver = new DefaultContextLoadTimeWeaver( beanFactory.getBeanClassLoader());

        AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver,  beanFactory.getBeanClassLoader());

    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader=classLoader;
    }
}

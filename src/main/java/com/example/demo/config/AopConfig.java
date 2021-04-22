package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;


@Configuration
public class AopConfig {

   // @Bean
    public LoadTimeWeaver loadTimeWeaver(){
        return new DefaultContextLoadTimeWeaver();
    }


   // @Bean
    public AspectJWeavingEnabler aspectJWeavingEnabler(LoadTimeWeaver loadTimeWeaver){
        AspectJWeavingEnabler aspectJWeavingEnabler= new AspectJWeavingEnabler();
        aspectJWeavingEnabler.setLoadTimeWeaver(loadTimeWeaver);
        return aspectJWeavingEnabler;
    }
}

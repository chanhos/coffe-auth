package com.coffepotato.config;

import com.coffepotato.common.object.AutowiringSpringBeanJobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
public class SchedulerConfig {

    @Bean
    public SchedulerFactoryBean setScheduler(ApplicationContext applicationContext){

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        //TODO:쿼츠 스케쥴러구현

        return schedulerFactoryBean;
    }
}

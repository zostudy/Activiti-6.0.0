package com.zos.workflow;

import com.google.common.collect.Lists;
import org.activiti.app.conf.ApplicationConfiguration;
import org.activiti.app.servlet.ApiDispatcherServletConfiguration;
import org.activiti.app.servlet.AppDispatcherServletConfiguration;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
/**
 * 根容器配置文件
 */
@Import({ApplicationConfiguration.class})
public class WorkflowApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

    /**
     * 配置程序入口
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WorkflowApplication.class);
    }

    @Bean
    public ServletRegistrationBean apiDispatcher() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        /**
         * 引入子容器
         */
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        dispatcherServlet.setContextConfigLocation(ApiDispatcherServletConfiguration.class.getName());
        servletRegistrationBean.setServlet(dispatcherServlet);
        servletRegistrationBean.addUrlMappings("/api/*");
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.setAsyncSupported(true);
        servletRegistrationBean.setName("api");
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean appDispatcher() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        /**
         * 引入子容器
         */
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        dispatcherServlet.setContextConfigLocation(AppDispatcherServletConfiguration.class.getName());
        servletRegistrationBean.setServlet(dispatcherServlet);
        servletRegistrationBean.addUrlMappings("/app/*");
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.setAsyncSupported(true);
        servletRegistrationBean.setName("app");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean openEntityManagerInViewFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new OpenEntityManagerInViewFilter());
        filterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterRegistrationBean.setName("openEntityManagerInViewFilter");
        filterRegistrationBean.setOrder(-200);
        filterRegistrationBean.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC));
        return filterRegistrationBean;
    }
}

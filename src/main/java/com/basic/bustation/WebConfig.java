package com.basic.bustation;

import com.basic.bustation.component.LoginHandlerInterceptor;
import com.basic.bustation.listener.IniitDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
//import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hp-pc on 2021/2/12.
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

  /*  @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main.html").setViewName("temp");
    }*/

    private Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("配置静态资源所在目录");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/asserts/**")
                .addResourceLocations("classpath:/static/asserts/");
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
          .addPathPatterns("/send_road_queryroad.action","/send_section_querysection.action");
    }

    // 用于处理编码问题
    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        registration.setFilter(characterEncodingFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("encoding","utf-8");
        registration.setName("encodingFilter");
        return registration;
    }

    ///解决Hibernate OpenSessionInView
    @Bean
    public FilterRegistrationBean OpenSessionInViewFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        OpenSessionInViewFilter openSessionInViewFilter=new OpenSessionInViewFilter();
        registration.setFilter(openSessionInViewFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("singleSession", "true");
        registration.setName("OpenSessionInViewFilter");
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean InitDataListenre(){
        ServletListenerRegistrationBean registrationBean=new ServletListenerRegistrationBean();
        IniitDataListener iniitDataListener=new IniitDataListener();
        registrationBean.setListener(iniitDataListener);
        registrationBean.setName("initDataListener");
        registrationBean.setEnabled(true);
        return registrationBean;
    }

//    @Bean
//    public ServletListenerRegistrationBean InitDataListenre(){
//        ServletListenerRegistrationBean registrationBean=new ServletListenerRegistrationBean();
//        IniitDataListener iniitDataListener=new IniitDataListener();
//        registrationBean.setListener(iniitDataListener);
////        registrationBean.setName("initDataListener");
//        registrationBean.setEnabled(true);
//        return registrationBean;
//    }

//    @Bean
//    public ServletRegistrationBean dispatcherServletRegistration() {
//        OL4JSFProxy ol4JSFProxyServlet=new OL4JSFProxy();
//        ServletRegistrationBean registration = new ServletRegistrationBean(ol4JSFProxyServlet,"/OL4JSFProxy/*");
//        registration.setLoadOnStartup(2);
//        return registration;
//    }

}

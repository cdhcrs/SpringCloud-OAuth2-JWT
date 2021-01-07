package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    //注入三个provider，对应密码，手机号，邮箱三种登录方式
    @Bean
    PasswordAuthenticationProvider passwordAuthenticationProvider(){
        return new PasswordAuthenticationProvider();
    }
    @Bean
    PhoneAuthenticationProvider phoneAuthenticationProvider(){
        return new PhoneAuthenticationProvider();
    }
    @Bean
    EmailAuthenticationProvider emailAuthenticationProvider(){
        return new EmailAuthenticationProvider();
    }

    /**
     * 配置过滤器
     * @param authenticationManager
     * @return
     */
    AuthenticationProcessingFilter authenticationProcessingFilter(AuthenticationManager authenticationManager){
        AuthenticationProcessingFilter userPasswordAuthenticationProcessingFilter = new AuthenticationProcessingFilter();
        //为filter设置管理器
        userPasswordAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        //登录成功后跳转到OAuth2的认证地址
        userPasswordAuthenticationProcessingFilter.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.sendRedirect("http://localhost:8083/auth/oauth/authorize?client_id=c1&response_type=token&scope=all&redirect_uri=http://localhost:8083/auth/token.html");
        });
        //登录失败后跳转
        userPasswordAuthenticationProcessingFilter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.sendRedirect("/login/failure");
        });
        return userPasswordAuthenticationProcessingFilter;
    }

    //配置管理器
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //往管理器中添加provider
        auth.authenticationProvider(passwordAuthenticationProvider())
            .authenticationProvider(phoneAuthenticationProvider())
            .authenticationProvider(emailAuthenticationProvider());
    }

    // 认证管理器
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //注入密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); // 不加密
        return new BCryptPasswordEncoder();// BCryptPasswordEncoder加密
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    //配置拦截路径等信息
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeRequests().antMatchers("/user/**","/test/**").authenticated()
                .and()
                .formLogin().loginPage("/login/noLogin");//未登录跳转到此接口
/*.loginProcessingUrl("/process")
                .successForwardUrl("/login/success")
                .failureForwardUrl("/login/failure");*/

            http.addFilterBefore(authenticationProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
}
/*
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    // 认证管理器
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // 安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().

                authorizeRequests().
                antMatchers("/admin/p1").
                hasAnyAuthority("p1").

                antMatchers("/user/p2").
                hasAnyAuthority("p2").
                antMatchers("/login*")
                .permitAll().
                anyRequest().
                authenticated().
                and().
                formLogin();

    }
}
*/

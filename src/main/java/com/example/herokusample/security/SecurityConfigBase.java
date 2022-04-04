package com.example.herokusample.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityConfigBase extends WebSecurityConfigurerAdapter{

    private final OncePerRequestFilter authFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제
            .csrf().disable() // csrf 보안 토큰 disable 처리.
            .formLogin().disable() // 기본 form login 제거
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests() // 요청에 대한 권한 지정
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight Request 허용해주기
            .anyRequest().authenticated() // 모든 요청이 인증되어야한다.
            .and()
            .addFilterBefore(authFilter,
                    UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //인증 예외 URL 설정
        web.ignoring()
            .antMatchers(HttpMethod.GET ,"/")
            .antMatchers("/css/**")
            .antMatchers("/static/**")
            .antMatchers("/js/**")
            .antMatchers("/img/**")
            .antMatchers("/fonts/**")
            .antMatchers("/vendor/**")
            .antMatchers("/favicon.ico")
            .antMatchers("/pages/**")
            .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**");
    }
}

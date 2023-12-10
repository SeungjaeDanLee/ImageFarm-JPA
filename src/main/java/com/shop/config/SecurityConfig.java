package com.shop.config;

import com.shop.handler.CustomFormLoginSuccessHandler;
import com.shop.handler.CustomSocialLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/index")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/index")
        ;

        http.oauth2Login()
                .loginPage("/members/login")
                .defaultSuccessUrl("/index")
//                .successHandler(authenticationSuccessHandler())
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/index")
        ;

        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**","/banner/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**", "/index", "/mail/**",
                        "/sendEmail/**", "/category/**", "/icon/**", "/illust/**", "/photo/**","/supervisor/**",
                        "/payDown", "/download/**" ,"/index_pay","/search/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers("/supervisor/**").hasRole("SUPERVISOR")
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/mail/**")
                       .ignoringAntMatchers("/members/findId")
        ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationFormLoginSuccessHandler() {
        return new CustomFormLoginSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
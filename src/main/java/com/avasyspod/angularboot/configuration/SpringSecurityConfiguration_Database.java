package com.avasyspod.angularboot.configuration;

import com.avasyspod.angularboot.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by farshidkhalaj on 9/17/18.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration_Database extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsServiceImpl  userDetailsServiceImpl;

   @Bean
    public PasswordEncoder passwordEncoder() {

       return new BCryptPasswordEncoder();

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        System.out.println("Configure method called in SpringSecurityConfiguration_Database..........");

        http
                .authorizeRequests()
                .antMatchers("/api/user/**")
                .authenticated()
                .and()
                .httpBasic()
                .realmName(" Registration System")
                .and().csrf().disable();
                //.logout()
                //.logoutSuccessUrl("/");
                //.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.ACCEPTED));

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{

        System.out.println("Configure 2 method called in SpringSecurityConfiguration_Database..........");

        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());

    }

}
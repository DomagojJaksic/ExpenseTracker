package hr.fer.opp.project.controllers.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests().antMatchers("/api/").permitAll();//.anyRequest().authenticated();
        http.formLogin()
                .successForwardUrl("/api/login/success")
                .failureForwardUrl("/api/login/failure");
        http.logout().logoutUrl("/api/logout");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.headers().frameOptions().sameOrigin(); // fixes h2-console problem
        http.csrf().disable();
    }
}
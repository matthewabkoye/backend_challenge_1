package com.matt.test.config;

import com.matt.test.model.User;
import com.matt.test.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicEntryPoint authenticationEntryPoint;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private CustomeUserDetailService customeUserDetailService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customeUserDetailService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(username -> {
            Optional<User> optUser = userRepository.findByUsername(username);
            User u = optUser.orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

            UserDetails ud = new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(u.getRole());
                    Set<SimpleGrantedAuthority> set = new HashSet<>();
                    set.add(simpleGrantedAuthority);
                    return set;
                }

                @Override
                public String getPassword() {
                    return u.getPassword();
                }

                @Override
                public String getUsername() {
                    return u.getUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return false;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return false;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return false;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }
            };
            return ud;
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/user").permitAll()
                .antMatchers(HttpMethod.GET,"/actuator").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/user").hasRole("Seller")
//                 authenticated()
                .and()
                .httpBasic()
                .realmName("Matthew")

                .authenticationEntryPoint(authenticationEntryPoint);


//        http.addFilterAfter(new CustomFilter(),
//                BasicAuthenticationFilter.class);
        http.cors().and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
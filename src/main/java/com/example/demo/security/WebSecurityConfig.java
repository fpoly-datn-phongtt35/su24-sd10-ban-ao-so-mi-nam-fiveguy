package com.example.demo.security;

import com.example.demo.security.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

//    public WebSecurityConfig(, ) {
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtUserDetailsService = jwtUserDetailsService;
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // configure AuthenticationManager so that it knows from where to load
//        // user for matching credentials
//        // Use BCryptPasswordEncoder
//        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
@Bean
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
}

//    @Autowired
//    private OlAccountRepository accountRepository;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService(accountRepository);
//    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jwtUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder()); // Sử dụng phương thức passwordEncoder()
        return provider;
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
//                        .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
//                        .requestMatchers("/api/admin/rate/**").hasAnyAuthority("ADMIN", "EMPLOYEE")


//                        product
                        .requestMatchers("/api/admin/bill-th/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/brand-th/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/category/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/collar/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/color/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/customer-th/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/material/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/product/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/product-detail/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/size/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/wrist/**").hasAnyAuthority("ADMIN", "EMPLOYEE")


//                            thống kê
                        .requestMatchers("/api/admin/bill-tinh/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/bill-history-tinh/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/billdetail-tinh/**").hasAuthority("ADMIN")


//                        customer
                        .requestMatchers("/api/admin/account/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/address/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/customer/**").hasAuthority("ADMIN")

//                            employee
                        .requestMatchers("/api/admin/audit-log/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/employee/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/customer/**").hasAuthority("ADMIN")

//                        Bill
                        .requestMatchers("/api/admin/billDetail/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/billHistory/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/bill/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/productProperty/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/productDetail/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/productDetail/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/product/maxPrice").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/admin/product/minPrice").hasAnyAuthority("ADMIN", "EMPLOYEE")

//                        Voucher
                        .requestMatchers("/api/admin/customerTypeN/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/customerTypeVoucher/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/voucher/**").hasAuthority("ADMIN")

//                        point
                        .requestMatchers("/api/admin/point-settings/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/customer-types/**").hasAuthority("ADMIN")


//                        sale
//                        .requestMatchers("/api/admin/image/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/sales/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/sales/products/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/sales/product-sales/**").hasAuthority("ADMIN")

//                        rate
                        .requestMatchers("/api/admin/rate/**").hasAuthority("ADMIN")




  

//                        .requestMatchers("/api/ol/authenticated/**").authenticated()
                        .anyRequest().permitAll()
        )
                .cors().and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Thêm filter xác thực JWT vào trước filter xác thực UsernamePassword
                .logout(lg -> lg
                        .logoutUrl("/auth/logoff")
                        .logoutSuccessUrl("/auth/logoff/success")
                )
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint); // Xác định xử lý exception trong trường hợp xác thực không thành công

        return httpSecurity.build();
    }


}


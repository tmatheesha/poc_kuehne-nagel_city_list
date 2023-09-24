package com.kuehne_nagel.city_list.application.config;

import com.kuehne_nagel.city_list.application.filter.CustomAuthenticationFailureHandler;
import com.kuehne_nagel.city_list.application.filter.JwtRequestFilter;
import com.kuehne_nagel.city_list.application.filter.RestAccessDeniedHandler;
import com.kuehne_nagel.city_list.domain.services.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtService jwtService;

    /**
     * Configure all authenticated urls
     *
     * @param http
     * @throws Exception
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                // allow all who are accessing
//                .antMatchers("/com/v1.0/citymgt/user/create").permitAll()
//                .antMatchers("/com/v1.0/citymgt/auth/login").permitAll()
//                .antMatchers("/com/v1.0/citymgt/auth/refresh/token").permitAll()
//                // Allow for swagger access
//                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
//                // any other request must be authenticated
//                .anyRequest().authenticated()
//
//                // Handle exceptions
//                .and().exceptionHandling().accessDeniedHandler(new RestAccessDeniedHandler())
//                .authenticationEntryPoint(new RestAuthenticationEntryPoint(jwtService)).and()
//
//                // Add a filter to validate the tokens with every request
//                .addFilterBefore(new JwtRequestFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
//
//                // this disables session creation on Spring Security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/com/v1.0/citymgt/user/create",
                                "/com/v1.0/citymgt/auth/login",
                                "/com/v1.0/citymgt/auth/refresh/token",
                                "/swagger*/**").permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling((exceptionHandling) ->
                        exceptionHandling.accessDeniedHandler(new RestAccessDeniedHandler()))
                .addFilterBefore(new JwtRequestFilter(jwtService), UsernamePasswordAuthenticationFilter.class)

                // this disables session creation on Spring Security
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();

    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/com/v1.0/citymgt/auth/login");
    }*/

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        List<String> publicApis = List.of("**/user/**", "**/login");
        return (web) -> web.ignoring().requestMatchers(publicApis.toArray(String[]::new));
    }

    @Bean
    public  CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}

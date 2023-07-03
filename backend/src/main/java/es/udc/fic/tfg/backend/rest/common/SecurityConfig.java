/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package es.udc.fic.tfg.backend.rest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JwtFilter(authenticationManager(), jwtGenerator))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/signUp").permitAll()
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/loginFromServiceToken").permitAll()
                .antMatchers(HttpMethod.POST, "/databases/add").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/user/databases").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/databaseTypes").permitAll()
                .antMatchers(HttpMethod.DELETE, "/databases/*/delete").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/databases/*/update").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/tasks/add").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/tasks/*/cancel").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/tasks/*/delete").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/user/tasks").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/tasks/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/tasks/*/logs").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/catalog/tasks/*/logs/*").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/logs/*/delete").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/geoJson/database/*/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/databases/*/tables").hasRole("USER")
                .anyRequest().denyAll();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        return source;

    }

}

package bsoft.nl.channel;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/**").hasAuthority("USER")

                .anyRequest()
                .fullyAuthenticated()

                .and()
                .csrf().disable();

         /*
        http
                .cors()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/**").hasAuthority("USER")
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
//                .formLogin()
//                .and()

                .csrf()
                .disable();
                */
    }

}


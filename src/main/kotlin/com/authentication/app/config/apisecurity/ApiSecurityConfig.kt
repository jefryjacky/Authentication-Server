package com.authentication.app.config.apisecurity

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint

/**
 * Created by Jefry Jacky on 25/12/20.
 */
@Configuration
@EnableWebSecurity
class ApiSecurityConfig: WebSecurityConfigurerAdapter() {

    @Value("\${api.key}")
    private var apiKey:String = ""

    override fun configure(http: HttpSecurity) {
        val filter = ApiKeyFilter("API-KEY")
        filter.setAuthenticationManager(AuthenticationManager {
            val requestApiKey = it.principal
            if(requestApiKey != apiKey){
                throw BadCredentialsException("The API Key was not found or not the expected value")
            }
            it.isAuthenticated = true
            return@AuthenticationManager it
        })

        http.antMatcher("/api/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .addFilterBefore(ExceptionTranslationFilter(Http403ForbiddenEntryPoint()), filter.javaClass)
                .authorizeRequests()
                .anyRequest()
                .authenticated()
    }
}
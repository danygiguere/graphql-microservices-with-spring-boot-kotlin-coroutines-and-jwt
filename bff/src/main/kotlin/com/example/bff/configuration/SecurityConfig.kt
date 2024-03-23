package com.example.bff.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.core.userdetails.User;

@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig() {

    // https://docs.spring.io/spring-graphql/reference/security.html
    // https://github.com/spring-projects/spring-graphql/blob/1.0.x/samples/webflux-security/src/main/java/io/spring/sample/graphql/SecurityConfig.java
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeExchange {
                it.anyExchange().permitAll()
             }
            .httpBasic(withDefaults())
            .build()
    }

    @Bean
    @Suppress("deprecation")
    fun userDetailsService(): MapReactiveUserDetailsService {
        val userBuilder: User.UserBuilder = User.withDefaultPasswordEncoder()
        val rob: UserDetails = userBuilder.username("rob").password("rob").roles("USER").build()
        val admin: UserDetails = userBuilder.username("admin").password("admin").roles("USER", "ADMIN").build()
        return MapReactiveUserDetailsService(rob, admin)
    }
}
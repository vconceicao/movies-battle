package com.desafio.moviesbattle.security;

import com.desafio.moviesbattle.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtFilterToken jwtTokenFilter;



    @Override

    protected void configure(HttpSecurity http) throws Exception {

        //habilitando cors desabilitando csrf
        http.cors().and().csrf().disable();

        //setando o gerenciamento de sessao para stateless
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        //configuracao requesicoes nao autorizadas e excecoes
        http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())).and()
        .logout().and();


        http.authorizeRequests()
                //endpoints publicos
                .antMatchers("/").permitAll()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers(format("%s/**", "/rest-api-docs")).permitAll()
                .antMatchers(format("%s/**", "/swagger-ui/*")).permitAll()
                //endpoints privados
                .anyRequest().authenticated();



        var usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        http.addFilterBefore(jwtTokenFilter, usernamePasswordAuthenticationFilter.getClass());

    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(nomeUsuario ->
                usuarioRepo.findUserByUsername(nomeUsuario)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado " + nomeUsuario)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

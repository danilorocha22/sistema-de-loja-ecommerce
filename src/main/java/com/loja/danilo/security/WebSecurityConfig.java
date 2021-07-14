package com.loja.danilo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("all")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioDetailsConfig usuarioDetailsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests() //define com as requisições HTTP devem ser tratadas com relação à segurança.
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/vendas/list").hasAnyRole("ADMIN")
                .antMatchers("/clientes/list").hasAnyRole("ADMIN")
                .antMatchers("/clientes/form").hasAnyRole("ADMIN")
                .antMatchers("/produtos/list").hasAnyRole("ADMIN")
                .antMatchers("/produtos/form").hasAnyRole("ADMIN")
                .antMatchers("/clientes/formcadastro").permitAll()
                .antMatchers("/clientes/login").permitAll()
                .antMatchers("/clientes/savecadastro").permitAll()
                .antMatchers("/clientes/minhascompras").permitAll()
                .antMatchers("/vendas/minhascompras").hasAnyRole("EDITOR")
                .antMatchers("/vendas/catalogo").hasAnyRole("EDITOR")
                .antMatchers("/home").hasAnyRole("EDITOR")
                .antMatchers("/vendas/filtrardatacompras").permitAll()
                .anyRequest() //define que a configuração é válida para qualquer requisição.
                .authenticated() //define que o usuário precisa estar autenticado.
                .and()
                .formLogin() //define que a autenticação pode ser feita via formulário de login.
                .loginPage("/login") // passamos como parâmetro a URL para acesso à página de login que criamos
                .permitAll() //define que essa página pode ser acessada por todos, independentemente do usuário estar autenticado ou não.
                .and()
                .logout()
                .permitAll();

    }

    @Autowired
    public void configureUserDetails(AuthenticationManagerBuilder builder)
            throws Exception {
        builder.userDetailsService(usuarioDetailsConfig).passwordEncoder(new BCryptPasswordEncoder());
                /*.inMemoryAuthentication()
                .withUser("dan")
                .password("$2a$10$TAcbut40cj3rMmvLlSJGP.0Y0Lf1dM84ynBv8S5wMOXIyYqR0thQe")
                .roles("ADMIN", "EDITOR")
                .and()
                .withUser("teste")
                .password("$2a$10$TAcbut40cj3rMmvLlSJGP.0Y0Lf1dM84ynBv8S5wMOXIyYqR0thQe")
                .roles("EDITOR");*/
    }

     /**
     * Com o método, instanciamos uma instância do encoder BCrypt e deixando o controle dessa instância como responsabilidade do Spring.
     * Agora, sempre que o Spring Security necessitar disso, ele já terá o que precisa configurado.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

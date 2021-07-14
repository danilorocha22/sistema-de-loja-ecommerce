package com.loja.danilo;

import com.loja.danilo.models.Usuario;
import com.loja.danilo.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Configuration indica ao Spring que essa é uma classe de configuração.
 * Em seguida, é preciso estender a classe WebMvcConfigurer.
 * @author fagno
 */
@SuppressWarnings("all")
@Configuration
public class ConfiguracaoSpringMvc implements WebMvcConfigurer{

    @Autowired
    UsuarioRepository usuarioRepository;

    /**
     * Com a chamada a registry.addViewController(), estamos registrando um controller automático.
     * Com o método setViewName(), indicamos a view para atender a requisições direcionadas à URL / e /home.
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");

    }

}

package com.controlefacil.controlefacil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração do Spring para definir as regras de CORS (Cross-Origin Resource Sharing).
 * Esta configuração permite que o frontend acesse a API sem problemas de política de mesma origem.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura as regras de CORS para os endpoints da API.
     *
     * @param registry O registro de CORS a ser configurado.
     * Este método permite que as requisições de diferentes origens sejam aceitas,
     * definindo quais métodos, cabeçalhos e credenciais podem ser utilizados.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

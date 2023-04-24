package poc.rest.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    public RestTemplateBuilder builder;

    @Bean("insecureRestTemplate")
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }
    @Bean("secureRestTemplate")
    public RestTemplate sslRestTemplate(){
        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }
}

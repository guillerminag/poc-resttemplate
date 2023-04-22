package poc.rest.demo.galicia.config;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//Esto tomaba los parametros del pool pero no pasaba por el logger
//@Configuration
public class RestTemplateConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfiguration.class);
    @Value("${pom.commons.starter.trustStore}")
    private String trustStore;
    @Value("${pom.commons.starter.trustStorePassword}")
    private String trustStorePassword;
    @Value("${server.restTemplate.connection-ssl-max-total}")
    private int connectionSSLMaxTotal;
    @Value("${server.restTemplate.connection-ssl-max-per-route}")
    private int connectionSSLMaxPerRoute;
    @Value("${server.restTemplate.connection-max-total}")
    private int connectionMaxTotal;
    @Value("${server.restTemplate.connection-max-per-route}")
    private int connectionMaxPerRoute;
    @Value("${server.restTemplate.connection.timeout}")
    private int connectionTimeout;

    private HttpComponentsClientHttpRequestFactory validateSSL() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContextBuilder.create().loadTrustMaterial(ResourceUtils.getFile(trustStore), trustStorePassword.toCharArray()).build();
        } catch (Exception ex) {
            LOGGER.error("Ocurrio un error al cargar el keystore.jks configurado. {}", ex.getMessage());
            throw new RuntimeException(ex.getCause());
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", new SSLConnectionSocketFactory(sslContext)).build();
        PoolingHttpClientConnectionManager connectionManagerSSL = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManagerSSL.setMaxTotal(connectionSSLMaxTotal);
        connectionManagerSSL.setDefaultMaxPerRoute(connectionSSLMaxPerRoute);
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).setConnectionManager(connectionManagerSSL).build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean("ar.com.bancogalicia.psca.bff.card.configuration.restTemplates")
    public Map<String, RestTemplate> restTemplates(RestTemplateBuilder restTemplateBuilder) {
        Map<String, RestTemplate> map = new HashMap<>();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connectionMaxTotal);
        connectionManager.setDefaultMaxPerRoute(connectionMaxPerRoute);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate connectionRestTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout,
                1)).setReadTimeout(Duration.ofSeconds(connectionTimeout, 1))
                .requestFactory(() -> requestFactory).build();
        map.put("connectionRestTemplate", connectionRestTemplate);
        RestTemplate connectionSSLRestTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout,
                1)).setReadTimeout(Duration.ofSeconds(connectionTimeout, 1))
                .requestFactory(this::validateSSL).build();
        map.put("connectionSSLRestTemplate", connectionSSLRestTemplate);
        return map;
    }
}

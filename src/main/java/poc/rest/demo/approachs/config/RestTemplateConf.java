package poc.rest.demo.approachs.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//[10:49] Andres Maldonado

//esto con el parent 2.x funcionaba. con el 3 ya no
//@Configuration
public class RestTemplateConf {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConf.class);
    @Autowired
    private SSLProperties sslProperties;

    private HttpComponentsClientHttpRequestFactory validateSSL() {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(100);
        clientConnectionManager.setDefaultMaxPerRoute(100);
        String trustStore = sslProperties.getTrustStore();
        String trustStorePassword = sslProperties.getTrustStorePassword();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContextBuilder.create().loadTrustMaterial(ResourceUtils.getFile(trustStore), trustStorePassword.toCharArray()).build();
        } catch (Exception ex) {
            LOGGER.error("Ocurrio un error al cargar el keystore.jks configurado.", ex.getMessage());
            throw new RuntimeException(ex.getCause());
        }
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).setConnectionManager(clientConnectionManager).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return requestFactory;
    }

    @Bean("prueba.configuration.restTemplates")
    public Map<String, RestTemplate> restTemplates(RestTemplateBuilder restTemplateBuilder) {
        Map<String, RestTemplate> map = new HashMap<>();
        RestTemplate connectionRestTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(3, 1)).setReadTimeout(Duration.ofSeconds(3, 1)).build();
        map.put("connectionRestTemplate", connectionRestTemplate);
        RestTemplate connectionSSLRestTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(3, 1)).setReadTimeout(Duration.ofSeconds(3, 1)).requestFactory(() -> validateSSL()).build();
        map.put("connectionSSLRestTemplate", connectionSSLRestTemplate);
        return map;
    }
}

package poc.rest.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Value("${api.url}")
    private String url;
    @Value("${api.endpoint}")
    private String endpoint;
    @Value("${api.url.secure}")
    private String urlSecure;
    @Value("${api.endpoint.secure}")
    private String endpointSecure;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUrlSecure() {
        return urlSecure;
    }

    public void setUrlSecure(String urlSecure) {
        this.urlSecure = urlSecure;
    }

    public String getEndpointSecure() {
        return endpointSecure;
    }

    public void setEndpointSecure(String endpointSecure) {
        this.endpointSecure = endpointSecure;
    }
}

package poc.rest.demo.approachs.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLProperties {
    private String trustStore;
    private String trustStorePassword;

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}

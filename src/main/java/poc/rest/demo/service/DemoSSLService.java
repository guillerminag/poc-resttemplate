package poc.rest.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import poc.rest.demo.config.ApiConfig;
import poc.rest.demo.dto.ListResponse;
import poc.rest.demo.dto.UserDTO;

import java.util.List;

@Service
public class DemoSSLService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoSSLService.class);
    @Autowired
    @Qualifier("secureRestTemplate")
    public RestTemplate restTemplate;
    @Autowired
    private ApiConfig apiConfig;

    public DemoSSLService() {

    }
    public List<UserDTO> getAll() {
        ResponseEntity<ListResponse> response;
        try {
            response = restTemplate.getForEntity(apiConfig.getUrlSecure()
                            + apiConfig.getEndpointSecure(), ListResponse.class);

        }catch(Exception ex){
            LOGGER.error("Error: ",ex);
            ListResponse restpApi = new ListResponse();
            restpApi.setStatus("Fail");
            response=  ResponseEntity.internalServerError().body(restpApi);

        }
        return response.getBody().getData();
    }
}

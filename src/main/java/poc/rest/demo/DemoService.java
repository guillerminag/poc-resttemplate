package poc.rest.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service

public class DemoService {

    private RestTemplate restTemplate;
    @Autowired
    private ApiConfig apiConfig;

    public DemoService(RestTemplateBuilder builder) {
        this.restTemplate=builder.build();
    }

    public List<UserDTO> getAll(){
        ResponseEntity<ListResponse> response;
        try {
            response = restTemplate.getForEntity(apiConfig.getUrl() + apiConfig.getEndpoint(),
                    ListResponse.class);

        }catch(Exception ex){
            System.out.println(ex.fillInStackTrace());
            ListResponse respApi = new ListResponse();
            respApi.setStatus("Fail");
            response=  ResponseEntity.internalServerError().body(respApi);

        }
        return response.getBody().getData();
    }
}

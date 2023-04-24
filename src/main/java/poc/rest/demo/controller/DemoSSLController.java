package poc.rest.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poc.rest.demo.dto.UserDTO;
import poc.rest.demo.service.DemoSSLService;

import java.util.List;

@RestController
@RequestMapping("/poc/secure/users")
public class DemoSSLController {
    @Autowired
    private DemoSSLService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){

        List<UserDTO> all = service.getAll();
        return new ResponseEntity<List<UserDTO>>(all, HttpStatus.OK);
    }
}

package poc.rest.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/poc/users")
public class DemoController {

    @Autowired
    private DemoService service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){

        List<UserDTO> all = service.getAll();
        return new ResponseEntity<List<UserDTO>>(all, HttpStatus.OK);
    }

}

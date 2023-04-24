package poc.rest.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/poc/simulate")
public class SimulatorController {

    @GetMapping(value = "/with-delay/{time}")
    public ResponseEntity<Map> getTomcatMetrics(@PathVariable Integer time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
        return new ResponseEntity<Map>(HttpStatus.OK);
    }
}

package poc.rest.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poc.rest.demo.monitoring.tomcat.TomcatMetricsService;

import javax.management.*;
import java.util.Map;

@RestController
@RequestMapping("/poc/monitoring")
public class MonitoringController {

    @Autowired
    private TomcatMetricsService tomcatService;



    @GetMapping(value = "/tomcat")
    public ResponseEntity<Map> getTomcatMetrics(){
        Map metrics = tomcatService.getMetrics();
        return new ResponseEntity<Map>(metrics, HttpStatus.OK);
    }
    @PostMapping(value = "/tomcat")
    public ResponseEntity<Map> setTomcatMetrics(@RequestBody Map<String,Double>newMetrics) throws MalformedObjectNameException, IntrospectionException, ReflectionException, InstanceNotFoundException, MBeanException, InvalidAttributeValueException, AttributeNotFoundException {
        Map metrics = tomcatService.setMetrics(newMetrics);
        return new ResponseEntity<Map>(metrics, HttpStatus.OK);
    }

}
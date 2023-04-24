package poc.rest.demo.monitoring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.rest.demo.monitoring.MonitoringService;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import java.lang.management.ManagementFactory;
import java.util.List;

@Service
public class ResTemplateMetricsService {

    @Autowired
    private MonitoringService monitoringService;


}

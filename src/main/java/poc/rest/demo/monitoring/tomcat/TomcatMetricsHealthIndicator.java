package poc.rest.demo.monitoring.tomcat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("TomcatMetricsHealthIndicator")
public class TomcatMetricsHealthIndicator implements HealthIndicator {


    @Autowired
    private TomcatMetricsService tomcatService;
    private boolean up = true;

    @Override
    public Health health() {
        Map<String, Double> metrics = tomcatService.getMetrics();
        double busyThreadsCount = metrics.get(TomcatMetricsService.TOMCAT_BUSY_THEADS);
        double allThreadsCount =metrics.get(TomcatMetricsService.TOMCAT_MAX_THREADS);
        double currentThreadCount=metrics.get(TomcatMetricsService.TOMCAT_CURRENT_THREADS);
        if((busyThreadsCount/allThreadsCount)>0.95){
            up=false;
        }else {
            up=true;
        }
        if(up){
            return new Health.Builder(Status.UP,metrics).build();
        }

        return new Health.Builder(Status.DOWN,metrics).build();
    }


}

package poc.rest.demo.monitoring.tomcat;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.rest.demo.monitoring.MonitoringService;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TomcatMetricsService {
    public static final String TOMCAT_BUSY_THEADS = "Tomcat Busy Theads";
    public static final String TOMCAT_MAX_THREADS = "Tomcat Max Threads";
    public static final String TOMCAT_CURRENT_THREADS = "Tomcat Current threads";
    public static final String MBEAN_NAME = "Tomcat:type=ThreadPool,name=\"http-nio-8080\"";
    public static final String TOMCAT_THREADS_BUSY = "tomcat.threads.busy";
    public static final String TOMCAT_THREADS_CONFIG_MAX = "tomcat.threads.config.max";
    public static final String TOMCAT_THREADS_CURRENT = "tomcat.threads.current";
    public static final String MAX_THREADS_ATTR = "maxThreads";
    @Autowired
    private MeterRegistry repo;


  //  @Autowired
    private Manager tomcatManager;

    public Map<String,Double> getMetrics(){
        Gauge busyThreads = repo.get(TOMCAT_THREADS_BUSY).gauge();
        Gauge allThreads  = repo.get(TOMCAT_THREADS_CONFIG_MAX).gauge();  // yes, could do @Value("${server.tomcat.max-threads:200}") and have it injected
        Gauge currentThread = repo.get(TOMCAT_THREADS_CURRENT).gauge();
        double busyThreadsCount = busyThreads.value();
        double allThreadsCount = allThreads.value();
        double currentThreadCount=currentThread.value();

        Map<String,Double> metricMap=new HashMap();
        metricMap.put(TOMCAT_BUSY_THEADS,busyThreadsCount);
        metricMap.put(TOMCAT_MAX_THREADS,allThreadsCount);
        metricMap.put(TOMCAT_CURRENT_THREADS,currentThreadCount);

        return metricMap;
    }


    public Map setMetrics(Map<String, Double> newMetrics) throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException, MBeanException, InvalidAttributeValueException, AttributeNotFoundException {
        MBeanServer mBeanServer = MonitoringService.getMBeanServer();

   /*     // mBeanServer.getDomains();
        Hashtable table=new Hashtable();
        table.put("type","ThreadPool");
        table.put("name", "http-nio-8080");
        //ObjectName objectName = new ObjectName("Tomcat",table);

       // MBeanInfo mBeanInfo1 = mBeanServer.getMBeanInfo(objectName);*/
        ObjectName objectName = ObjectName.getInstance(MBEAN_NAME);

     //   MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
        Double value = newMetrics.get(MAX_THREADS_ATTR);
        Attribute attribute=new Attribute(MAX_THREADS_ATTR, value.intValue());
        mBeanServer.setAttribute(objectName,attribute);
        return getMetrics();
    }
}

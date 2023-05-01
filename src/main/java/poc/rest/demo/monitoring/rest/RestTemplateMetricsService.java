package poc.rest.demo.monitoring.rest;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import poc.rest.demo.monitoring.MonitoringService;
import poc.rest.demo.monitoring.mbeans.RestTemplateMBean;

import javax.management.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestTemplateMetricsService {


    private static final String REST_TEMPLATE_REQUEST_FACTORY_CLASS = "resTemplateRequestFactoryClass";
    private static final String REQUEST_FACTORY_CLIENT_HTTP_CLASS = "requestFactoryHttpClientClassName";
    private static final String HTTP_CLIENT_CONN_MANAGER = "htpClientConnectionManager";
    private static final String MAX_TOTAL = "maxTotal";
    @Autowired
    @Qualifier("insecureRestTemplate")
    private RestTemplate restTemplate;
    @Autowired
    private MeterRegistry repo;

    public void initialize(){

        try {
            MBeanServer mBeanServer = MonitoringService.getMBeanServer();
            ObjectName objectName = new ObjectName(RestTemplateMBean.REST_TEMPLATE_INSECURE_MBEAN_NAME);
            RestTemplateMBean restTemplateMBean=new RestTemplateMBean();
            restTemplateMBean.addAttribute(RestTemplateMBean.REST_TEMPLATE_INSECURE_MBEAN_NAME,restTemplate);
            mBeanServer.registerMBean(restTemplateMBean, objectName);

        } catch (InstanceAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (MBeanRegistrationException e) {
            throw new RuntimeException(e);
        } catch (NotCompliantMBeanException e) {
            throw new RuntimeException(e);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        } catch (AttributeNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MBeanException e) {
            throw new RuntimeException(e);
        } catch (InvalidAttributeValueException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String,String> getMetrics(){
        Map<String,String> metricMap=new HashMap();
        try {
            Gauge http = repo.get("http.client.requests").gauge();

        }catch(Exception ex){

        }
        ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
        if(requestFactory instanceof InterceptingClientHttpRequestFactory){

        }
        metricMap.put(REST_TEMPLATE_REQUEST_FACTORY_CLASS,requestFactory.getClass().getCanonicalName());
        if (requestFactory instanceof HttpComponentsClientHttpRequestFactory){
            HttpClient httpClient = ((HttpComponentsClientHttpRequestFactory) requestFactory)
                    .getHttpClient();
                    metricMap.put(REQUEST_FACTORY_CLIENT_HTTP_CLASS,httpClient.getClass().getCanonicalName());
            ClientConnectionManager connectionManager = httpClient.getConnectionManager();
                    metricMap.put(HTTP_CLIENT_CONN_MANAGER,connectionManager.getClass().getCanonicalName());
                    if(connectionManager instanceof PoolingHttpClientConnectionManager){
                        PoolingHttpClientConnectionManager poolingConnectionManager = (PoolingHttpClientConnectionManager) connectionManager;
                        int maxTotal = poolingConnectionManager.getMaxTotal();
                        metricMap.put(MAX_TOTAL,String.valueOf(maxTotal));
                    }
        }
        return metricMap;
    }

}

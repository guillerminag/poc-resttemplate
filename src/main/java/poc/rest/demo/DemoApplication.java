package poc.rest.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import poc.rest.demo.monitoring.rest.RestTemplateMetricsService;

@SpringBootApplication
public class DemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);
	@Autowired
	private RestTemplateMetricsService restTemplateMetricsService;
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@EventListener
	public void doAfterStartup(ApplicationReadyEvent event) {
		//LOGGER.info("hello world, I have just started up in {}:", event.getTimeTaken()); <-- version de spring boot 2.7.11
		LOGGER.info("hello world, I have just started up in");
		restTemplateMetricsService.initialize();
	}

}

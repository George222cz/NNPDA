package cz.upce.project;

import cz.upce.project.service.MeasurementServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Measuring devices API", version = "1.0", description = "Devices Information"))
@SecurityScheme(name = "BasicAuth", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class Application {

    private static MeasurementServiceImpl measurementService = null;

    public Application(MeasurementServiceImpl measurementService) {
        Application.measurementService = measurementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    measurementService.generateMeasurement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10000);
    }

}

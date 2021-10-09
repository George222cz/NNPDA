package cz.upce.project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Measuring devices API", version = "1.0", description = "Devices Information"))
@SecurityScheme(name = "BasicAuth", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

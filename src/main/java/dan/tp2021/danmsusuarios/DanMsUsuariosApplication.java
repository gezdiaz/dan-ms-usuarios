package dan.tp2021.danmsusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class DanMsUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanMsUsuariosApplication.class, args);
    }

}

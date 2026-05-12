package co.quind.peajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "co.quind.peajes")
public class PeajesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeajesApplication.class, args);
	}

}

package OnlineBookingSystem.OnlineBookingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "OnlineBookingSystem.OnlineBookingSystem.model")
public class OnlineBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBookingSystemApplication.class, args);
	}

}

package yass.jouao.labx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yass.jouao.labx.Config.RsakeysConfig;

@SpringBootApplication
@EnableConfigurationProperties(RsakeysConfig.class)

public class LabxApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabxApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
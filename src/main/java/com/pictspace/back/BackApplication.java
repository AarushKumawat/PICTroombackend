// package com.pictspace.back;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;

// import com.pictspace.back.config.JwtConfig;

// @SpringBootApplication
// @EnableConfigurationProperties(JwtConfig.class)
// public class BackApplication {
// 	public static void main(String[] args) {
// 		SpringApplication.run(BackApplication.class, args);
// 	}

// }

package com.pictspace.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }
}
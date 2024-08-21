package org.sebastiandev.azureescapehotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Enable caching
public class AzureEscapeHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzureEscapeHotelApplication.class, args);
    }

}

package com.popsa;

import com.popsa.service.PhotoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class PopsaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PopsaApplication.class, args);
    }

    @Bean
    WebClient hereApiWebClient() {
        return WebClient.create("https://revgeocode.search.hereapi.com/v1");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            final PhotoService bean = ctx.getBean(PhotoService.class);
            System.out.println("Please wait while accessing HERE api.");
            System.out.println(bean.getTitle(args[0]));
            System.exit(1);
        };
    }
}

package com.link8.tw;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"com.link8.tw", "com.tgfc.acl.*"})
@EnableJpaRepositories(basePackages = "com.link8.tw.repository")
@EntityScan(basePackages = {"com.link8.tw.model"})
@EnableScheduling
public class MainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }

    @PostConstruct
    void started() {
        // set JVM timezone as UTC
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
    }

    /**
     * socket
     */
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setPort(8889);

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}

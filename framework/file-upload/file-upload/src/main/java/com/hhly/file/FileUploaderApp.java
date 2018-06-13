package com.hhly.file;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.hhly.file.service.UpAndDownLoadService;

/**
 * 启动类
 *
 * @author BSW
 * @create 2017-09-28
 *
 * version 1.0
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class FileUploaderApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FileUploaderApp.class, args);
    }

    @Bean
    CommandLineRunner init(final UpAndDownLoadService storageService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                storageService.init();
            }
        };
    }

}

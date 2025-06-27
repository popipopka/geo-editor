package ru.vistar.geoeditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GeoEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoEditorApplication.class, args);
    }
}

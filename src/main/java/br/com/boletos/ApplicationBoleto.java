package br.com.boletos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class ApplicationBoleto {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoleto.class, args);
    }

    @RequestMapping("/doc")
    public String index() {
        return "index";
    }
}

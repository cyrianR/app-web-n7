package fr.n7.spring_boot_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class HelloWorld {

    @GetMapping("/hello")
    public String hello() {
        return "feur !";
    }
    

}

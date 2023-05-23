package com.skycast;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppResourceController {

    @GetMapping("/")
    public String index() {
        return "Greetings!";
    }


}

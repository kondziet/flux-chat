package pl.kondziet.springbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @GetMapping("/home")
    public String hello() {
        return "Hello reactive world!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "top secret";
    }
}

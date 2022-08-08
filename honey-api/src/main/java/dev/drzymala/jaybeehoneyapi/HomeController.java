package dev.drzymala.jaybeehoneyapi;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("OK");
    }
}

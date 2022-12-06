package de.bencoepp.utils.daemon.controller;

import de.bencoepp.entity.App;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class GeneralController {
    private App app = new App();
    @GetMapping("/")
    public ResponseEntity<String> getMain() throws IOException {
        app.init();
        return ResponseEntity.ok(app.getIp());
    }

    @GetMapping("/health")
    public ResponseEntity<Integer> getHealth() {
        return ResponseEntity.ok(200);
    }
}

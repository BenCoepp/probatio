package de.bencoepp.utils.daemon.controller;

import de.bencoepp.entity.App;
import de.bencoepp.entity.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private App app = new App();

    @GetMapping("/all")
    public ResponseEntity<ArrayList<Project>> getAll() throws IOException {
        app.init();
        return ResponseEntity.ok(app.getProjects());
    }
}

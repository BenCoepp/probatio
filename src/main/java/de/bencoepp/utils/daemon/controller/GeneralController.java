package de.bencoepp.utils.daemon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The type General controller.
 */
@Controller
public class GeneralController {
    private final App app = new App();

    /**
     * Gets main.
     *
     * @return the main
     * @throws IOException the io exception
     */
    @GetMapping("/")
    public ResponseEntity<String> getMain() throws IOException {
        app.init();
        return ResponseEntity.ok(app.getIp());
    }

    /**
     * Gets health.
     *
     * @return the health
     */
    @GetMapping("/health")
    public ResponseEntity<Integer> getHealth() {
        return ResponseEntity.ok(200);
    }

    /**
     * Gets doctor.
     *
     * @return the doctor
     * @throws IOException the io exception
     */
    @GetMapping("/doctor")
    public ResponseEntity<ArrayList<CheckElement>> getDoctor() throws IOException {
        ArrayList<CheckElement> list = new ArrayList<>();
        String homeDir = System.getProperty("user.home");
        File doctor_status = new File(homeDir + "//probatio//doctor_status.json");
        if(doctor_status.exists()){
            String json = Files.readString(Path.of(doctor_status.getAbsolutePath())) ;
            int count = JsonPath.read(json, "$.doctors.length()");
            for (int i = 0; i < count; i++) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.doctors[" + i + "]"));
                CheckElement checkElement = new CheckElement();
                checkElement.fromJson("{\"doctor\":" + jsonObject + "}");
                list.add(checkElement);
            }
        }else{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(list);
            Files.createDirectories(Paths.get(homeDir + "//probatio"));
            try (PrintWriter out = new PrintWriter(doctor_status.getAbsolutePath())) {
                out.println("{\"doctors\":" + json + "}");
            }
        }
        return ResponseEntity.ok(list);
    }
}

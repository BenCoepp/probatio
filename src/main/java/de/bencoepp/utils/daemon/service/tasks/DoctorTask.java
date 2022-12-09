package de.bencoepp.utils.daemon.service.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.bencoepp.entity.CheckElement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static de.bencoepp.utils.DoctorHelper.*;

/**
 * The type Doctor task.
 */
public class DoctorTask extends SchedulerTask{

    @Override
    public void execute() throws IOException {
        ArrayList<CheckElement> list = new ArrayList<>();
        list.add(checkDocker());
        list.add(checkDockerCompose());
        list.add(checkKubectl());
        String homeDir = System.getProperty("user.home");
        File doctor_status = new File(homeDir + "//probatio//doctor_status.json");
        if(doctor_status.exists()){
            doctor_status.delete();
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(list);
        Files.createDirectories(Paths.get(homeDir + "//probatio"));
        try (PrintWriter out = new PrintWriter(doctor_status.getAbsolutePath())) {
            out.println("{\"doctors\":" + json + "}");
        }
    }
}

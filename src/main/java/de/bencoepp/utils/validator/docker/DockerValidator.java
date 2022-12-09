package de.bencoepp.utils.validator.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.utils.validator.docker.entity.DockerCompose;
import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DockerValidator {
    public static ArrayList<CheckElement> validateDockerComposeFile(File dockercomposeFile) throws IOException {
        ArrayList<CheckElement> list = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
            pb.setExtraMessage("DockerCompose...");
            pb.step();
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            pb.step();
            CheckElement syntaxCheck = new CheckElement();
            syntaxCheck.setTitle("Syntax");
            syntaxCheck.setDescription("The syntax check for the compiled docker compose file");
            try {
                DockerCompose dockerCompose = mapper.readValue(dockercomposeFile, DockerCompose.class);
                syntaxCheck.setCheck(true);
            }catch (Exception e){
                syntaxCheck.setCheck(false);
                syntaxCheck.setInfo(e.toString());
            }
            pb.step();
            list.add(syntaxCheck);
        }
        return list;
    }

    public static ArrayList<CheckElement> validateDockerfile(File dockerfile){
        ArrayList<CheckElement> list = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
            pb.setExtraMessage("Get Path...");
            pb.step();
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            pb.step();
            CheckElement syntaxCheck = new CheckElement();
            syntaxCheck.setTitle("Syntax");
            syntaxCheck.setDescription("The syntax check for the compiled Dockerfile");
            try {
                //TODO add validator
                syntaxCheck.setCheck(true);
            }catch (Exception e){
                syntaxCheck.setCheck(false);
                syntaxCheck.setInfo(e.toString());
            }
            pb.step();
            list.add(syntaxCheck);
        }
        return list;
    }
}

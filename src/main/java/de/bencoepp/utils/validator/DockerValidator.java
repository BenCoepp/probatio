package de.bencoepp.utils.validator;

import de.bencoepp.entity.CheckElement;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DockerValidator {
    public static ArrayList<CheckElement> validateDockerComposeFile(File dockercomposeFile) throws IOException {
        ArrayList<CheckElement> list = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
            pb.setExtraMessage("Get Path...");
            pb.step();
            String data = FileUtils.readFileToString(dockercomposeFile, "UTF-8");
            pb.step();

        }
        return list;
    }
}

package de.bencoepp.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.bencoepp.entity.Project;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.*;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "init",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "initialize a new project",
        description = "create a new project file with all necessary setups for further use")
public class InitCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-c", "--check"},
            description = "check project file")
    boolean check;

    @CommandLine.Option(names = {"-f", "--force"},
            description = "check project file")
    boolean force;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");
        File optFile = new File("probatio.json");
        if(!check){
            if(optFile.exists()){
                if(force){
                    optFile.delete();
                    createProjectFile(currentDir);
                }else{
                    System.out.println("There is already a project file in this directory. You can edit this file" +
                            " or run the following command to delete it and create a new one.");
                    System.out.println("\nprobatio init --force");
                    System.out.println("");
                    System.out.println("Otherwiese go ahead and check file syntax by running the command below");
                    System.out.println("\nprobatio init --check");
                }
            }else{
                createProjectFile(currentDir);
            }
        }
        if(check && !force){
            try (ProgressBar pb = new ProgressBar("Analyzing", 9)) {
                pb.setExtraMessage("Scanning...");
                pb.step();
                String json = "";
                BufferedReader reader;
                pb.step();
                try {
                    reader = new BufferedReader(new FileReader("probatio.json"));
                    pb.step();
                    String line = reader.readLine();
                    pb.step();
                    while (line != null) {
                        System.out.println(line);
                        line = reader.readLine();
                        json += reader.readLine();
                    }
                    pb.step();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    pb.step();
                    ObjectMapper mapper = new ObjectMapper();
                    pb.step();
                    Project project = mapper.readValue(json, Project.class);
                    pb.step();
                    System.out.println("Project read successfully you can continue with your work.");
                    pb.step();
                } catch (Exception e) {
                    System.out.println("An error has accused while reading the project, please check the configuration file.");
                }
            }
        }
        if(check && force){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }

    private void createProjectFile(String currentDir) throws JsonProcessingException, FileNotFoundException {
        String projectTitle = currentDir.substring(currentDir.lastIndexOf("\\") + 1 ,currentDir.length());
        Project project = new Project();
        project.setTitle(projectTitle);
        project.setDescription("");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(project);
        try (PrintWriter out = new PrintWriter("probatio.json")) {
            out.println(json);
        }
    }
}

package de.bencoepp.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.bencoepp.entity.App;
import de.bencoepp.entity.DeploymentStep;
import de.bencoepp.entity.IntegrationStep;
import de.bencoepp.entity.Project;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    private App app = new App();

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        String currentDir = System.getProperty("user.dir");
        File optFile = new File("probatio.json");
        if(!check){
            if(optFile.exists()){
                if(force){
                    optFile.delete();
                    createProjectFile(currentDir);
                    System.out.println("Project file was created");
                }else{
                    System.out.println("There is already a project file in this directory. You can edit this file" +
                            " or run the following command to delete it and create a new one.");
                    System.out.println("\nprobatio init --force");
                    System.out.println("");
                    System.out.println("Otherwiese go ahead and check file syntax by running the command below");
                    System.out.println("\nprobatio init --check");
                    String json = Files.readString(Path.of("probatio.json")) ;
                    Project project = new Project();
                    project.fromJson(json);
                    app.addProject(project);
                }
            }else{
                createProjectFile(currentDir);
                System.out.println("Project file was created");
            }
        }
        if(check && !force){
            try (ProgressBar pb = new ProgressBar("Analyzing", 5)) {
                pb.setExtraMessage("Scanning...");
                pb.step();
                String json = Files.readString(Path.of("probatio.json")) ;
                try {
                    pb.step();
                    pb.step();
                    Project project = new Project();
                    project.fromJson(json);
                    pb.step();
                    System.out.println("Project read successfully you can continue with your work.");
                    pb.step();
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("An error has accused while reading the project, please check the configuration file.");
                }
            }
        }
        if(check && force){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }

    private void createProjectFile(String currentDir) throws IOException {
        String projectTitle = currentDir.substring(currentDir.lastIndexOf("\\") + 1 ,currentDir.length());
        Project project = new Project();
        project.setTitle(projectTitle);
        project.setDescription("");
        ArrayList<DeploymentStep> deploymentSteps = new ArrayList<>();
        deploymentSteps.add(new DeploymentStep());
        ArrayList<IntegrationStep> integrationSteps = new ArrayList<>();
        integrationSteps.add(new IntegrationStep());
        project.setDeployment(true);
        project.setIntegration(true);
        project.setIntegrationStepList(integrationSteps);
        project.setDeploymentStepList(deploymentSteps);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(project);
        try (PrintWriter out = new PrintWriter("probatio.json")) {
            out.println("{\"project\":" + json + "}");
        }
        app.addProject(project);
    }
}

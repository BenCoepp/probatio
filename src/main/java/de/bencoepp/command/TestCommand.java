package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Project;
import de.bencoepp.utils.IntegrationHelper;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "test",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "test specific project in a variety of different ways",
        description = "use this command to test your project in a variety of ways")
public class TestCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-s", "--shallow"},
            description = "test project only shallowly")
    boolean shallow;

    @CommandLine.Option(names = {"-d", "--deep"},
            description = "test project as deep as possible")
    boolean deep;

    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "show more output")
    boolean verbose;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private App app = new App();

    @Override
    public Integer call() throws IOException {
        boolean ok = true;
        app.init();
        String currentDir = System.getProperty("user.dir");
        Project project = new Project();
        File optFile = new File("probatio.json");
        if(optFile.exists()){
            try (ProgressBar pb = new ProgressBar("Testing", 5)) {
                pb.setExtraMessage("Loading Project...");
                try{
                    pb.step();
                    String json = Files.readString(Path.of("probatio.json"));
                    pb.step();
                    pb.step();
                    project.fromJson(json);
                    pb.step();
                    System.out.println("Project read successfully you can continue with your work.");
                    pb.step();
                }catch (Exception e){
                    ok = false;
                    System.out.println(e);
                    System.out.println("An error has accused while reading the project, please check the configuration file.");
                }
            }
        }else{
            System.out.println("There is no project file in this directory please make sure to run the following command:");
            System.out.println("    probatio init");
            System.out.println("");
            System.out.println("Make sure to fill the created project file with your necessary information");
        }
        if(shallow && !deep && ok){
            ArrayList<CheckElement> list = IntegrationHelper.executeIntegration(project);
            System.out.println("Results Integration Test:");
            for (CheckElement element : list) {
                element.print(verbose);
            }
        }
        if(deep && !shallow && ok){
            ArrayList<CheckElement> list = IntegrationHelper.executeIntegration(project);
            for (CheckElement element : list) {
                element.print(verbose);
            }
        }
        if(!shallow && !deep){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

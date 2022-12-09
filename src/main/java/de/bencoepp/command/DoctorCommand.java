package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Driver;
import de.bencoepp.utils.CommandHelper;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "doctor",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "check system readiness for use",
        description = "check if the system is ready for the production use of probatio and the nessary tools")
public class DoctorCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "show all information about readiness")
    boolean verbose;

    @CommandLine.Option(names = {"-f", "--fix"},
            description = "fix underlying issues")
    boolean fix;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private final App app = new App();
    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        ArrayList<CheckElement> list = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Analyzing", 4)) {
            pb.setExtraMessage("Check Applications...");
            pb.step();
            list.add(checkDocker());
            pb.step();
            list.add(checkDockerCompose());
            pb.step();
            list.add(checkKubectl());
            pb.step();
        }

        int issues = 0;
        for (CheckElement element : list) {
            if(!element.getCheck()){
                issues++;
            }
        }
        if(verbose && !fix){
            System.out.println("Doctor summary (to see less details, run probatio doctor):");
            for (CheckElement element : list) {
                element.print(verbose);
            }
            printReport(issues);
        }
        if(fix && !verbose){
            ArrayList<Driver> drivers = app.getDrivers();
            for (CheckElement element : list) {
                if(!element.getCheck()){
                    Optional<Driver> driverEl =  drivers.stream().filter(driver -> element.getTitle().equals(driver.getName())).findFirst();
                    driverEl.ifPresent(Driver::printFix);
                }
            }
        }
        if(!verbose && !fix){
            System.out.println("Doctor summary (to see all details, run probatio doctor -v):");
            for (CheckElement element : list) {
                element.print(verbose);
            }
            printReport(issues);
        }
        return ok ? 0 : 1;
    }

    private void printReport(int issues){
        System.out.println("");
        if(issues == 0){
            System.out.println("√ Doctor did not find any issues");
        }else if(issues == 1){
            System.out.println("! Doctor found issues in " + issues + " category");
        }else{
            System.out.println("! Doctor found issues in " + issues + " categorys");
        }
    }
    private CheckElement checkDocker() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker");
        String[] dockerVersion = {"docker", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output);
            String[] dockerINfo = {"docker", "info"};
            String info = CommandHelper.executeCommandWithOutput(dockerINfo);
            checkElement.setInfo(info);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Docker is ether not installed or not running please start the service or install docker");
            checkElement.setInfo(output);
        }
        return checkElement;
    }

    private CheckElement checkDockerCompose() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker Compose");
        String[] dockerVersion = {"docker-compose", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Docker-Compose is ether not installed or not running please start the service or install docker-compose");
            checkElement.setInfo(output);
        }
        return checkElement;
    }

    private CheckElement checkKubectl() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Kubernetes kubectl");
        String[] kubectlVersion = {"kubectl", "version"};
        if(CommandHelper.executeCommand(kubectlVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(kubectlVersion);
            checkElement.setDescription(output);
            String[] kubectlInfo = {"kubectl", "version", "--output=json"};
            String info = CommandHelper.executeCommandWithOutput(kubectlInfo);
            checkElement.setInfo(info);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(kubectlVersion);
            checkElement.setDescription("kubectl is eather not installed or missing please make sure to install it if it nessary for you");
            checkElement.setInfo(output);
        }
        return checkElement;
    }
}

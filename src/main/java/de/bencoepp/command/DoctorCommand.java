package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Driver;
import de.bencoepp.utils.RequestHandler;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

import static de.bencoepp.utils.DoctorHelper.*;

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

    @CommandLine.Option(names = {"-r", "--remote"},
            description = "get doctor info from remote")
    boolean remote;

    @CommandLine.Parameters(description = "remotes you want to run doctor on", arity = "0..1")
    private String[] remotes;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private final App app = new App();
    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        if(!remote && remotes.length == 0){
            ArrayList<CheckElement> list = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Analyzing", 6)) {
                pb.setExtraMessage("Check Applications...");
                pb.step();
                list.add(checkDocker());
                pb.step();
                list.add(checkDockerCompose());
                pb.step();
                list.add(checkKubectl());
                pb.step();
                list.add(checkInternetConnected());
                pb.step();
                list.add(checkPodman());
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
        }else{
            for (String rem : remotes) {
                String json = RequestHandler.get(new URL(""));
                //TODO finish this
            }
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
}

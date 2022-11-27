package de.bencoepp.command;

import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "test",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "test local system in all kinds of ways",
        description = "use this command to test your system in a variety of ways")
public class TestCommand implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "<drivers>", defaultValue = "docker",
            description = "comma seperated list of drivers to test")
    private String[] drivers;
    @CommandLine.Option(names = {"-e", "--everything"},
            description = "test everything")
    boolean everything;

    @CommandLine.Option(names = {"-f", "--functionality"},
            description = "test for functionality")
    boolean functionality;

    @CommandLine.Option(names = {"-a", "--application"},
            description = "test for applications")
    boolean application;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() {
        boolean ok = true;
        int count = 0;
        if(everything){
            count = 1;
        } else if (functionality) {
            count = 2;
        }else if (application){
            count = 3;
        }
        try (ProgressBar pb = new ProgressBar("Testing", count)) {
            pb.setExtraMessage("Checking Drivers...");
            String currentDir = System.getProperty("user.dir");
            if(everything && !functionality && !application){

            }
            if(functionality && !everything && !application){

            }
            if(application && !everything && !functionality){

            }
            if (!everything && !functionality && !application) {
                spec.commandLine().usage(System.err);
            }
        }
        return ok ? 0 : 1;
    }
}

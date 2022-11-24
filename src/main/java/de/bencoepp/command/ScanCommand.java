package de.bencoepp.command;

import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "scan",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "scan a project for a variety of issues",
        description = "analyze your applications and make sure they have what is requiered")
public class ScanCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-a", "--all"}, description = "scan everything")
    boolean all;

    @CommandLine.Option(names = {"-d", "--dockerfile"}, description = "scan everything")
    boolean dockerfile;

    @CommandLine.Option(names = {"-dc", "--dockercompose"}, description = "scan everything")
    boolean dockercompose;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        try (ProgressBar pb = new ProgressBar("Scanning", 9)) {
            pb.setExtraMessage("Checking Dockerfile...");
            pb.step();
            if(all && !dockerfile && !dockercompose){
                pb.step();
                if(checkDockerfile()){
                    //TODO add report here
                }
                pb.step();
            }
        }
        return ok ? 0 : 1;
    }

    private boolean checkDockerfile() throws IOException {
        String[] buildImage = {"docker", "build -t probatio/test ."};
        executeCommand(buildImage,false);
        /*String[] aptUpdate = {"apt-get", "update"};
        executeCommand(aptUpdate,false);
        String[] installScan = {"apt-get", "install docker-scan-plugin"};
        executeCommand(installScan,false);*/
        String[] enableScan = {"docker", "scan --accept-license --version"};
        executeCommand(enableScan,false);
        String[] dockerScan = {"docker", "scan --json --file Dockerfile probatio/test"};
        executeCommand(dockerScan,true);
        return true;
    }

    private void executeCommand(String[] commands, boolean getOutput) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            if(getOutput){
                System.out.println(s);
            }
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
    }
}

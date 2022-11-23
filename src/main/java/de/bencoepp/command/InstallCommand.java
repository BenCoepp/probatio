package de.bencoepp.command;

import de.bencoepp.utils.OsCheck;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "install",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "install nessary dependencys",
        description = "with this command you can install applications like docker and kubernetes directly through this application")
public class InstallCommand implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", arity = "0..1")
    String project;
    @CommandLine.Parameters(hidden = true)  // "hidden": don't show this parameter in usage help message
    List<String> allParameters; // no "index" attribute: captures _all_ arguments

    @CommandLine.Option(names = {"-r", "--required"},
            description = "install only required applications")
    boolean required;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private String os = System.getProperty("os.name");

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        if(required){
            if(os.startsWith("Windows")) {
                OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
                if (ostype.equals(OsCheck.OSType.Windows)) {
                    String home = System.getProperty("user.home");
                    String downloadDir = home + "\\Downloads\\DockerDesktopInstaller.exe";
                    String[] dockerDownload = {"curl", "-o " + downloadDir + " https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe"};
                    executeCommand(dockerDownload);
                } else if (ostype.equals(OsCheck.OSType.Linux)) {
                    String[] baseInstall = {"apt-get", "install ca-certificates curl gnupg lsb-release"};
                    String[] deleteOldDocker = {"apt-get", "remove docker docker-engine docker.io containerd runc"};
                    String[] update = {"apt-get", "update"};
                    String[] keyrings = {"mkdir", "-p /etc/apt/keyrings"};
                    String[] curlKey = {"curl", "-fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg"};
                    String[] setRepo = {"echo", " \"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable\" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null"};
                    String[] installDocker = {"apt-get", "install docker-ce docker-ce-cli containerd.io docker-compose-plugin"};
                    String[] htopInstall = {"apt-get", "install htop"};
                    String[] netToolsInstall = {"apt-get", "install net-tools"};
                }
            }
        }
        return ok ? 0 : 1;
    }

    private boolean executeCommand(String[] commands) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
            return false;
        }
        return true;
    }
}

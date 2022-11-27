package de.bencoepp.command;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "auth",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "authenticate this application instance",
        description = "use this command to authenticate this application in order to use it for production use")
public class AuthCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-f", "--force"},
            description = "force login")
    boolean force;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");
        //TODO add content
        if (!force) {
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

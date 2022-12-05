package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Remote;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "remote",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "manage remote connections",
        description = "use this command to manage and connect to remote instances")
public class RemoteCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-l", "--list"},
            description = "list registered remotes")
    boolean list;

    @CommandLine.Option(names = {"-n", "--new"},
            description = "create a new remote")
    boolean newRemote;

    @CommandLine.Option(names = {"-t", "--test"},
            description = "test all connections to remotes")
    boolean test;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private App app = new App();

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        ArrayList<Remote> remotes = app.getRemotes();
        if(list && !test && !newRemote){
            if(remotes.isEmpty()){
                System.out.println("There are no remotes defined. To create a remote you need to run the command" +
                        "below. With that you are able to create a new remote." +
                        "\n probatio remote --new" +
                        "\nPlease note that this command will also test your remote connection before it will save it.");
            }else{
                System.out.println("List of remotes:\n");
                for (Remote remote : remotes) {
                    remote.print();
                }
            }
        }
        if(newRemote && !list && !test){
            System.out.println("New remote...\nPlease fill in all necessary information:");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            Remote remote = new Remote();
            System.out.println("Name:");
            remote.setName(reader.readLine());
            System.out.println("Description:");
            remote.setDescription(reader.readLine());
            System.out.println("IP:");
            remote.setIp(reader.readLine());
            System.out.println("User:");
            remote.setUser(reader.readLine());
            System.out.println("Password:");
            remote.setPassword(reader.readLine());
            System.out.println("Port:");
            remote.setPort(Integer.valueOf(reader.readLine()));
            if(remote.testConnection()){
                app.addRemote(remote);
            }
        }
        if(test && !list && !newRemote){
            ArrayList<CheckElement> checkElements = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Remote", 4 + remotes.size())) {
                pb.setExtraMessage("Test connection...");
                for (Remote remote : remotes) {
                    pb.step();
                    CheckElement checkElement = new CheckElement();
                    pb.step();
                    checkElement.setTitle(remote.getName());
                    pb.step();
                    checkElement.setDescription(remote.getDescription());
                    pb.step();
                    checkElement.setCheck(remote.testConnection());
                    pb.step();
                    checkElements.add(checkElement);
                }
            }

            for (CheckElement checkEl : checkElements) {
                checkEl.print(false);
            }
        }
        if(!list && !newRemote && !test){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

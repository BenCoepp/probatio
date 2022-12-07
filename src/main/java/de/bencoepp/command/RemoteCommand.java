package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Remote;
import de.bencoepp.utils.validator.StringValidator;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;
import java.io.BufferedReader;
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

    @CommandLine.Option(names = {"-r", "--remove"},
            description = "remove specific remote")
    boolean remove;

    @CommandLine.Parameters(paramLabel = "Remote", description = "one or more remotes", arity = "0..1")
    ArrayList<String> listRemotes;
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
            if(StringValidator.validateTitle(remote.getName())){
                if(remote.testConnection()){
                    app.addRemote(remote);
                    remote.install();
                    remote.setupDaemon();
                }
            }else{
                System.out.println("Please try again some information does not meet the requirements");
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
        if(remove && !list && !newRemote && !test && !(listRemotes == null)){
            for (String remote : listRemotes) {
                remotes.removeIf(element -> element.getName().equals(remote));
            }
            app.setRemotes(remotes);
            System.out.println("Removed remotes from remote list");
        }
        if(!list && !newRemote && !test && !remove){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

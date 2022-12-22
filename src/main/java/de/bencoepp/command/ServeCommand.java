package de.bencoepp.command;

import de.bencoepp.entity.App;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;
import utils.CommandHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "serve",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "serve a daemon or or the wiki from your command line",
        description = "lunch some websites from your command line")
public class ServeCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-w", "--wiki"},
            description = "serve the docs from your command line")
    boolean wiki;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    private final App app = new App();

    @Override
    public Integer call() throws IOException {
        boolean ok = true;
        app.init();
        if(wiki){
            try (ProgressBar pb = new ProgressBar("Starting", 9)) {
                pb.setExtraMessage("Wiki");
                pb.step();
                String[] command = {"docker", "build", "-t","probatio_wiki","."};
                pb.step();
                CommandHelper.executeCommand(command, new File("./docs"));
                pb.step();
                String[] stop = {"docker","stop","probatio_wiki"};
                pb.step();
                CommandHelper.executeCommand(stop, new File("./docs"));
                pb.step();
                String[] remove = {"docker","rm","probatio_wiki"};
                pb.step();
                CommandHelper.executeCommand(remove, new File("./docs"));
                pb.step();
                String[] commandRun = {"docker", "run", "-d","-p","1313:80","--name","probatio_wiki","probatio_wiki"};
                pb.step();
                CommandHelper.executeCommand(commandRun, new File("./docs"));
                pb.step();
            }
            System.out.println("Open the following in your browser of choice to view the wiki:" +
                    "\nhttp://localhost:1313");
        }
        return ok ? 0 : 1;
    }
}

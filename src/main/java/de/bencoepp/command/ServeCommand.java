package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.utils.daemon.Probatioed;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "serve",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "serve daemon of probatio",
        description = "lunch a daemon of probatio and use it in the background")
public class ServeCommand implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    private App app = new App();

    @Override
    public Integer call() throws IOException {
        boolean ok = true;
        app.init();
        Probatioed probatioed = new Probatioed();
        probatioed.main(new String[]{});
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!reader.readLine().equals("exit")) {
            if (reader.readLine().equals("exit")) {
                return ok ? 0 : 1;
            }
        }
        return ok ? 0 : 1;
    }
}

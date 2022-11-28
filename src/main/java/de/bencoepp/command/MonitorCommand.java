package de.bencoepp.command;

import de.bencoepp.utils.CommandHelper;
import org.bouncycastle.eac.EACIOException;
import picocli.CommandLine;

import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "monitor",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "monitor a system and display its information",
        description = "with this command you are able to monitor the system within its parameters")
public class MonitorCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-l", "--local"},
            description = "monitor the local system")
    boolean local;

    @CommandLine.Option(names = {"-r", "--remote"},
            description = "monitor a remote system")
    boolean remote;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private Integer width = 100;
    private Integer height = 480;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");

        if(local && !remote){
            print();
        }
        if(!local && !remote){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }

    private void print() throws IOException, InterruptedException {
        while (true){
            try {
                final String os = System.getProperty("os.name");
                if (os.contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else {
                    new ProcessBuilder("bash", "clear").inheritIO().start().waitFor();
                }
            }
            catch (final Exception e) {
                System.out.println(e);
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getInfoHeader());
            stringBuilder.append(getRuntimes());
            System.out.print(stringBuilder);
            Thread.sleep(5000);
        }
    }

    private String getRuntimes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n╭─ Runtimes ");
        for (int i = 0; i < width - 80; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╮");
        stringBuilder.append("╭─ Performance ");
        for (int i = 0; i < width - 46; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        for (int j = 0; j < 5; j++) {
            String str = "│  Docker v20.152.122 ";

            stringBuilder.append(str);
            for (int i = 0; i < width-str.length() - 67; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("│");
            String str1 = "│   ";

            stringBuilder.append(str1);
            for (int i = 0; i < width-str1.length() - 30; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("│\n");
        }
        stringBuilder.append("╰──");
        for (int i = 0; i < width -71; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╯");
        stringBuilder.append("╰──");
        for (int i = 0; i < width -34; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╯");

        return stringBuilder.toString();
    }

    private String getInfoHeader() throws UnknownHostException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("╭─ System ");
        for (int i = 0; i < width - 7; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        String hostname = InetAddress.getLocalHost().getHostName();
        String ip = InetAddress.getLocalHost().toString();
        String str = "│  Hostname: " + hostname + "   IP: " + ip + "  Status: OK";

        stringBuilder.append(str);
        for (int i = 0; i < width-str.length() + 4; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append("│\n");
        stringBuilder.append("╰──");
        for (int i = 0; i < width; i++) {
            stringBuilder.append("─");
        }
        stringBuilder.append("─╯");

        return stringBuilder.toString();
    }
}

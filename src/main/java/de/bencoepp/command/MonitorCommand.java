package de.bencoepp.command;

import de.bencoepp.entity.App;
import de.bencoepp.utils.CommandHelper;
import de.bencoepp.utils.asciichart.AsciiChart;
import de.bencoepp.utils.asciichart.chart.BarChart;
import de.bencoepp.utils.asciichart.chart.entity.BarElement;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private App app = new App();
    private BarChart runtimePerformace;

    private ArrayList<String> runtimes = new ArrayList<>();
    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");
        app.init();
        try (ProgressBar pb = new ProgressBar("Retrieving", 7)) {
            pb.setExtraMessage("Data...");
            pb.step();
            String[] dockerVersion = {"docker", "--version"};
            pb.step();
            String dockerVersionOut = CommandHelper.executeCommandWithOutput(dockerVersion);
            pb.step();
            runtimes.add(dockerVersionOut.split(",")[0].replace("\n",""));
            pb.step();
            String[] dockerComposeVersion = {"docker-compose", "--version"};
            pb.step();
            String dockerComposeVersionOut = CommandHelper.executeCommandWithOutput(dockerComposeVersion);
            pb.step();
            runtimes.add(dockerComposeVersionOut.replace("version ", "").replace("\n",""));
            pb.step();
        }
        if(local && !remote){
            BarChart barChart = new BarChart();
            ArrayList<BarElement> barElements = new ArrayList<>();
            barElements.add(new BarElement("Docker", "",new BigDecimal("28")));
            barElements.add(new BarElement("Docker Compose", "",new BigDecimal("22")));
            barChart.setElements(barElements);
            runtimePerformace = barChart;
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
                    //Runtime.getRuntime().exec(new String[]{"reset"});
                }
            } catch (final Exception e) {
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
        stringBuilder.append("─".repeat(Math.max(0, width - 80)));
        stringBuilder.append("─╮");
        stringBuilder.append("╭─ Performance ");
        stringBuilder.append("─".repeat(Math.max(0, width - 46)));
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        for (int j = 0; j < runtimes.size(); j++) {
            String str = "│ " + runtimes.get(j);

            stringBuilder.append(str);
            stringBuilder.append(" ".repeat(Math.max(0, width - str.length() - 67)));
            stringBuilder.append("│");
            String str1 = "│" + AsciiChart.getBarChartByLine(runtimePerformace, j);

            stringBuilder.append(str1);
            stringBuilder.append(" ".repeat(Math.max(0, width - str1.length() - 30)));
            stringBuilder.append("│\n");
        }
        stringBuilder.append("╰──");
        stringBuilder.append("─".repeat(Math.max(0, width - 71)));
        stringBuilder.append("─╯");
        stringBuilder.append("╰──");
        stringBuilder.append("─".repeat(Math.max(0, width - 34)));
        stringBuilder.append("─╯");

        return stringBuilder.toString();
    }

    private String getInfoHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("╭─ System ");
        stringBuilder.append("─".repeat(Math.max(0, width - 7)));
        stringBuilder.append("─╮");
        stringBuilder.append("\n");
        String str = "│  Hostname: " + app.getHostname() + "   IP: " + app.getIp() + "  Status: OK";

        stringBuilder.append(str);
        stringBuilder.append(" ".repeat(Math.max(0, width - str.length() + 4)));
        stringBuilder.append("│\n");
        stringBuilder.append("╰──");
        stringBuilder.append("─".repeat(Math.max(0, width)));
        stringBuilder.append("─╯");

        return stringBuilder.toString();
    }
}

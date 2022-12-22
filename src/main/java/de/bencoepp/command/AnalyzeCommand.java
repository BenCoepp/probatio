package de.bencoepp.command;

import de.bencoepp.utils.asciichart.AsciiChart;
import de.bencoepp.utils.asciichart.chart.BarChart;
import de.bencoepp.utils.asciichart.chart.entity.BarElement;
import entity.CheckElement;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.io.FilenameUtils;
import org.barfuin.texttree.api.DefaultNode;
import org.barfuin.texttree.api.TextTree;
import org.barfuin.texttree.api.TreeOptions;
import org.barfuin.texttree.api.style.TreeStyle;
import picocli.CommandLine;
import utils.DirectoryHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
@CommandLine.Command(name = "analyze",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "analyze any container project you have",
        description = "analyze the project by executing the command please choose a visualization method")
public class AnalyzeCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-t", "--tree"},
            description = "visualize project as tree")
    boolean tree;

    @CommandLine.Option(names = {"-c", "--chart"},
            description = "visualize project as charts")
    boolean chart;

    @CommandLine.Parameters(description = "chart you want to run analysis on", arity = "0..1")
    private String chartType;

    @CommandLine.Option(names = {"-s", "--scan"},
            description = "scan docker and docker-compose files")
    boolean scan;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");
        if(tree && !chart){
            List<File> listFiles = new ArrayList<>();
            List<File> listDockerfiles = new ArrayList<>();
            List<File> listDockerComposeFiles = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
                pb.setExtraMessage("Get Path...");
                pb.step();
                pb.setExtraMessage("Gathering files...");
                listFiles = DirectoryHelper.getFilesFromDirectory(currentDir);
                pb.step();
                pb.setExtraMessage("Separating Files...");
                for (File file : listFiles) {
                    if(file.getName().equals("Dockerfile")){
                        listDockerfiles.add(file);
                    }
                    if(file.getName().equals("docker-compose.yaml")){
                        listDockerComposeFiles.add(file);
                    }
                    if(file.getName().equals("docker-compose.yml")){
                        listDockerComposeFiles.add(file);
                    }
                }
                pb.setExtraMessage("Finished...");
            }
            String project = currentDir.substring(currentDir.lastIndexOf("\\") + 1 ,currentDir.length());
            DefaultNode tree = new DefaultNode(project);
            for (ListIterator<File> iter = listFiles.listIterator(); iter.hasNext(); ) {
                File element = iter.next();
                String filePath = element.getCanonicalPath().replace(currentDir + "\\", "");
                if(!filePath.contains("\\")){
                    DefaultNode node = new DefaultNode(element.getName());
                    if(element.getName().equals("Dockerfile")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    if(element.getName().equals("docker-compose.yaml")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    if(element.getName().equals("docker-compose.yml")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    tree.addChild(node);
                    iter.remove();
                }
            }
            TreeOptions options = new TreeOptions();
            options.setStyle(new TreeStyle("├─── ", "│    ", "╰─── ", "<", ">"));
            String rendered = TextTree.newInstance(options).render(tree);
            System.out.println(rendered);
        }
        if(chart && !tree){
            if(chartType == null){
                System.out.println("Please provide a chart type to the chart option in order to print out a corresponding chart.");
                System.out.println("Following chart types are available:");
                System.out.println("    bar <filetype>");
                System.out.println();
                System.out.println("Please execute the following command with your desired type of chart and filter");
                System.out.println("    probatio --chart <chartType> <filter>");
            }
            int width = 100;
            if(chartType.equals("bar")){
                List<File> files = DirectoryHelper.getFilesFromDirectory(currentDir);
                ArrayList<BarElement> results = new ArrayList<>();
                ArrayList<String> list = new ArrayList<>();
                for (File file : files) {
                    list.add(FilenameUtils.getExtension(file.getName()));
                }
                Map<String, Long> counts = list.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
                for (Map.Entry<String, Long> entry : counts.entrySet()) {
                    if(entry.getKey().equals("")){
                        continue;
                    }
                    results.add(new BarElement(entry.getKey(),"", new BigDecimal(entry.getValue())));
                }
                BarChart barChart = new BarChart();
                barChart.setTitle("Filetypes ordered by occurrences");
                barChart.setDescription("A simple bar chart");
                barChart.setElements(results);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("╭─ ").append(barChart.getTitle()).append(" ");
                stringBuilder.append("─".repeat(Math.max(0, width - barChart.getTitle().length() - 1)));
                stringBuilder.append("─╮");
                stringBuilder.append("\n");
                String str1 = "│    " + barChart.getDescription();
                stringBuilder.append(str1);
                stringBuilder.append(" ".repeat(Math.max(0, width - str1.length() + 4)));
                stringBuilder.append("│\n");
                for (int j = 0; j < results.size(); j++) {
                    String str = "│ ";
                    str += AsciiChart.getBarChartByLine(barChart, j);
                    stringBuilder.append(str);
                    stringBuilder.append(" ".repeat(Math.max(0, width - str.length() + 4)));
                    stringBuilder.append("│\n");
                }

                stringBuilder.append("╰──");
                stringBuilder.append("─".repeat(Math.max(0, width)));
                stringBuilder.append("─╯");
                System.out.println(stringBuilder);
            }
            if(chartType.equals("heatmap")){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("╭─ Heatmap ");
                stringBuilder.append("─".repeat(Math.max(0, width - 8)));
                stringBuilder.append("─╮");
                stringBuilder.append("\n");
                for (int j = 0; j < 15; j++) {
                    String str = "│ ";
                    stringBuilder.append(str);
                    stringBuilder.append(" ".repeat(Math.max(0, width - str.length() + 4)));
                    stringBuilder.append("│\n");
                }

                stringBuilder.append("╰──");
                stringBuilder.append("─".repeat(Math.max(0, width)));
                stringBuilder.append("─╯");
                System.out.println(stringBuilder);
            }
        }
        if(scan && !tree && !chart){
            List<File> listFiles = new ArrayList<>();
            List<File> listDockerfiles = new ArrayList<>();
            List<File> listDockerComposeFiles = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
                pb.setExtraMessage("Get Path...");
                pb.step();
                pb.setExtraMessage("Gathering files...");
                listFiles = DirectoryHelper.getFilesFromDirectory(currentDir);
                pb.step();
                pb.setExtraMessage("Separating Files...");
                for (File file : listFiles) {
                    if(file.getName().equals("Dockerfile")){
                        listDockerfiles.add(file);
                    }
                    if(file.getName().equals("docker-compose.yaml")){
                        listDockerComposeFiles.add(file);
                    }
                    if(file.getName().equals("docker-compose.yml")){
                        listDockerComposeFiles.add(file);
                    }
                }
                pb.setExtraMessage("Finished...");
            }
            for (File dockerCompose : listDockerComposeFiles) {

            }
        }
        if(!tree && !chart && !scan){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

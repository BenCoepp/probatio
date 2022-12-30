package de.bencoepp.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Project;
import de.bencoepp.entity.Remote;
import de.bencoepp.utils.RequestHandler;
import de.bencoepp.utils.asciichart.AsciiChart;
import de.bencoepp.utils.asciichart.chart.BarChart;
import de.bencoepp.utils.asciichart.chart.entity.BarElement;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "project",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "interact with projects",
        description = "use this command to interact with projects and manage them on a specific project to project and system to system basis")
public class ProjectCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-r", "--remote"},
            description = "interact with project on a remote machine")
    boolean remote;

    @CommandLine.Parameters(description = "remotes you want to interact with", arity = "0..1")
    private String[] remoteStrings;

    @CommandLine.Option(names = {"-rm", "--remove"},
            description = "remove specific project")
    boolean remove;
    @CommandLine.Option(names = {"-l", "--list"},
            description = "list all projects available")
    boolean list;

    @CommandLine.Option(names = {"-a", "--analyze"},
            description = "analyze projects from all remotes and local in different ways")
    boolean analyze;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private final App app = new App();
    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        ArrayList<Remote> remotes = new ArrayList<>();
        if(!remote){
            //Run local
            Remote remoteLocal = new Remote();
            remoteLocal.setPort(25420);
            remoteLocal.setIp("localhost");
            remoteLocal.setName("localhost");
            remotes.add(remoteLocal);
        } else {
            //run on remotes
            if(remoteStrings == null){
                System.out.println("Please provide one remote you want to interact with");
            }else{
                for (String rem : remoteStrings) {
                    Optional<Remote> remoteOpt = app.getRemotes().stream().filter(remote1 -> remote1.getName().equals(rem)).findAny();
                    if(remoteOpt.isPresent()){
                        remotes.add(remoteOpt.get());
                    }
                }
            }
        }
        if(list && !analyze){
            for (Remote remote : remotes) {
                String json = "{\"project\":";
                json += RequestHandler.get(new URL("http://" + remote.getIp() + ":" + remote.getPort() + "/api/project/all"));
                json += "}";
                int countProjects = JsonPath.read(json, "$.project.length()");
                ArrayList<Project> projects = new ArrayList<>();
                for (int i = 0; i < countProjects; i++) {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.project[" + i + "]"));
                    Project project = new Project();
                    project.fromJson("{\"project\":" + jsonObject + "}");
                    projects.add(project);
                }
                if(projects.isEmpty()){
                    System.out.println("No projects to be displayed you need to create a project before you can continue");
                }
                for (Project project : projects) {
                    project.print();
                }
            }
        }
        if(analyze && !list){
            ArrayList<BarElement> countBarElements = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Gathering", 3)) {
                pb.setExtraMessage("Projects...");
                pb.step();
                Remote remoteLocal = new Remote();
                remoteLocal.setPort(25420);
                remoteLocal.setIp("localhost");
                remoteLocal.setName("localhost");
                pb.step();
                app.addRemote(remoteLocal);
                pb.step();
                for (Remote remote : app.getRemotes()) {
                    String str = RequestHandler.get(new URL("http://" + remote.getIp() + ":" + remote.getPort() + "/api/project/count"));
                    BarElement barElement = new BarElement(remote.getName(), remote.getIp(), new BigDecimal(str));
                    countBarElements.add(barElement);
                }
                BarChart barChart = new BarChart();
                barChart.setTitle("Projects");
                barChart.setDescription("Count of all projects connected");
                barChart.setElements(countBarElements);
                int width = 100;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("╭─ ").append(barChart.getTitle()).append(" ");
                stringBuilder.append("─".repeat(Math.max(0, width - barChart.getTitle().length() - 1)));
                stringBuilder.append("─╮");
                stringBuilder.append("\n");
                String str1 = "│    " + barChart.getDescription();
                stringBuilder.append(str1);
                stringBuilder.append(" ".repeat(Math.max(0, width - str1.length() + 4)));
                stringBuilder.append("│\n");
                for (int j = 0; j < countBarElements.size(); j++) {
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
        }
        if(remove && !analyze && !list){
            System.out.println("Please select the machine where the project is located");
            Remote remoteLocal = new Remote();
            remoteLocal.setPort(25420);
            remoteLocal.setIp("localhost");
            remoteLocal.setName("localhost");
            app.addRemote(remoteLocal);
            for (int i = 0; i < app.getRemotes().size(); i++) {
                System.out.println("[" + i + "] " + app.getRemotes().get(i).getName() + " - " + app.getRemotes().get(i).getIp());
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            int selectedMaschine = Integer.parseInt(reader.readLine());
            String json = "{\"project\":";
            json += RequestHandler.get(new URL("http://" + app.getRemotes().get(selectedMaschine).getIp() + ":" + app.getRemotes().get(selectedMaschine).getPort() + "/api/project/all"));
            json += "}";
            int countProjects = JsonPath.read(json, "$.project.length()");
            ArrayList<Project> projects = new ArrayList<>();
            for (int i = 0; i < countProjects; i++) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.project[" + i + "]"));
                Project project = new Project();
                project.fromJson("{\"project\":" + jsonObject + "}");
                projects.add(project);
            }
            System.out.println("Please select the project to be deleted");
            for (int i = 0; i < projects.size(); i++) {
                System.out.println("[" + i + "] " + projects.get(i).getTitle());
            }
            int selectedProject = Integer.parseInt(reader.readLine());
            String response = RequestHandler.post(new URL("http://" + app.getRemotes().get(selectedMaschine).getIp() + ":" + app.getRemotes().get(selectedMaschine).getPort() + "/api/project/" + projects.get(selectedProject).getTitle() + "/remove"));
            System.out.println("Project successfully removed");
        }
        if(!analyze && !list && !remove){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

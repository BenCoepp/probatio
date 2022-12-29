package de.bencoepp.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.bencoepp.entity.App;
import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.Project;
import de.bencoepp.entity.Remote;
import de.bencoepp.utils.RequestHandler;
import picocli.CommandLine;

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

    @CommandLine.Option(names = {"-l", "--list"},
            description = "list all projects available")
    boolean list;

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
        if(list){
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
                for (Project project : projects) {
                    project.print();
                }
            }
        }
        return ok ? 0 : 1;
    }
}

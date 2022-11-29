package de.bencoepp.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
    private String hostname;
    private String ip;
    private Integer status;
    private Integer appStatus;
    private ArrayList<Project> projects;

    private String currentDir = System.getProperty("user.dir");

    public void init() throws IOException {
        String homeDir = System.getProperty("user.home");
        File probatio_conf = new File(homeDir + "\\probatio\\probatio_conf.json");
        if(probatio_conf.exists()){
            String json = Files.readString(Path.of(probatio_conf.getAbsolutePath())) ;
            fromJson(json);
        }else{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            App app = new App();
            app.setCurrentDir(probatio_conf.getAbsolutePath());
            app.setHostname(InetAddress.getLocalHost().getHostName());
            app.setIp(InetAddress.getLocalHost().toString());

            String json = ow.writeValueAsString(app);
            Files.createDirectories(Paths.get(homeDir + "\\probatio"));
            try (PrintWriter out = new PrintWriter(probatio_conf.getAbsolutePath())) {
                out.println("{\"app\":" + json + "}");
            }
        }
    }

    private void fromJson(String json) throws JsonProcessingException {
        this.hostname = JsonPath.read(json, "$.app.hostname");
        this.ip = JsonPath.read(json, "$.app.id");
        this.appStatus = JsonPath.read(json, "$.app.appStatus");
        this.status = JsonPath.read(json, "$.app.status");
        int countProjects = JsonPath.read(json, "$.app.projects.length()");
        for (int i = 0; i < countProjects; i++) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(JsonPath.read(json,"$.app.projects["+ i + "]"));
            Project project = new Project();
            project.fromJson(jsonObject);
            this.projects.add(project);
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }
}

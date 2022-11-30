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
import java.util.ListIterator;

public class App {
    private String hostname;
    private String ip;
    private Integer status;
    private Integer appStatus;
    private ArrayList<Project> projects;
    private Integer user;
    private String email;
    private String password;
    private ArrayList<String> runtimes;

    private String currentDir = System.getProperty("user.dir");

    public void init() throws IOException {
        String homeDir = System.getProperty("user.home");
        File probatio_conf = new File(homeDir + "//probatio//probatio_conf.json");
        if(probatio_conf.exists()){
            String json = Files.readString(Path.of(probatio_conf.getAbsolutePath())) ;
            fromJson(json);
        }else{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            App app = new App();
            app.setCurrentDir(probatio_conf.getAbsolutePath());
            app.setHostname(InetAddress.getLocalHost().getHostName());
            app.setIp(InetAddress.getLocalHost().toString());
            app.setProjects(new ArrayList<>());
            String json = ow.writeValueAsString(app);
            Files.createDirectories(Paths.get(homeDir + "//probatio"));
            try (PrintWriter out = new PrintWriter(probatio_conf.getAbsolutePath())) {
                out.println("{\"app\":" + json + "}");
            }
            this.projects = app.getProjects();
            this.ip = app.getIp();
            this.status = app.getStatus();
            this.appStatus = app.getAppStatus();
            this.hostname = app.getHostname();
        }
    }

    private void fromJson(String json) throws JsonProcessingException {
        this.hostname = JsonPath.read(json, "$.app.hostname");
        this.ip = JsonPath.read(json, "$.app.ip");
        this.appStatus = JsonPath.read(json, "$.app.appStatus");
        this.status = JsonPath.read(json, "$.app.status");
        int countProjects = JsonPath.read(json, "$.app.projects.length()");
        if(countProjects != 0) {
            for (int i = 0; i < countProjects; i++) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.app.projects[" + i + "]"));
                Project project = new Project();
                project.fromJson("{\"project\":" + jsonObject + "}");
                this.projects = new ArrayList<>();
                this.projects.add(project);
            }
        }
    }

    public void update() throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String homeDir = System.getProperty("user.home");
        File probatio_conf = new File(homeDir + "/probatio/probatio_conf.json");
        String json = ow.writeValueAsString(this);
        if(probatio_conf.exists()){
            Files.delete(Paths.get(homeDir + "/probatio/probatio_conf.json"));
        }
        Files.createDirectories(Paths.get(homeDir + "/probatio"));
        try (PrintWriter out = new PrintWriter(probatio_conf.getAbsolutePath())) {
            out.println("{\"app\":" + json + "}");
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) throws IOException {
        this.hostname = hostname;
        update();
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) throws IOException {
        this.user = user;
        update();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IOException {
        this.email = email;
        update();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws IOException {
        this.password = password;
        update();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) throws IOException {
        this.ip = ip;
        update();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) throws IOException {
        this.status = status;
        update();
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) throws IOException {
        this.appStatus = appStatus;
        update();
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) throws IOException {
        this.projects = projects;
        update();
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) throws IOException {
        this.currentDir = currentDir;
        update();
    }

    public void addProject(Project project) throws IOException {
        if(projects == null){
            this.projects = new ArrayList<>();
        }
        for (ListIterator<Project> iter = projects.listIterator(); iter.hasNext(); ) {
            Project element = iter.next();
            if(element.getRoot() == project.getRoot()){
                iter.remove();
            }
        }
        this.projects.add(project);
        update();
    }

    public ArrayList<String> getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(ArrayList<String> runtimes) throws IOException {
        this.runtimes = runtimes;
        update();
    }

    public void addRuntime(String runtime) throws IOException {
        if(runtimes == null){
            this.runtimes = new ArrayList<>();
        }
        for (ListIterator<String> iter = runtimes.listIterator(); iter.hasNext(); ) {
            String element = iter.next();
            if(element.equals(runtime)){
                iter.remove();
            }
        }
        this.runtimes.add(runtime);
        update();
    }
}

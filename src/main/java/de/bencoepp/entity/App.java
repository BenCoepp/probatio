package de.bencoepp.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import de.bencoepp.utils.firebase.Firebase;

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
    private ArrayList<Project> projects = new ArrayList<>();
    private Integer user;
    private String email;
    private String password;
    private ArrayList<Driver> drivers;
    private ArrayList<Remote> remotes = new ArrayList<>();

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
        this.drivers = initDrivers();
        Firebase firebase = new Firebase("AIzaSyDW4EOc-6nLGGLUEaJ3c9yxSlw1aQcqrWc");
        firebase.init();
    }

    private ArrayList<Driver> initDrivers() {
        ArrayList<Driver> driverArrayList = new ArrayList<>();
        Driver docker = new Driver();
        docker.setName("Docker");
        docker.setDescription("This driver handles and is linked to the docker engine");
        docker.setInstallInstructions("""
                To install docker follow the link below and search for the necessary install instructions for your operating system.
                 https://docs.docker.com/get-docker/
                 Once you have downloaded and followed the operating system specific install  instructions you should run the command below to test if the install was successful.\s
                The provided install commands should only be used on a ubuntu system.""");
        ArrayList<String[]> dockerInstallCommand = new ArrayList<>();
        dockerInstallCommand.add(new String[]{"apt-get","remove","docker","docker-engine","docker.io","containerd","runc"});
        dockerInstallCommand.add(new String[]{"apt-get","update"});
        dockerInstallCommand.add(new String[]{"apt-get","install","ca-certificates","curl","gnupg","lsb-release"});
        dockerInstallCommand.add(new String[]{"mkdir","-p","/etc/apt/keyrings"});
        dockerInstallCommand.add(new String[]{"curl","-fsSL","https://download.docker.com/linux/ubuntu/gpg","|","gpg","--dearmor","-o","/etc/apt/keyrings/docker.gpg"});
        dockerInstallCommand.add(new String[]{"apt-get","update"});
        dockerInstallCommand.add(new String[]{"apt-get","install","docker-ce","docker-ce-cli","containerd.io","docker-compose-plugin"});
        docker.setInstallCommands(dockerInstallCommand);
        docker.setFixInstructions("""
                To fix docker there are a few methods available depending on why docker is currently not working. The following list may help you narrow the problem down.\s
                \s
                - restart or start the docker engine\s
                - run sudo systemctl docker start\s
                - restart the maschine\s
                - check if you have docker installed\s
                \s
                If you have tried everything, or the problem has resolved itself then please rerun the command below to make sure everything is working in order.
                 probatio doctor""");
        driverArrayList.add(docker);
        Driver dockercompose = new Driver();
        dockercompose.setName("Docker Compose");
        dockercompose.setDescription("This driver links to Docker Compose, but only if Docker Compose is installed as its own application.");
        dockercompose.setInstallInstructions("To install Docker Compose you can just install Docker. In most cases Docker comes with Docker Compose" +
                " or it is a direct Docker plugin. Still make sure it is running by simply running the doctor again and it should at some point turn to true.");
        ArrayList<String[]> dockercomposeInstallCommand = new ArrayList<>();
        dockercomposeInstallCommand.add(new String[]{"apt-get","install","docker-compose-plugin"});
        dockercompose.setInstallCommands(dockercomposeInstallCommand);
        dockercompose.setFixInstructions("""
                To fix Docker Compose there are only two real ways.\s
                - Installing Docker Compose
                - Using docker compose instead of docker-compose

                After that run the doctor again and test if the changes applied.""");
        driverArrayList.add(dockercompose);
        Driver kubectl = new Driver();
        kubectl.setName("Kubernetes kubectl\n");
        kubectl.setDescription("This driver links to kubectl");
        kubectl.setFixInstructions("""

                If kubectl is not available there are two possible reasons for this.
                - kubectl is not installed
                - the installation is broken

                after that please run the doctor again and see if the changes have applied.""");
        driverArrayList.add(kubectl);
        return driverArrayList;
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
        int countRemotes = JsonPath.read(json, "$.app.remotes.length()");
        if(countRemotes != 0){
            for (int i = 0; i < countRemotes; i++) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.app.remotes[" + i + "]"));
                Remote remote = new Remote();
                remote.fromJson("{\"remote\":" + jsonObject + "}");
                this.remotes.add(remote);
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
        projects.removeIf(element -> element.getRoot() == project.getRoot());
        this.projects.add(project);
        update();
    }

    public void addDriver(Driver driver) throws IOException {
        if(drivers == null){
            this.drivers = new ArrayList<>();
        }
        drivers.removeIf(element -> element.getName().equals(driver.getName()));
        this.drivers.add(driver);
        update();
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(ArrayList<Driver> drivers) throws IOException {
        this.drivers = drivers;
        update();
    }

    public ArrayList<Remote> getRemotes() {
        return remotes;
    }

    public void setRemotes(ArrayList<Remote> remotes) {
        this.remotes = remotes;
    }

    public void addRemote(Remote remote) throws IOException {
        if(remotes == null){
            this.remotes = new ArrayList<>();
        }
        remotes.removeIf(element -> element.getName().equals(remote.getName()));
        this.remotes.add(remote);
        update();
    }
}

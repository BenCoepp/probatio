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
    private ArrayList<Driver> drivers;

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
    }

    private ArrayList<Driver> initDrivers() {
        ArrayList<Driver> driverArrayList = new ArrayList<>();
        Driver docker = new Driver();
        docker.setName("Docker");
        docker.setDescription("This driver handles and is linked to the docker engine");
        docker.setInstallInstructions("To install docker follow the link below and search for" +
                " the necessary install instructions for your operating system.\n" +
                " https://docs.docker.com/get-docker/\n" +
                " Once you have downloaded and followed the operating system specific install " +
                " instructions you should run the command below to test if the install was successful." +
                " \nThe provided install commands should only be used on a ubuntu system.");
        ArrayList<String[]> dockerInstallCommand = new ArrayList<>();
        dockerInstallCommand.add(new String[]{"apt-get","remove","docker","docker-engine","docker.io","containerd","runc"});
        dockerInstallCommand.add(new String[]{"apt-get","update"});
        dockerInstallCommand.add(new String[]{"apt-get","install","ca-certificates","curl","gnupg","lsb-release"});
        dockerInstallCommand.add(new String[]{"mkdir","-p","/etc/apt/keyrings"});
        dockerInstallCommand.add(new String[]{"curl","-fsSL","https://download.docker.com/linux/ubuntu/gpg","|","gpg","--dearmor","-o","/etc/apt/keyrings/docker.gpg"});
        dockerInstallCommand.add(new String[]{"apt-get","update"});
        dockerInstallCommand.add(new String[]{"apt-get","install","docker-ce","docker-ce-cli","containerd.io","docker-compose-plugin"});
        docker.setInstallCommands(dockerInstallCommand);
        docker.setFixInstructions("To fix docker there are a few methods available depending on why" +
                " docker is currently not working. The following list may help you narrow the problem down." +
                " \n" +
                " \n- restart or start the docker engine" +
                " \n- run sudo systemctl docker start" +
                " \n- restart the maschine" +
                " \n- check if you have docker installed" +
                " \n" +
                " \nIf you have tried everything, or the problem has resolved itself then please" +
                " rerun the command below to make sure everything is working in order." +
                "\n probatio doctor");
        driverArrayList.add(docker);
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

    public void addDriver(Driver driver) throws IOException {
        if(drivers == null){
            this.drivers = new ArrayList<>();
        }
        for (ListIterator<Driver> iter = drivers.listIterator(); iter.hasNext(); ) {
            Driver element = iter.next();
            if(element.getName().equals(driver.getName())){
                iter.remove();
            }
        }
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
}

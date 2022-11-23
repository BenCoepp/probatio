package de.bencoepp.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bencoepp.entity.Project;
import de.bencoepp.utils.DbUtils;
import de.vandermeer.asciitable.AsciiTable;
import picocli.CommandLine;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "project",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "controll and manage projects",
        description = "manage and controll projects for the use in this application" +
                "as well as report project use")
public class ProjectCommand implements Callable<Integer> {

    @CommandLine.Parameters(hidden = true)  // "hidden": don't show this parameter in usage help message
    List<String> allParameters; // no "index" attribute: captures _all_ arguments
    @CommandLine.Option(names = {"-a", "--all"},
            description = "list all projects")
    boolean all;

    @CommandLine.Option(names = {"-n", "--new"},
            description = "create new project")
    boolean newProject;

    @CommandLine.Option(names = {"-s", "--scan"},
            description = "create new project")
    boolean scan;

    private Connection con;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        DbUtils dbUtils = new DbUtils();
        con = dbUtils.getCon();
        if(all){
            if(newProject){
                spec.commandLine().usage(System.err);
            }
            ArrayList<Project> projects = new ArrayList<>();
            String sqlProjects = "SELECT * FROM project";
            try(PreparedStatement preparedStatement = con.prepareStatement(sqlProjects)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while (resultSet.next()){
                        Project project = new Project();
                        project.setId(resultSet.getInt("id"));
                        project.setName(resultSet.getString("name"));
                        projects.add(project);
                    }
                }
            }
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("ID","Name");
            for (Project project : projects) {
                at.addRule();
                at.addRow(project.getId(), project.getName());
            }
            at.addRule();
            String rend = at.render();
            System.out.println(rend);
        }
        if(newProject){
            if(all){
                spec.commandLine().usage(System.err);
            }
            System.out.println("Creating new project, please fill out the information below");
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                Project project = new Project();
                System.out.println("Enter path to project:");
                project.setPath(reader.readLine());
                File optConfig = new File(project.getPath() + "//probatio.json");
                if(optConfig.exists()){
                    //TODO read project from path
                }else{
                    System.out.println("Enter project name:");
                    project.setName(reader.readLine());
                    System.out.println("Enter project type:");
                    System.out.println("    (" + Project.TYPE_DOCKER + ") Docker");
                    System.out.println("    (" + Project.TYPE_PODMAN + ") Podman");
                    System.out.println("    (" + Project.TYPE_REDHEAD + ") RedHead");
                    project.setType(Integer.valueOf(reader.readLine()));
                    if(project.getType() == Project.TYPE_DOCKER){
                        System.out.println("Path to Dockerfile:");
                        project.setPathToDockerfile(reader.readLine());
                    }
                }
                try(PreparedStatement preparedStatement = con.prepareStatement(project.getPreparedInsert())){
                    preparedStatement.setString(1, project.getName());
                    preparedStatement.setString(2, project.getPath());
                    preparedStatement.setInt(3, project.getType());
                    preparedStatement.setString(4, project.getPathToDockerfile());
                    preparedStatement.execute();
                }
                ObjectMapper mapper = new ObjectMapper();
                if(!optConfig.exists()){
                    if(!optConfig.mkdirs()){
                        System.out.println("An error has accrued please try again");
                    }
                }
                FileWriter fileWriter = new FileWriter(optConfig);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(mapper.writeValueAsString(project));
                writer.close();
            }
        }
        if(scan){
            if (newProject || all){
                spec.commandLine().usage(System.err);
            }
            String project = allParameters.get(0);
            //TODO check if project exists
                //TODO if not then give error
            //TODO get project from db
            //TODO do scan
                //TODO check for Dockerfile
                    //TODO if yes then build image
                    //TODO then scan image
                //TODO check for docker-compose.yaml file
                    //TODO run up -d
                    //TODO run down -v
            //TODO create report and print it to disk and db
        }
        if(!all && !newProject && !scan){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

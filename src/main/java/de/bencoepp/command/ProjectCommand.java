package de.bencoepp.command;

import de.bencoepp.utils.DbUtils;
import picocli.CommandLine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    @CommandLine.Option(names = {"-a", "--all"},
            description = "list all projects")
    boolean all;

    private Connection con;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        DbUtils dbUtils = new DbUtils();
        con = dbUtils.getCon();
        if(all){
            String sqlProjects = "";
            try(PreparedStatement preparedStatement = con.prepareStatement(sqlProjects)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while (resultSet.next()){

                    }
                }
            }
        }
        if(!all){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}

package de.bencoepp.command;

import de.bencoepp.utils.DbUtils;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "init",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "Initilize the application for use",
        description = "When executing this command a SQLLight DB will be created as well as" +
                "a few more files in order to start working")
public
class InitCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-d", "--dry"},
            description = "test the command without commiting any permanant changes")
    boolean dry;


    private Connection con;
    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        try (ProgressBar pb = new ProgressBar("Init", 9)) {
            //TODO check if there was already a init
            String userDir = System.getProperty("user.dir");
            pb.step();
            File optFile = new File(userDir + "//sql.db");
            pb.step();
            if(optFile.exists()){
                //TODO check db
            }else{
                //TODO create sql.db file in directory
                optFile.createNewFile();
                DbUtils dbUtils = new DbUtils();
                con = dbUtils.getCon();
                String sqlCreateProject = "CREATE TABLE project (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  name varchar(255)," +
                        "  path varchar(255)," +
                        "  type INTEGER," +
                        "  pathToDockerfile varchar(255)" +
                        ")";
                Statement statement = con.createStatement();
                statement.execute(sqlCreateProject);
            }
            //check for docker
            Runtime rt = Runtime.getRuntime();
            pb.step();
            String[] commands = {"docker", "--version"};
            pb.step();
            Process proc = rt.exec(commands);
            pb.step();
            try( BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()))){
                try(BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(proc.getErrorStream()))){
                    // Read the output from the command
                    String s = null;
                    while ((s = stdInput.readLine()) != null) {
                        //TODO check vesion of docker and advice updating it
                    }
                    // Read any errors from the attempted command
                    while ((s = stdError.readLine()) != null) {
                        //TODO tell user to install or start docker
                        ok = false;
                    }
                }
            }
            //TODO check for docker compose
            pb.step();
            String[] commands1 = {"docker-compose", "--version"};
            pb.step();
            Process proc1 = rt.exec(commands1);
            pb.step();
            try( BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc1.getInputStream()))){
                try(BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(proc1.getErrorStream()))){
                    // Read the output from the command
                    String s = null;
                    while ((s = stdInput.readLine()) != null) {
                        //TODO check vesion of docker compose and advice updating it
                    }
                    // Read any errors from the attempted command
                    while ((s = stdError.readLine()) != null) {
                        String[] commands2 = {"docker compose", "--version"};
                        Process proc2 = rt.exec(commands2);
                        try( BufferedReader stdInput1 = new BufferedReader(new
                                InputStreamReader(proc2.getInputStream()))){
                            try(BufferedReader stdError1 = new BufferedReader(new
                                    InputStreamReader(proc2.getErrorStream()))){
                                // Read the output from the command
                                String s1 = null;
                                while ((s1 = stdInput1.readLine()) != null) {
                                    //TODO check vesion of docker and advice updating it
                                }
                                // Read any errors from the attempted command
                                while ((s1 = stdError1.readLine()) != null) {
                                    //TODO tell user to install or start docker compose
                                    ok = false;
                                }
                            }
                        }
                    }
                }
            }
            pb.step();
            //TODO check platofrm
            pb.setExtraMessage("Executing...");
        }
        return ok ? 0 : 1;
    }


}

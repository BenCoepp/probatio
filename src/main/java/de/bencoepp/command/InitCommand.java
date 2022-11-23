package de.bencoepp.command;

import de.bencoepp.utils.DbUtils;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.File;
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
        try (ProgressBar pb = new ProgressBar("Test", 6)) {
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
                pb.step();
                DbUtils dbUtils = new DbUtils();
                con = dbUtils.getCon();
                String sqlCreateProject = "CREATE TABLE project (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  name varchar(255)" +
                        ")";
                pb.step();
                Statement statement = con.createStatement();
                pb.step();
                statement.execute(sqlCreateProject);
                pb.step();
            }
            //TODO check for docker
            //TODO check for docker compose
            //TODO check platofrm
            pb.setExtraMessage("Executing...");
        }
        return ok ? 0 : 1;
    }


}

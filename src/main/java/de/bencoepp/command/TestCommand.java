package de.bencoepp.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.bencoepp.entity.App;
import de.bencoepp.entity.Project;
import de.bencoepp.entity.Remote;
import de.bencoepp.entity.test.StepResult;
import de.bencoepp.entity.test.TestInfo;
import de.bencoepp.entity.test.TestResult;
import de.bencoepp.utils.RequestHandler;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "test",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "test projects and systems",
        description = "test projects and systems in a variety of ways")
public class TestCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "make output verbose")
    boolean verbose;

    @CommandLine.Option(names = {"-f", "--file"},
            description = "output a txt file which includes the test results")
    boolean file;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private final App app = new App();

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        app.init();
        File optFile = new File("probatio.json");
        if(optFile.exists()){
            System.out.println("You are currently in a project directory.\nDo you want to test the project or run tests " +
                    "on other machines");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            System.out.println("Select Y/N if you want to test the project");
            String selectStr = reader.readLine();
            if(selectStr.equals("Y")){
                System.out.println("The project tests will now be executed");
                //TODO run project tests
            }else if (selectStr.equals("N")){
                optionSelecting();
            }
        }else{
            optionSelecting();
        }
        return ok ? 0 : 1;
    }

    private void optionSelecting() throws IOException {
        ArrayList<Remote> remotes = new ArrayList<>();
        int depth = 0;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        Remote remoteLocal = new Remote();
        remoteLocal.setPort(25420);
        remoteLocal.setIp("localhost");
        remoteLocal.setName("localhost");
        app.addRemote(remoteLocal);
        for (int i = 0; i < app.getRemotes().size(); i++) {
            System.out.println("[" + (i + 1) + "] " + app.getRemotes().get(i).getName() + " " + app.getRemotes().get(i).getIp());
        }
        System.out.println("Write machines to run tests on as a comma seperated list below [1,2,5]");
        String[] machines = reader.readLine().replace(" ", "").split(",");
        if(machines == null){
            System.out.println("Rerun the command you need to provide at least one machine.");
        }else{
            for (String machine : machines) {
                remotes.add(app.getRemotes().get(Integer.parseInt(machine)-1));
            }
            System.out.println("Select the depth of tests you want to run");
            System.out.println("[1] Base Test");
            System.out.println("[2] Simple Test");
            System.out.println("[3] Full Test (Takes the longest time)");
            depth = Integer.parseInt(reader.readLine());
            //TODO get from each maschine all tests for that depth and compare them, if the versions do not match advice for a update
            ArrayList<TestInfo> testInfos = new ArrayList<>();
            for (Remote remote : remotes) {
                String json = "{\"info\":";
                json += RequestHandler.get(new URL("http://" + remote.getIp() + ":" + remote.getPort() + "/api/test/" + depth + "/all/info"));
                json += "}";
                int countInfos = JsonPath.read(json, "$.info.length()");
                for (int i = 0; i < countInfos; i++) {
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.info[" + i + "]"));
                    TestInfo testInfo = new TestInfo();
                    testInfo.fromJson("{\"info\":" + jsonObject + "}");
                    if(!testInfos.contains(testInfo)){
                        testInfos.add(testInfo);
                    }
                }
            }
            System.out.println("Below you can see the test that will be run on your selected machines.");
            for (int i = 0; i < testInfos.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + testInfos.get(i).getTitle() + " Version: " + testInfos.get(i).getVersion());
            }
            System.out.println();
            System.out.println("Test will now be executed this may take a while...");
            ArrayList<TestResult> testResults = new ArrayList<>();
            try (ProgressBar pb = new ProgressBar("Testing", remotes.size())) {
                for (Remote remote : remotes) {
                    pb.setExtraMessage(remote.getName());
                    String json = "{\"result\":";
                    json += RequestHandler.get(new URL("http://" + remote.getIp() + ":" + remote.getPort() + "/api/test/" + depth + "/all/execute"));
                    json += "}";
                    int countResult = JsonPath.read(json, "$.result.length()");
                    for (int i = 0; i < countResult; i++) {
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonObject = mapper.writeValueAsString(JsonPath.read(json, "$.result[" + i + "]"));
                        TestResult testResult = new TestResult();
                        testResult.fromJson("{\"result\":" + jsonObject + "}");
                        testResults.add(testResult);
                    }
                    pb.step();
                }
            }
            for (TestResult testResult : testResults) {
                if(verbose){
                    if(testResult.getSuccessful()){
                        System.out.println("[√] " + testResult.getTitle());
                    }else {
                        System.out.println("[!] " + testResult.getTitle());
                    }
                    System.out.println("    " + testResult.getDescription());
                    for (StepResult stepResult : testResult.getStepResults()) {
                        if(stepResult.getSuccessful()){
                            System.out.println("    [√] " + stepResult.getTitle());
                        }else {
                            System.out.println("    [!] " + stepResult.getTitle());
                        }
                    }
                } else {
                    if(testResult.getSuccessful()){
                        System.out.println("[√] " + testResult.getTitle());
                    }else {
                        System.out.println("[!] " + testResult.getTitle());
                    }
                    System.out.println("    " + testResult.getDescription());
                    for (StepResult stepResult : testResult.getStepResults()) {
                        if(stepResult.getSuccessful()){
                            System.out.println("    [√] " + stepResult.getTitle());
                        }else {
                            System.out.println("    [!] " + stepResult.getTitle());
                        }
                    }
                }
            }
        }
    }
}

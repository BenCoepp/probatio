package de.bencoepp.utils.daemon.service.tasks;

import de.bencoepp.entity.CheckElement;
import de.bencoepp.utils.CommandHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DoctorTask extends SchedulerTask{

    @Override
    public void execute() throws IOException {
        ArrayList<CheckElement> list = new ArrayList<>();
        list.add(checkDocker());
        list.add(checkDockerCompose());
        list.add(checkKubectl());
        System.out.println("list le" + list.size());
    }

    private CheckElement checkDocker() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker");
        String[] dockerVersion = {"docker", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output);
            String[] dockerINfo = {"docker", "info"};
            String info = CommandHelper.executeCommandWithOutput(dockerINfo);
            checkElement.setInfo(info);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Docker is ether not installed or not running please start the service or install docker");
            checkElement.setInfo(output);
        }
        return checkElement;
    }

    private CheckElement checkDockerCompose() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker Compose");
        String[] dockerVersion = {"docker-compose", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Docker-Compose is ether not installed or not running please start the service or install docker-compose");
            checkElement.setInfo(output);
        }
        return checkElement;
    }

    private CheckElement checkKubectl() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Kubernetes kubectl");
        String[] kubectlVersion = {"kubectl", "version"};
        if(CommandHelper.executeCommand(kubectlVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(kubectlVersion);
            checkElement.setDescription(output);
            String[] kubectlInfo = {"kubectl", "version", "--output=json"};
            String info = CommandHelper.executeCommandWithOutput(kubectlInfo);
            checkElement.setInfo(info);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(kubectlVersion);
            checkElement.setDescription("kubectl is eather not installed or missing please make sure to install it if it nessary for you");
            checkElement.setInfo(output);
        }
        return checkElement;
    }
}

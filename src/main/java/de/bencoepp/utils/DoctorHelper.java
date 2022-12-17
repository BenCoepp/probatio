package de.bencoepp.utils;

import de.bencoepp.entity.CheckElement;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DoctorHelper {
    public static CheckElement checkDocker() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker");
        String[] dockerVersion = {"docker", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output.replace("\n",""));
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

    public static CheckElement checkDockerCompose() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Docker Compose");
        String[] dockerVersion = {"docker-compose", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output.replace("\n",""));
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Docker-Compose is ether not installed or not running please start the service or install docker-compose");
            checkElement.setInfo(output);
        }
        return checkElement;
    }

    public static CheckElement checkKubectl() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Kubernetes kubectl");
        String[] kubectlVersion = {"kubectl", "version"};
        if(CommandHelper.executeCommand(kubectlVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(kubectlVersion);
            checkElement.setDescription(output.replace("\n",""));
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

    public static CheckElement checkInternetConnected() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Internet Access");
        checkElement.setCheck(netIsAvailable());
        if (checkElement.getCheck()){
            checkElement.setDescription("The open internet was reachable");
        }else{
            checkElement.setDescription("Not possible to connect to the internet, please make sure to connect if you require internet access");
        }
        return checkElement;
    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static CheckElement checkPodman() throws IOException {
        CheckElement checkElement = new CheckElement();
        checkElement.setTitle("Podman");
        String[] dockerVersion = {"podman", "--version"};
        if(CommandHelper.executeCommand(dockerVersion,new File(System.getProperty("user.dir")))){
            checkElement.setCheck(true);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription(output.replace("\n",""));
            String[] dockerINfo = {"podman", "info"};
            String info = CommandHelper.executeCommandWithOutput(dockerINfo);
            checkElement.setInfo(info);
        }else{
            checkElement.setCheck(false);
            String output = CommandHelper.executeCommandWithOutput(dockerVersion);
            checkElement.setDescription("Podman is ether not installed or not running please start the service or install podman");
            checkElement.setInfo(output);
        }
        return checkElement;
    }
}

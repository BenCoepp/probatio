package de.bencoepp.utils;

import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.DeploymentStep;
import de.bencoepp.entity.IntegrationStep;
import de.bencoepp.entity.Project;
import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

public class DeploymentHelper {

    public static ArrayList<CheckElement> executeDeployment(Project project) throws IOException {
        ArrayList<CheckElement> results = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Deployment", project.getDeploymentStepList().size() * 9)) {
            pb.setExtraMessage("Executing...");
            for (DeploymentStep deploymentStep : project.getDeploymentStepList()) {
                if(deploymentStep.getCommand() != null){
                    results.add(executeDeploymentStep(deploymentStep,pb));
                }
            }
        }
        return results;
    }

    private static CheckElement executeDeploymentStep(DeploymentStep deploymentStep, ProgressBar pb) throws IOException {
        CheckElement checkElement = new CheckElement();
        pb.step();
        checkElement.setTitle(deploymentStep.getTitle());
        pb.step();
        checkElement.setDescription(deploymentStep.getDescription());
        pb.step();
        String[] commands = deploymentStep.getCommand().split(" ");
        pb.step();
        if(commands.length > 0){
            pb.step();
            String output = CommandHelper.executeCommandWithOutput(commands);
            pb.step();
            checkElement.setInfo(output);
            pb.step();
            checkElement.setCheck(true);
            pb.step();
        }else{
            checkElement.setCheck(false);
        }
        pb.step();
        return checkElement;
    }
}

package de.bencoepp.utils;

import de.bencoepp.entity.CheckElement;
import de.bencoepp.entity.IntegrationStep;
import de.bencoepp.entity.Project;
import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

public class IntegrationHelper {

    public static ArrayList<CheckElement> executeIntegration(Project project) throws IOException {
        ArrayList<CheckElement> results = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Integration", project.getIntegrationStepList().size() * 9)) {
            pb.setExtraMessage("Executing...");
            for (IntegrationStep integrationStep : project.getIntegrationStepList()) {
                results.add(executeIntegrationStep(integrationStep,pb));
            }
        }
        return results;
    }

    private static CheckElement executeIntegrationStep(IntegrationStep integrationStep, ProgressBar pb) throws IOException {
        CheckElement checkElement = new CheckElement();
        pb.step();
        checkElement.setTitle(integrationStep.getTitle());
        pb.step();
        checkElement.setDescription(integrationStep.getDescription());
        pb.step();
        String[] commands = integrationStep.getCommand().split(" ");
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

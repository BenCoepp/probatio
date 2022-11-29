package de.bencoepp.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
public class Project {
    private String title;
    private String description;
    private Boolean deployment;
    private Boolean integration;
    private ArrayList<DeploymentStep> deploymentStepList = new ArrayList<>();
    private ArrayList<IntegrationStep> integrationStepList = new ArrayList<>();
    private String root;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeployment() {
        return deployment;
    }

    public void setDeployment(Boolean deployment) {
        this.deployment = deployment;
    }

    public Boolean getIntegration() {
        return integration;
    }

    public void setIntegration(Boolean integration) {
        this.integration = integration;
    }

    public ArrayList<DeploymentStep> getDeploymentStepList() {
        return deploymentStepList;
    }

    public void setDeploymentStepList(ArrayList<DeploymentStep> deploymentStepList) {
        this.deploymentStepList = deploymentStepList;
    }

    public ArrayList<IntegrationStep> getIntegrationStepList() {
        return integrationStepList;
    }

    public void setIntegrationStepList(ArrayList<IntegrationStep> integrationStepList) {
        this.integrationStepList = integrationStepList;
    }

    public void fromJson(String json) throws JsonProcessingException {
        this.title = JsonPath.read(json, "$.project.title");
        this.description = JsonPath.read(json, "$.project.description");
        this.deployment = JsonPath.read(json, "$.project.deployment");
        this.integration = JsonPath.read(json, "$.project.integration");
        int countIntegration = JsonPath.read(json, "$.project.integrationStepList.length()");
        for (int i = 0; i < countIntegration; i++) {
            IntegrationStep integrationStep = new IntegrationStep();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(JsonPath.read(json,"$.project.integrationStepList["+ i + "]"));
            integrationStep.fromJson(jsonObject);
            this.integrationStepList.add(integrationStep);
        }
        int countDeployment= JsonPath.read(json, "$.project.deploymentStepList.length()");
        for (int i = 0; i < countDeployment; i++) {
            DeploymentStep deploymentStep = new DeploymentStep();
            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(JsonPath.read(json,"$.project.deploymentStepList["+ i + "]"));
            deploymentStep.fromJson(jsonObject);
            this.deploymentStepList.add(deploymentStep);
        }
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}

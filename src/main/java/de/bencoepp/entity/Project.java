package de.bencoepp.entity;

import java.util.ArrayList;

public class Project {
    private String title;
    private String description;
    private Boolean deployment;
    private Boolean integration;
    private ArrayList<DeploymentStep> deploymentStepList;
    private ArrayList<IntegrationStep> integrationStepList;

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
}

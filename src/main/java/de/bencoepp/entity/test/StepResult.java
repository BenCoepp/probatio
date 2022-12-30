package de.bencoepp.entity.test;

import com.jayway.jsonpath.JsonPath;

public class StepResult {
    private String title;
    private String command;
    private String output;
    private Boolean successful;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void fromJson(String json) {
        this.title = JsonPath.read(json, "$.result.title");
        this.command = JsonPath.read(json, "$.result.command");
        this.output = JsonPath.read(json, "$.result.output");
        this.successful = JsonPath.read(json, "$.result.successful");
    }
}

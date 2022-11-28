package de.bencoepp.entity;

import com.jayway.jsonpath.JsonPath;

public class IntegrationStep {
    private String title;
    private String description;
    private String command;

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void fromJson(String jsonObject) {
        this.title = JsonPath.read(jsonObject, "$.title");
        this.description = JsonPath.read(jsonObject, "$.description");
        this.command = JsonPath.read(jsonObject, "$.command");
    }
}

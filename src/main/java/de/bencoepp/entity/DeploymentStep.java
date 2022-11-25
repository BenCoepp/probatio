package de.bencoepp.entity;

import com.jayway.jsonpath.JsonPath;

public class DeploymentStep {
    private String title;
    private String description;

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

    public void fromJson(String jsonObject) {
        this.title = JsonPath.read(jsonObject, "$.title");
        this.description = JsonPath.read(jsonObject, "$.description");
    }
}

package de.bencoepp.entity.test;

import com.jayway.jsonpath.JsonPath;

public class TestInfo {
    private String title;
    private String description;
    private Integer version;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void fromJson(String json) {
        this.title = JsonPath.read(json, "$.info.title");
        this.description = JsonPath.read(json, "$.info.description");
        this.version = JsonPath.read(json, "$.info.version");
    }

    public void print() {
    }
}


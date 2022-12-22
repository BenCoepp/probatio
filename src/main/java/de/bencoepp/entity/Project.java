package de.bencoepp.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
public class Project {
    private String title;
    private String description;
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

    public void fromJson(String json) throws JsonProcessingException {
        this.title = JsonPath.read(json, "$.project.title");
        this.description = JsonPath.read(json, "$.project.description");
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
